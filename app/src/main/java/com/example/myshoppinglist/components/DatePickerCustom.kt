package com.example.myshoppinglist.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.screen.RegisterTextFieldViewModel
import com.example.myshoppinglist.ui.theme.text_secondary_light
import com.example.myshoppinglist.utils.FormatUtils
import java.util.*


@ExperimentalComposeUiApi
@Composable
fun DatePickerCustom(
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    backgroundColor: Color? = text_secondary_light,
    reset: Boolean,
    isEnableClick: Boolean? = false,
    context: Context
) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val formatedDate = FormatUtils().getDateFormatted(formatPtBR = true)

    val date = remember { mutableStateOf(formatedDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = FormatUtils().getDateFormatted(dayOfMonth, month, year, true)
            registerTextFieldViewModel.onChangeDateCurrent(
                FormatUtils().getDateFormatted(
                    dayOfMonth,
                    month,
                    year
                )
            )
        },
        year,
        month,
        dayOfMonth
    )

    datePickerDialog.datePicker.maxDate = calendar.time.time

    LaunchedEffect(Unit) {
        registerTextFieldViewModel.onChangeDateCurrent(
            FormatUtils().getDateFormatted(
                dayOfMonth,
                month,
                year,
            )
        )
    }

    registerTextFieldViewModel.dateCurrent.observeForever {
        if (it.isNotBlank()) {
            date.value = FormatUtils().getDateFromatted(Date(it.toString().replace("-", "/")))
        }
    }

    TextInputComponent(
        backgroundColor = backgroundColor!!,
        label = "Data da Compra",
        reset = reset,
        value = date.value,
        maxChar = 30,
        isEnableClick = false,
        modifier = Modifier.fillMaxWidth(.98f),
        customOnClick = object : CustomTextFieldOnClick {
            override fun onClick() {
                if (!isEnableClick!!) {
                    val splitedDate = date.value.split("/")
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