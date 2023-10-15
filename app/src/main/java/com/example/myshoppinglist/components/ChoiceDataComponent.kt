package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.background_card_light
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.FormatUtils
import java.util.*

@ExperimentalMaterialApi
@Composable
fun ChoiceDataComponent(
    idCard: Long,
    dataCurrent: String,
    callback: Callback
) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val purchaseController = PurchaseController()
    var expanded by remember { mutableStateOf(false) }
    val splitDataCurrent = if (dataCurrent.isNotBlank()) dataCurrent.split("-") else listOf()
    var yearCurrent by remember { mutableStateOf(if (splitDataCurrent.isNotEmpty()) splitDataCurrent[0] else "") }
    var monthCurrent by remember { mutableStateOf(if (splitDataCurrent.isNotEmpty()) splitDataCurrent[1] else "") }
    val dateMonthAndYear = remember { mutableStateMapOf<String, MutableList<String>>() }

    LaunchedEffect(key1 = idCard) {
        purchaseController.getMonthByIdCardDB(idCard).observe(lifecycleOwner){
//            dateMonthAndYear
        }
    }

    Column {
        Spacer(Modifier.size(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Data:", fontFamily = LatoBlack)

            Card(
                elevation = 0.dp, modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable(onClick = { expanded = true })
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = yearCurrent,
                        color = text_primary,
                        fontSize = 12.sp
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = text_primary,
                    )

                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    dateMonthAndYear.keys.forEach { yearDate ->
                        DropdownMenuItem(modifier = Modifier.height(25.dp), onClick = {
                            expanded = false
                            yearCurrent = yearDate
                        }) {
                            Text(text = yearDate, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(.27f)
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                items(dateMonthAndYear.values.toList()) { months ->
                    months.forEach { month ->
                        val isChoiceCurrent = monthCurrent == month
                        CustomerChip(
                            paddingVertical = 0.dp,
                            label = FormatUtils().getNameMonth(month)
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            isEnabled = true,
                            isBackgroundCircle = true,
                            isChoice = isChoiceCurrent,
                            color = background_card_light,
                            callback = object : Callback {
                                override fun onClick() {
                                    if (monthCurrent == month) {
                                        monthCurrent = ""
                                    } else {
                                        monthCurrent = month

                                        callback.onChangeValue("$yearCurrent-$monthCurrent")
                                    }

                                }
                            })
                    }
                }
            }

        }

    }
}