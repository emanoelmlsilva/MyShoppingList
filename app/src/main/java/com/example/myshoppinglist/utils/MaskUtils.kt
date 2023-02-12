package com.example.myshoppinglist.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object MaskUtils {

    @JvmStatic
    fun convertValueStringToDouble(value: String): Double{
        var valueFormated: String = value.replace(",", ".")

        if(valueFormated.count { it == '.' } > 1){
            val index = valueFormated.indexOf('.')
            valueFormated = valueFormated.removeRange(index,(index+1))
        }

        return valueFormated.toDouble()
    }

    @JvmStatic
    fun convertValueDoubleToString(value: Double): String{
        return "%.2f".format(value)
    }

    @JvmStatic
    fun replaceAll(value: String): String {
        //remove caracteres que não sejam numericos
        var removeAll = value.replace("[^0-9]".toRegex(), "");
        return removeAll
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

        if(value.endsWith(".") || value.endsWith(".")){
            return value.substring(3, value.length - 1)
        }

        if(valueFormat.length < 3){
            valueFormat = valueFormat.padStart(3, '0')
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
        var rounded = String.format("%.2f", valueFormat.toFloat())
        var valueFormatCompleted = decimalFormat.format(rounded.replace(",", ".").toFloat())


        return if(valueFormatCompleted.length > 6) {
            val indexComma = valueFormatCompleted.lastIndexOf(",")
            if(indexComma != -1 && valueFormatCompleted.substring(indexComma+1).length < 2){
                valueFormatCompleted += "0"
            }
            valueFormatCompleted
        } else {
            var indexComma = rounded.indexOf(',')

            if(indexComma == -1 && rounded.length > 7){
                indexComma = rounded.indexOf('.')
            }

            if(indexComma > -1 && rounded.substring(0, indexComma).length > 3){
                rounded = rounded.replace(".", ",")
                val indexCommaLast = valueFormatCompleted.lastIndexOf(".")
                var size = if(indexCommaLast == -1) 1 else rounded.substring(0, indexCommaLast).length
                val indexDotted = if(size > 3) size - 3 else size
                rounded = rounded.substring(0, indexDotted) + "." + rounded.substring(indexDotted)
                rounded
            }else{
                rounded.replace(".", ",")
            }
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
