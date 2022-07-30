package com.example.myshoppinglist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import java.util.*

@Composable
fun BoxPurchaseHistoryComponent(purchaseColleciton: List<Purchase>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(purchaseColleciton) { purchase ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        painter = painterResource(id = purchase.category.imageCircle),
                        contentDescription = null,
                        Modifier
                            .size(46.dp)
                            .padding(top = 3.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.7f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = purchase.name, modifier = Modifier
                                .fillMaxWidth(.6f)
                        )
                        Row(modifier = Modifier
                            .padding(top = 12.dp)) {
                            Text(
                                text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(purchase.price))}",
                                textAlign = TextAlign.End
                            )

                            Text(
                                text = "x ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                    Text(text = FormatUtils().getDate(purchase.date))
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

@Preview(showBackground = true)
@Composable
fun PreivewBoxPurchaseHistoryComponent() {
    BoxPurchaseHistoryComponent(
        listOf(
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-07-2022",
                "0.0".toDouble(),
                TypeCategory.DRINKS
            ),
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-07-2022",
                "0.0".toDouble(),
                TypeCategory.DRINKS
            )
        )
    )
}