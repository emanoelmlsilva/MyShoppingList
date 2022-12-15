package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxShowPriceProduct(modifier:Modifier = Modifier, speding: String, amount: String) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = background_card_light,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Text(text = "PRODUTOS", fontFamily = LatoBlack, fontSize = 18.sp, color = text_secondary)
                Divider(
                    color = text_secondary,
                    modifier = Modifier
                        .fillMaxWidth(0.26f)
                        .height(1.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f).padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(text = "Gastos ", fontFamily = LatoBold, fontSize = 19.sp, color = text_secondary)

                    Text(text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(speding.toDouble()))}", fontFamily = LatoBlack, fontSize = 18.sp, color = primary_dark)
                }
                Divider(
                    color = primary_dark,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "Quantidades", fontFamily = LatoBold, fontSize = 19.sp, color = text_secondary)

                    Text(text = amount, fontFamily = LatoBlack, fontSize = 18.sp, color = primary_dark)
                }
            }
        }
    }
}