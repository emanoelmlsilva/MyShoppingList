package com.example.myshoppinglist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun SpendingComponent(price: Double, isVisibleValue: Boolean, callback: Callback? = null, currentCreditCard: CreditCard?,creditCards: List<CreditCard>, callbackCreditCard: CallbackCreditCard){
    Column(modifier = Modifier
        .background(secondary)
        .height(120.dp)
        .fillMaxWidth()
        .padding(top = 8.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Gastos", modifier = Modifier.padding(top = 16.dp, start = 16.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
            IconButton(onClick = { callback?.onClick() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "show screen purchase",
                    tint = secondary_dark,
                )
            }
        }
        Card(modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 10.dp)
            .fillMaxSize(),  elevation = 0.dp) {
            Row(modifier = Modifier
                .background(if(true) secondary else secondary_light), verticalAlignment = Alignment.CenterVertically){
                if(isVisibleValue){
                    Text(text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(price))}", modifier = Modifier.padding(start = 16.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    if(currentCreditCard != null) BoxDropdownCardCredit(creditCards, currentCreditCard, callbackCreditCard)
                }

            }
        }

    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSpendingComponent(){
//    SpendingComponent(0.0,false, object : Callback{
//        override fun onClick() {
//
//        }
//    },listOf<CreditCard>())
//}