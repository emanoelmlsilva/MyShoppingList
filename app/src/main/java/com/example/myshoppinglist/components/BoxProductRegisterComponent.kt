package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CallbackPurchase
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.enums.TypeState
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalAnimationApi
@Composable
fun BoxProductRegisterComponent(
    context: Context,
    purchaseInfoCollection: MutableList<PurchaseInfo>,
    callbackPurchase: CallbackPurchase
) {
    val expandeds = remember { mutableStateListOf<Int>() }

    fun expandableContainer(index: Int) {
        val auxExpandeds = expandeds.toMutableList()
        expandeds.removeAll(expandeds)
        expandeds.addAll(changeVisibility(index, auxExpandeds))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        purchaseInfoCollection.mapIndexed { indexInfo, purchaseInfo ->
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clickable { expandableContainer(indexInfo) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(modifier = Modifier
                            .padding(4.dp), onClick = { expandableContainer(indexInfo) }) {
                            Icon(
                                imageVector = if (isExpanded(
                                        indexInfo,
                                        expandeds
                                    )
                                ) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = text_primary,
                            )
                        }
                        Text(
                            text = purchaseInfo.title,
                            modifier = Modifier.padding(start = 8.dp),
                            fontFamily = LatoBold
                        )
                    }
                    Divider(
                        color = secondary_light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }
            }
            if (isExpanded(
                    indexInfo,
                    expandeds
                )
            ) itemsIndexed(purchaseInfo.purchaseCollection) { index, purchaseAndCategory ->
                val category = purchaseAndCategory.category
                val purchase = purchaseAndCategory.purchase

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.Center
                    ) {

                        IconCategoryComponent(
                            modifier = Modifier.padding(start = 6.dp),
                            iconCategory = AssetsUtils.readIconBitmapById(
                                context,
                                category.idImage
                            )!!
                                .asImageBitmap(),
                            colorIcon = Color(category.color),
                            size = 40.dp,
                            enabledBackground = true
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    fontFamily = LatoRegular,
                                    text = purchase.name, modifier = Modifier
                                        .padding(start = 12.dp),
                                    textAlign = TextAlign.Start
                                )
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        fontFamily = LatoRegular,
                                        fontSize = 12.sp,
                                        text = "${if (purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}"
                                    )
                                    Text(
                                        fontFamily = LatoBold,
                                        text = "R$ ${
                                            MaskUtils.maskValue(
                                                MaskUtils.convertValueDoubleToString(
                                                    purchase.price
                                                )
                                            )
                                        }",
                                        modifier = Modifier
                                            .padding(start = 12.dp),
                                    )
                                }
                            }
                            if (purchase.discount > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth(.75f)
                                        .padding(top = 8.dp)
                                ) {
                                    Text(
                                        fontFamily = LatoRegular,
                                        text = "desconto", modifier = Modifier
                                            .padding(start = 16.dp),
                                        textAlign = TextAlign.Start,
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text(
                                            fontFamily = LatoRegular,
                                            fontSize = 12.sp,
                                            text = "R$ -${
                                                MaskUtils.maskValue(
                                                    MaskUtils.convertValueDoubleToString(
                                                        purchase.discount
                                                    )
                                                )
                                            }"
                                        )
                                        Text(
                                            fontFamily = LatoBold,
                                            text = "R$ ${
                                                MaskUtils.maskValue(
                                                    MaskUtils.convertValueDoubleToString(
                                                        purchase.price - purchase.discount
                                                    )
                                                )
                                            }",
                                            modifier = Modifier
                                                .padding(start = 12.dp),
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp), horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    expandeds.removeAll(expandeds)
                                    callbackPurchase.onChangeIndex(indexInfo, index, TypeState.EDIT)
                                })
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = null,
                                        tint = text_primary,
                                    )
                                }
                                IconButton(onClick = {
                                    expandeds.removeAll(expandeds)
                                    callbackPurchase.onChangeIndex(
                                        indexInfo,
                                        index,
                                        TypeState.DELETE
                                    )
                                })
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = null,
                                        tint = text_primary,
                                    )
                                }

                            }
                        }
                    }
                    Divider(
                        color = divider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }


            } else null
        }
    }
}

fun changeVisibility(index: Int, visibilityCollection: MutableList<Int>): MutableList<Int> {
    if (visibilityCollection.indexOf(index) != -1) {
        visibilityCollection.remove(index)
    } else {
        visibilityCollection.add(index)
    }

    return visibilityCollection
}

fun isExpanded(index: Int, visibilityCollection: MutableList<Int>): Boolean {
    return visibilityCollection.indexOf(index) != -1
}