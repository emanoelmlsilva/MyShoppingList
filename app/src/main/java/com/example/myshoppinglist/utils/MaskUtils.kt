package com.example.myshoppinglist.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object MaskUtils {

    @JvmStatic
    fun replaceAll(value: String): String {
        return value.replace("[.,]".toRegex(), "")
    }

    @JvmStatic
    fun maskQuantity(value: String): String{
        if(value.isBlank()) return "0"
        return replaceAll(value).toInt().toString()
    }

    @JvmStatic
    fun maskValue(value: String): String {
        val indexInitial = 0
        var valueFormat = replaceAll(value)
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        val decimalFormat = DecimalFormat("###,###,###.##", symbols)


        if (valueFormat.isBlank()) {
            return "0,00"
        }

        var sizeValue = valueFormat.length
        var dotBeforeValue = valueFormat.length - 2

        if(valueFormat.substring(0, dotBeforeValue).length > 2 && valueFormat.startsWith("0")){
            valueFormat = valueFormat.substring(1, valueFormat.length)
            sizeValue = valueFormat.length
            dotBeforeValue = valueFormat.length - 2
        }

        valueFormat = "${valueFormat.substring(indexInitial, dotBeforeValue)}.${
            valueFormat.substring(
                dotBeforeValue,
                sizeValue
            )
        }"
        val rounded = String.format("%.2f", valueFormat.toFloat())
        var valueFormatCompleted = decimalFormat.format(rounded.replace(",", ".").toFloat())


        return if(valueFormatCompleted.length > 6) {
            val indexComma = valueFormatCompleted.lastIndexOf(",")
            if(indexComma != -1 && valueFormatCompleted.substring(indexComma+1).length < 2){
                valueFormatCompleted += "0"
            }
            valueFormatCompleted
        } else {
            rounded.replace(".", ",")
        }

    }

    @JvmStatic
    fun maskKiloGram(value: String): String{
        var valueFormat = replaceAll(value)
        val indexInitial = 0
        if(valueFormat.isBlank()) return "0.000"

        valueFormat = valueFormat.padStart(4, '0')

        if(valueFormat.length > 4 && valueFormat.startsWith("0")){
            val removeQuantZero = valueFormat.length - 4
            valueFormat = valueFormat.substring(removeQuantZero, valueFormat.length)
        }

        val sizeValue = valueFormat.length
        val dotBeforeValue = valueFormat.length - 3

        valueFormat = "${valueFormat.substring(indexInitial, dotBeforeValue)}.${
            valueFormat.substring(
                dotBeforeValue,
                sizeValue
            )
        }"

        return valueFormat

    }
}
