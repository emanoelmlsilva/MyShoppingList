package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
@Composable
fun BoxPurchaseHistoryComponent(
    purchaseCollection: List<PurchaseAndCategoryDTO>
) {
    val context = LocalContext.current

    if (purchaseCollection.isNotEmpty()) {
        BaseLazyColumnScroll(
            modifier = Modifier.padding(bottom = 0.dp, start = 24.dp, end = 24.dp)
        ) {
            itemsIndexed(purchaseCollection) { index, purchaseAndCategory ->
                val purchase = purchaseAndCategory.purchaseDTO
                val category = purchaseAndCategory.categoryDTO

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index == (purchaseCollection.size - 1)) 56.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Box() {
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

                            if (purchase.isRepeat) {
                                Card(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape),
                                    backgroundColor = text_primary
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.EventRepeat,
                                        contentDescription = null,
                                        tint = text_secondary_light,
                                        modifier = Modifier
                                            .padding(4.dp)
                                    )
                                }
                            }

                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth(.3f)
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
                                text = purchaseAndCategory.dateFormat,
                                fontFamily = LatoRegular,
                                fontSize = 12.sp,
                                color = text_primary_light
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "${if (purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.amountOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                                    fontFamily = LatoRegular,
                                    fontSize = 12.sp,
                                    color = text_primary_light
                                )

                                Text(
                                    text = "R$ ${
                                        purchaseAndCategory.priceFormat
                                    }",
                                    modifier = Modifier.padding(start = 10.dp),
                                    textAlign = TextAlign.End,
                                    fontFamily = LatoBlack,
                                    fontSize = 12.sp,
                                    color = text_primary_light
                                )

                            }

                            if (purchase.discount > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                ) {
                                    Text(
                                        fontFamily = LatoRegular,
                                        text = "desconto", modifier = Modifier,
                                        textAlign = TextAlign.Start,
                                        fontSize = 12.sp,
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        fontFamily = LatoRegular,
                                        fontSize = 12.sp,
                                        text = "R$ -${
                                            purchaseAndCategory.discountFormat
                                        }"
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        fontFamily = LatoBlack,
                                        fontSize = 12.sp,
                                        color = text_primary_light,
                                        text = "R$ ${
                                            purchaseAndCategory.totalWithoutDiscountFormat
                                        }",
                                    )

                                }
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
    } else {
        EmptyTextComponent("Não foi realizada nenhuma compras nos últimos 7 dias.")
    }

}