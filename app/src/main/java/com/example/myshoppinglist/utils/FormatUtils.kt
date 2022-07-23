package com.example.myshoppinglist.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class FormatUtils {

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }

    fun getDate(time: Long): String{
        val yearCurrent = Date().year
        val date = Date(time)
        val pattern = if(date.year == yearCurrent) "dd MMM" else "dd MMM yyyy"
        return SimpleDateFormat(pattern).format(date)
    }
}