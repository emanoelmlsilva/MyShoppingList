package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalAnimationApi
@Composable
fun BoxPurchaseHistoryComponent(visibleAnimation: Boolean, purchaseColleciton: List<Purchase>, callback: VisibleCallback) {
    if(purchaseColleciton.isNotEmpty()){
        BaseLazyColumnScroll(modifier = Modifier.padding(bottom = 0.dp, start = 16.dp, end = 16.dp), visibleAnimation = visibleAnimation, callback = callback) {
            itemsIndexed(purchaseColleciton) { index, purchase ->
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = if(index == (purchaseColleciton.size - 1)) 56.dp else 0.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = painterResource(id = purchase.category.imageCircle),
                            contentDescription = null,
                            Modifier
                                .size(46.dp)
                                .padding(top = 3.dp)
                        )
                        Column(
                            modifier = Modifier.padding(start = 12.dp).fillMaxWidth(.58f)
                        ) {
                            Text(
                                text = purchase.name.capitalize(), fontFamily = LatoRegular, fontSize = 16.sp, color = text_primary_light

                            )
                            Text(
                                text = purchase.locale, modifier = Modifier.padding(top = 12.dp), fontFamily = LatoRegular, fontSize = 12.sp, color = text_primary_light
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                            Text(text = FormatUtils().getNameDay(purchase.date).uppercase(), fontFamily = LatoRegular, fontSize = 12.sp, color = text_primary_light)

                            Row(modifier = Modifier
                                .padding(top = 12.dp)) {
                                Text(
                                    text = "${if(purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                                    fontFamily = LatoRegular, fontSize = 12.sp, color = text_primary_light
                                )

                                Text(
                                    text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(purchase.price))}",
                                    modifier = Modifier.padding(start = 10.dp),
                                    textAlign = TextAlign.End,
                                    fontFamily = LatoBlack, fontSize = 12.sp, color = text_primary_light
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

@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun PreivewBoxPurchaseHistoryComponent() {
    BoxPurchaseHistoryComponent(true,
        listOf(
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-07-2022",
                "1323.23".toDouble(),
                TypeCategory.DRINKS
            ),
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-07-2022",
                "23423.43".toDouble(),
                TypeCategory.DRINKS
            )
        ),
        object : VisibleCallback(){
            override fun onChangeVisible(visible: Boolean) {
                TODO("Not yet implemented")
            }

        }
    )
}