package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackOptions
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils


@Composable
fun BoxPurchaseSpendingComponent(
    purchaseAndCategory: PurchaseAndCategory,
    idPurchaseEdit: Long,
    callback: Callback,
    callbackOptions: CallbackOptions
) {
    val purchase = purchaseAndCategory.purchase
    val category = purchaseAndCategory.category
    val context = LocalContext.current
    val options = idPurchaseEdit != 0L && purchase.myShoppingId == idPurchaseEdit

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { callback.onClick() })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconCategoryComponent(
                iconCategory = AssetsUtils.readIconBitmapById(context, category.idImage)!!
                    .asImageBitmap(),
                size = 46.dp,
                colorIcon = Color(category.color),
                enabledBackground = true,
                enableClick = true,
                callback = object : Callback {
                    override fun onClick() {
                        callback.onClick()
                    }
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp, top = if (purchase.discount > 0) {
                            24.dp
                        } else {
                            0.dp
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth(.25f)
                ) {
                    Text(
                        text = purchase.name.capitalize(),
                        fontFamily = LatoBlack,
                        color = text_primary_light,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = purchase.locale,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        fontSize = 12.sp,
                        color = text_title_secondary,
                        textAlign = TextAlign.Start
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "R$ ${
                            MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    purchase.price
                                )
                            )
                        }",
                        fontFamily = LatoBlack,
                        color = text_primary_light,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                    if (purchase.discount > 0) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth(.85f)
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                fontFamily = LatoRegular,
                                text = "desconto", modifier = Modifier,
                                textAlign = TextAlign.Start,
                                fontSize = 12.sp,
                            )
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
                                fontFamily = LatoBlack,
                                color = text_primary_light,
                                text = "R$ ${
                                    MaskUtils.maskValue(
                                        MaskUtils.convertValueDoubleToString(
                                            purchase.price - purchase.discount
                                        )
                                    )
                                }",
                            )

                        }
                    }
                    Text(
                        text = "${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                        color = text_title_secondary, modifier = Modifier
                            .padding(top = 8.dp), fontSize = 14.sp, textAlign = TextAlign.End
                    )
                }
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Options(
                options,
                purchase.purchaseCardId,
                callbackOptions
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

@Composable
fun Options(
    options: Boolean,
    idCard: Long,
    callback: CallbackOptions
) {
    if (options) {
        Card(
            modifier = Modifier.padding(bottom = 14.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = background_divider
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                IconButton(modifier = Modifier
                    .padding(end = 4.dp),
                    onClick = {
                        callback.onEditable(idCard)
                    })
                {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = text_primary,
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .width(1.dp)
                        .height(45.dp)
                )

                IconButton(modifier = Modifier
                    .padding(horizontal = 4.dp), onClick = {
                    callback.onDelete()
                })
                {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = text_primary,
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .width(1.dp)
                        .height(45.dp)
                )

                IconButton(modifier = Modifier
                    .padding(start = 4.dp), onClick = {
                    callback.onTransfer(true, Purchase(), 0L)
                })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.transfer),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = text_primary
                    )
                }
            }
        }
    }
}