package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.message_error
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary
import java.util.HashMap

@Composable
fun CustomDropdownMenu(valueCollection: HashMap<String, Long>, error: Boolean? = false, callback: CustomTextFieldOnClick, reset: Boolean = false){
    var expanded by remember {mutableStateOf(false)}
    var selectedValue by remember { mutableStateOf(-1L) }
    var items = valueCollection

    if(reset){
        selectedValue = -1L
    }
        Card(elevation = 2.dp, shape = RoundedCornerShape(6.dp), backgroundColor = text_secondary, border = BorderStroke(1.dp, if(error!!) message_error else text_secondary), modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp ).clickable(onClick = { expanded = true })){
            Row(horizontalArrangement = Arrangement.Center){
                Text(items.entries.find { it.value == selectedValue }!!.key,modifier = Modifier.padding(16.dp))
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

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewCustomDropdownMenu(){
//    CustomDropdownMenu(listOf<String>("Cartões", "Card 1", "Card 2", "Card 3"), object : CustomTextFieldOnClick{
//        override fun onChangeValeu(newValue: String) {
//
//        }
//    })
}