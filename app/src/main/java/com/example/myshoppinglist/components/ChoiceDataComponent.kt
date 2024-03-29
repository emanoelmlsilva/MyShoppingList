package com.example.myshoppinglist.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.fieldViewModel.ChoiceDataFieldViewModel
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.background_card_light
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.FormatDateUtils
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalMaterialApi
@Composable
fun ChoiceDataComponent(
    idCard: Long,
    dataCurrent: String,
    choiceDataFieldViewModel: ChoiceDataFieldViewModel,
    callback: Callback
) {
    var expanded by remember { mutableStateOf(false) }
    val yearCurrent by choiceDataFieldViewModel.yearCurrent.collectAsState()
    val monthCurrent by choiceDataFieldViewModel.monthCurrent.collectAsState()
    val dateMonthAndYear by choiceDataFieldViewModel.dateMonthAndYear.collectAsState()
    val monthCollection by choiceDataFieldViewModel.monthCollection.collectAsState()

    LaunchedEffect(key1 = dataCurrent) {
        val splitDataCurrent = if (dataCurrent.isNotBlank()) dataCurrent.split("-") else listOf()
        if (splitDataCurrent.isNotEmpty()) {
            choiceDataFieldViewModel.updateYear(splitDataCurrent[0])
        }

        if (splitDataCurrent.isNotEmpty()) {
            choiceDataFieldViewModel.updateMonth(splitDataCurrent[1])
        }
    }

    LaunchedEffect(key1 = idCard) {
        choiceDataFieldViewModel.updateMonths(idCard)
    }

    LaunchedEffect(key1 = yearCurrent, key2 = dateMonthAndYear.size) {
        choiceDataFieldViewModel.updateMonthCollection()
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
                            choiceDataFieldViewModel.updateYear(yearDate)
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
                if (yearCurrent.isNotBlank() && monthCollection.isNotEmpty()) {
                    items(monthCollection) { month ->
                        val isChoiceCurrent = monthCurrent == month
                        CustomerChip(
                            paddingVertical = 0.dp,
                            label = FormatDateUtils().getNameMonth(month)
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            isEnabled = true,
                            isBackgroundCircle = true,
                            isChoice = isChoiceCurrent,
                            color = background_card_light,
                            callback = object : Callback {
                                override fun onClick() {
                                    var monthDate = ""
                                    var monthChoice = month

                                    if (monthCurrent != month) {
                                        monthDate = "$yearCurrent-$month"
                                    } else{
                                        monthChoice = ""
                                    }

                                    choiceDataFieldViewModel.updateMonth(monthChoice)
                                    callback.onChangeValue(monthDate)
                                }
                            })
                    }
                }
            }

        }
    }
}