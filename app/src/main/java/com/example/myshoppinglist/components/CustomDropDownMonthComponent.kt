package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.background_card
import com.example.myshoppinglist.ui.theme.text_title_secondary


@Composable
fun CustomDropDownMonthComponent(
    callback: CustomTextFieldOnClick,
    monthCollection: List<String>,
    monthCurrent: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 0.dp, backgroundColor = background_card, modifier = Modifier
            .padding(6.dp, 16.dp, 16.dp, 16.dp)
            .clickable(onClick = {
                if (monthCollection.isNotEmpty()) {
                    expanded = true
                }
            })
    ) {

        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "- $monthCurrent",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = text_title_secondary
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (monthCollection.isNotEmpty()) {
                monthCollection.forEach { month ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        callback.onChangeValue(month)
                    }) {
                        Text(text = month)
                    }
                }
            }
        }
    }

}