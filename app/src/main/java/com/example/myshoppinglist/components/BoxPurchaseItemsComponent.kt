package com.example.myshoppinglist.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.screen.ProductManagerFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxPurchaseItemsComponent(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    purchaseInfoCollection: List<PurchaseInfo>,
    productManagerFieldViewModel: ProductManagerFieldViewModel
) {
    val listState: LazyListState = rememberLazyListState()

    val expands = remember { mutableStateListOf<Int>() }
    var visibleAnimation by remember { mutableStateOf(false) }

    fun expandableContainer(index: Int) {
        val auxExpandeds = expands.toMutableList()
        expands.removeAll(expands)
        expands.addAll(changeVisibility(index, auxExpandeds))
    }

    fun isExpanded(index: Int, visibilityCollection: MutableList<Int>): Boolean {
        return visibilityCollection.indexOf(index) != -1
    }

    productManagerFieldViewModel.visibleAnimation.observe(lifecycleOwner){
        visibleAnimation = it
    }

    BaseLazyColumnScroll(
        listState = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        callback = object : VisibleCallback {
            override fun onChangeVisible(visible: Boolean) {
                productManagerFieldViewModel.onChangeVisibleAnimation(visible)
            }
        }
    ) {
        purchaseInfoCollection.mapIndexed { indexInfo, purchaseInfo ->
            item {
                Column( modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandableContainer(indexInfo) }
                            .padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { expandableContainer(indexInfo) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = if (isExpanded(
                                            indexInfo,
                                            expands
                                        )
                                    ) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = text_primary,
                                )
                            }
                            IconCategoryComponent(
                                modifier = Modifier.padding(start = 6.dp),
                                iconCategory = AssetsUtils.readIconBitmapById(
                                    context,
                                    purchaseInfo.avatar
                                )!!
                                    .asImageBitmap(),
                                colorIcon = purchaseInfo.color,
                                size = 30.dp,
                                enabledBackground = true
                            )

                            Text(
                                text = purchaseInfo.title.capitalize(),
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .padding(start = 8.dp),
                                fontFamily = LatoBold
                            )
                        }
                        Text(
                            text = "${if (purchaseInfo.purchaseCollection.size < 100) "0${purchaseInfo.purchaseCollection.size}" else purchaseInfo.purchaseCollection.size}",
                            fontFamily = LatoBlack
                        )
                    }
                }

            }

            if (isExpanded(
                    indexInfo,
                    expands
                )
            ) {

                itemsIndexed(purchaseInfo.purchaseCollection) { index, purchaseAndCategory ->
                    val purchase = purchaseAndCategory.purchase

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(.9f)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    fontFamily = LatoRegular,
                                    text = purchase.locale, modifier = Modifier
                                        .padding(start = 12.dp),
                                    textAlign = TextAlign.Start
                                )

                                Column(horizontalAlignment = Alignment.End) {

                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.Bottom,
                                        modifier = Modifier
                                            .fillMaxWidth(.75f)
                                    ) {
                                        Text(
                                            fontFamily = LatoRegular,
                                            fontSize = 12.sp,
                                            text = "${if (purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}"
                                        )

                                        Text(
                                            fontFamily = LatoRegular,
                                            fontSize = 12.sp,
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
                                                verticalAlignment = Alignment.Bottom,
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
                                                    fontFamily = LatoRegular,
                                                    fontSize = 12.sp,
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

                                }

                                Text(
                                    text = FormatUtils().getNameDay(purchase.date, false)
                                        .uppercase(),
                                    fontFamily = LatoBlack,
                                    fontSize = 12.sp,
                                    color = text_primary_light,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                )

                            }
                            Divider(
                                color = divider_ligth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 6.dp,
                            end = 6.dp,
                            bottom = if (indexInfo == (purchaseInfoCollection.size - 1)) 56.dp else 8.dp
                        ),
                ) {
                    Text(text = "Total", fontFamily = LatoBlack, color = text_title_secondary)
                    Text(
                        text = "- R$ ${
                            MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    purchaseInfo.value
                                )
                            )
                        }",
                        fontFamily = LatoBlack,
                        modifier = Modifier.padding(start = 8.dp, end = 6.dp),
                        color = primary_dark
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )

            }

        }
    }
}