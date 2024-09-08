package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.primary_dark
import com.example.myshoppinglist.ui.theme.secondary_light

@Composable
fun PageVisibility(finished: Boolean){
    Row(
        modifier = Modifier.fillMaxWidth(0.45f),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = primary_dark,
            modifier = Modifier
                .width(75.dp)
                .height(5.dp)
        ) {}

        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = if (finished) primary_dark else secondary_light,
            modifier = Modifier
                .width(75.dp)
                .height(5.dp)
        ) {}
    }
}