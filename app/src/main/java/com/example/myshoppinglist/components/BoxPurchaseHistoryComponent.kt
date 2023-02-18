package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.LatoRegular
import com.example.myshoppinglist.ui.theme.text_primary_light
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalAnimationApi
@Composable
fun BoxPurchaseHistoryComponent(
    visibleAnimation: Boolean,
    purchaseColleciton: List<PurchaseAndCategory>,
    callback: VisibleCallback
) {
    val context = LocalContext.current

    if (purchaseColleciton.isNotEmpty()) {
        BaseLazyColumnScroll(
            modifier = Modifier.padding(bottom = 0.dp, start = 24.dp, end = 24.dp),
            visibleAnimation = visibleAnimation,
            callback = callback
        ) {
            itemsIndexed(purchaseColleciton) { index, purchaseAndCategory ->
                val purchase = purchaseAndCategory.purchase ?: Purchase()
                val category = purchaseAndCategory.category ?: Category()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index == (purchaseColleciton.size - 1)) 56.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
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
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth(.58f)
                        ) {
                            Text(
                                text = purchase.name.capitalize(),
                                fontFamily = LatoRegular,
                                fontSize = 16.sp,
                                color = text_primary_light

                            )
                            Text(
                                text = purchase.locale,
                                modifier = Modifier.padding(top = 8.dp),
                                fontFamily = LatoRegular,
                                fontSize = 12.sp,
                                color = text_primary_light
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = FormatUtils().getNameDay(purchase.date).uppercase(),
                                fontFamily = LatoRegular,
                                fontSize = 12.sp,
                                color = text_primary_light
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "${if (purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                                    fontFamily = LatoRegular,
                                    fontSize = 12.sp,
                                    color = text_primary_light
                                )

                                Text(
                                    text = "R$ ${
                                        MaskUtils.maskValue(
                                            MaskUtils.convertValueDoubleToString(
                                                purchase.price
                                            )
                                        )
                                    }",
                                    modifier = Modifier.padding(start = 10.dp),
                                    textAlign = TextAlign.End,
                                    fontFamily = LatoBlack,
                                    fontSize = 12.sp,
                                    color = text_primary_light
                                )

                            }
                        }

                    }
                    Divider(
                        color = Color(0x46C4C4C4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }
            }
        }
    }

}