package com.example.myshoppinglist.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@SuppressLint("SuspiciousIndentation")
@Composable
fun BoxShowPriceProduct(speding: String, amount: String) {
    val sizeIcon = 25.dp

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(.4f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_monetization_on_24),
                contentDescription = null,
                modifier = Modifier
                    .size(sizeIcon)
                    .padding(end = 6.dp),
                tint = text_primary
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "R$ ${
                        MaskUtils.maskValue(speding)
                    }",
                    fontFamily = LatoBlack,
                    fontSize = 18.sp,
                    color = primary_dark
                )
                Divider(
                    color = primary_dark,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(1.5.dp)
                )
            }
        }

        Divider(
            color = secondary_dark,
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(.5f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(.55f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_counter),
                contentDescription = null,
                modifier = Modifier
                    .size(sizeIcon)
                    .padding(end = 6.dp),
                tint = text_primary
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = amount,
                    fontFamily = LatoBlack,
                    fontSize = 18.sp,
                    color = primary_dark
                )
                Divider(
                    color = primary_dark,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(1.5.dp)
                )
            }
        }

    }
}