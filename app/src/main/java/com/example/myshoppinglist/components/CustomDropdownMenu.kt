package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.*
import java.util.*

@Composable
fun CustomDropdownMenu(backgroundColor: Color = text_secondary, idCardEditable: Long?, valueCollection: HashMap<String, Long>, error: Boolean? = false, isEnableClick: Boolean? = true, callback: CustomTextFieldOnClick, reset: Boolean = false){
    var expanded by remember {mutableStateOf(false)}
    var selectedValue by remember { mutableStateOf(idCardEditable) }
    var items = valueCollection
    var nameCard by remember { mutableStateOf("") }

    LaunchedEffect(key1 = valueCollection){
        val itemCard = items.entries.find { it.value == selectedValue }
        if(itemCard != null) {
            nameCard = itemCard.key
        }
    }

    LaunchedEffect(key1 = idCardEditable, key2 = items){
        val itemCard = items.entries.find { it.value == idCardEditable }
        if(itemCard != null) {
            nameCard = itemCard.key
        }
    }

        Card(elevation = 2.dp, shape = RoundedCornerShape(6.dp), backgroundColor = background_text_field_dark, border = BorderStroke(1.dp, if(error!!) message_error else text_secondary),
            modifier = Modifier
                .padding(0.dp, 16.dp, 16.dp, 16.dp)
                .clickable(enabled = isEnableClick!!, onClick = {
                    expanded = true
                })){
            Row(modifier = Modifier.background(backgroundColor), horizontalArrangement = Arrangement.Center){
                Text(nameCard,modifier = Modifier.padding(16.dp), fontFamily = LatoRegular, color = text_primary)
                Icon(
                    painter = painterResource(if(expanded) R.drawable.ic_baseline_arrow_drop_up_24 else R.drawable.ic_baseline_arrow_drop_down_24),
                    contentDescription = null,
                    tint = secondary_dark,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                items.entries.forEachIndexed { index, hashMap ->
                    val key = hashMap.key
                    val value = hashMap.value
                    DropdownMenuItem(onClick = {
                        selectedValue = value
                        expanded = false
                        callback.onChangeValueLong(value)
                    }) {
                        Text(text = key)
                    }
                }
            }

    }

}