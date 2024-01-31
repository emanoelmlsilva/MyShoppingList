package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxPurchaseItemsComponent(
    context: Context,
    purchaseInfoCollection: List<PurchaseInfo>
) {
    val expands = remember { mutableStateListOf<Int>() }

    fun expandableContainer(index: Int) {
        val auxExpandeds = expands.toMutableList()
        expands.removeAll(expands)
        expands.addAll(changeVisibility(index, auxExpandeds))
    }

    fun isExpanded(index: Int, visibilityCollection: MutableList<Int>): Boolean {
        return visibilityCollection.indexOf(index) != -1
    }

    BaseLazyColumnScroll(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        purchaseInfoCollection.mapIndexed { indexInfo, purchaseInfo ->
            item {
                Card(
                    shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                    backgroundColor = secondary,
                    elevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier
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


            }

            if (isExpanded(
                    indexInfo,
                    expands
                )
            ) {
                itemsIndexed(purchaseInfo.purchaseCollection) { index, purchaseAndCategory ->
                    val purchase = purchaseAndCategory.purchaseDTO
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(background_text_field)
                            .padding(start = 8.dp, end = 12.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 6.dp)) {
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
                                                purchaseAndCategory.priceFormat
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
                                                        purchaseAndCategory.discountFormat
                                                    }"
                                                )
                                                Text(
                                                    fontFamily = LatoRegular,
                                                    fontSize = 12.sp,
                                                    text = "R$ ${
                                                        purchaseAndCategory.totalWithoutDiscountFormat
                                                    }",
                                                    modifier = Modifier
                                                        .padding(start = 12.dp),
                                                )
                                            }
                                        }
                                    }

                                }

                                Text(
                                    text = purchaseAndCategory.dateFormat,
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
                Card(
                    shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp),
                    backgroundColor = secondary,
                    elevation = 1.dp,
                    modifier = Modifier.padding(bottom = if (indexInfo == (purchaseInfoCollection.size - 1)) 56.dp else 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 8.dp,
                                end = 8.dp, bottom = 8.dp
                            ),
                    ) {
                        Text(text = "Total", fontFamily = LatoBlack, color = text_title_secondary)
                        Text(
                            text = "- R$ ${MaskUtils.maskValue(String.format("%.2f", purchaseInfo.value))}",
                            fontFamily = LatoBlack,
                            modifier = Modifier.padding(start = 8.dp, end = 6.dp),
                            color = primary_dark
                        )
                    }
                }
            }
        }
    }
}