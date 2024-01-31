package com.example.myshoppinglist.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.text_secondary_light
import com.example.myshoppinglist.utils.FormatDateUtils
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun DatePickerCustom(
    dateCurrent: String = "",
    backgroundColor: Color? = text_secondary_light,
    isEnableClick: Boolean? = false,
    callback: Callback,
    context: Context
) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val formatedDate = FormatDateUtils().getDateFormatted(formatPtBR = true)

    var date by remember { mutableStateOf(formatedDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, yearPicker: Int, monthPicker: Int, dayOfMonthPicker: Int ->
            date =
                FormatDateUtils().getDateFormatted(dayOfMonthPicker, monthPicker, yearPicker, false)
            callback.onChangeValue(date)
        },
        year,
        month,
        dayOfMonth
    )

    datePickerDialog.datePicker.maxDate = calendar.time.time

    fun getFormattedDate(date: String): String {
        return if (date.contains("-")) {
            val formatter = date.split("-")
            "${formatter[2]}/${formatter[1]}/${formatter[0]}"
        } else {
            date
        }
    }

    LaunchedEffect(Unit) {
        date =
            FormatDateUtils().getDateFormatted(
                dayOfMonth,
                month,
                year,
            )
        callback.onChangeValue(date)

    }


    LaunchedEffect(key1 = dateCurrent) {
        if (dateCurrent.isNotEmpty()) {
            date = dateCurrent
            callback.onChangeValue(date)
        }
    }

    TextInputComponent(
        backgroundColor = backgroundColor!!,
        label = "Data da Compra",
        value = getFormattedDate(date),
        maxChar = 30,
        isEnableClick = false,
        modifier = Modifier.fillMaxWidth(.98f),
        customOnClick = object : CustomTextFieldOnClick {
            override fun onClick() {
                if (!isEnableClick!!) {
                    val splitedDate = getFormattedDate(date).split("/")
                    datePickerDialog.updateDate(
                        splitedDate[2].toInt(),
                        splitedDate[1].toInt() - 1,
                        splitedDate[0].toInt()
                    )
                    datePickerDialog.show()
                }
            }
        })
}