package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CallbackPurchase
import com.example.myshoppinglist.callback.CallbackSwipe
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.enums.TypeState
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalAnimationApi
@Composable
fun BoxProductRegisterComponent(
    context: Context,
    purchaseAndCategoryDTOCollection: MutableList<PurchaseAndCategoryDTO>,
    callbackPurchase: CallbackPurchase
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(purchaseAndCategoryDTOCollection) { index, purchaseAndCategory ->
            val category = purchaseAndCategory.categoryDTO
            val purchase = purchaseAndCategory.purchaseDTO

            SwipeComponent(
                index = index,
                onSwipe = {  },
                onDragStart = {  },
                onDragEnd = { },
                colorBackground = background_card,
                callback = object : CallbackSwipe {
                    override fun onHandlerLeftAction() {
                        callbackPurchase.onChangeIndex(
                            index,
                            TypeState.DELETE
                        )
                    }

                    override fun onHandlerHighAction() {
                        callbackPurchase.onChangeIndex(index, TypeState.EDIT)
                    }
                },
                dismissBackground = {
                    Column{
                        Card(
                            elevation = 0.dp,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.fillMaxWidth().background(background_card_light)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth().background(background_card_light)
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
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 18.dp), horizontalArrangement = Arrangement.End
//                        ) {
//                            IconButton(onClick = {
//                                callbackPurchase.onChangeIndex(index, TypeState.EDIT)
//                            })
//                            {
//                                Icon(
//                                    imageVector = Icons.Outlined.Edit,
//                                    contentDescription = null,
//                                    tint = text_primary,
//                                )
//                            }
//                            IconButton(onClick = {
//                                callbackPurchase.onChangeIndex(
//                                    index,
//                                    TypeState.DELETE
//                                )
//                            })
//                            {
//                                Icon(
//                                    imageVector = Icons.Outlined.Delete,
//                                    contentDescription = null,
//                                    tint = text_primary,
//                                )
//                            }
//
//                        }
                                        }
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

                })
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