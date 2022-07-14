package com.example.myshoppinglist.utils

import java.text.NumberFormat
import java.util.*

class FormatUtils {

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }

}