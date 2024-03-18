package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.ui.theme.*

@Composable
fun DayClosedInvoiceComponent(currentDayClosedInvoice: Int? = null, callbackDay: Callback) {

    val daysOfWeekOneCollections = (1..10).toList()
    val daysOfWeekTwoCollections = (11..20).toList()
    val daysOfWeekThreeCollections = (21..30).toList()
    val listDay = listOf(31)

    var dayChoiceCurrent by remember { mutableStateOf(1) }

    LaunchedEffect(key1 = currentDayClosedInvoice){
        if(currentDayClosedInvoice != null){
            dayChoiceCurrent = currentDayClosedInvoice
        }
    }

    val callback = object : Callback{
        override fun onChangeValue(value: Int) {
            dayChoiceCurrent = value
            callbackDay.onChangeValue(dayChoiceCurrent)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp, bottom = 26.dp)) {

        Text(text = "Dia da Fatura Fechar ", fontFamily = LatoBold, modifier = Modifier.padding(vertical = 8.dp))

        Column(modifier = Modifier) {


            rowCollection(daysOfWeekOneCollections, dayChoiceCurrent, callback)

            rowCollection(daysOfWeekTwoCollections, dayChoiceCurrent, callback)

            rowCollection(daysOfWeekThreeCollections, dayChoiceCurrent, callback)

            rowCollection(listDay, dayChoiceCurrent, callback)

        }

    }

}

@Composable
fun rowCollection(dayCollection: List<Int>, dayChoiceCurrent: Int, callback: Callback) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        dayCollection.forEach { day ->
            Card(backgroundColor = (if (dayChoiceCurrent == day) background_card_light else background_card),
                modifier = Modifier
                    .padding(all = 2.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable(enabled = true, onClick = { callback.onChangeValue(day) })) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$day",
                        fontFamily = LatoBlack,
                        color = text_primary,
                        fontSize = 12.sp
                    )
                }

            }
        }

    }
}