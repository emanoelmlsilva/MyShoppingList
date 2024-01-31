package com.example.myshoppinglist.utils

import android.util.Log

object MaskUtils {

    @JvmStatic
    fun convertValueStringToDouble(value: String): Double {
        val valorFormatted = value.replace(Regex("[^0-9.,]"), "")
        return try {
            valorFormatted.toDouble()
        } catch (numberFormatException: NumberFormatException) {
            valorFormatted.replace(".", "")
                .replace(",", ".").toDouble()
        }
    }

    @JvmStatic
    fun convertValueDoubleToString(value: Double): String {
        var valueFormated: String = value.toString().replace(",", ".")

        return "%.2f".format(valueFormated.toDouble())
    }

    @JvmStatic
    fun convertValueString(value: String): String {
        var valueFormated: String = value.replace(",", ".")

        return "%.2f".format(valueFormated.toDouble())
    }

    @JvmStatic
    fun replaceAll(value: String): String {
        val removeAllCharacterNotNumber = value.replace("[^0-9]".toRegex(), "");
        return removeAllCharacterNotNumber
    }

    @JvmStatic
    fun maskQuantity(value: String): String {
        if (value.isBlank()) return "0"
        return replaceAll(value).toInt().toString()
    }


    @JvmStatic
    fun maskValue(value: String): String {
        val cleanedValue = replaceAll(value).trimStart('0')
        val decimalSeparator = ","
        val thousandsSeparator = "."

        return when {
            cleanedValue.length <= 2 -> "0$decimalSeparator${cleanedValue.padStart(2, '0')}"
            cleanedValue.length <= 5 -> cleanedValue.substring(0, cleanedValue.length - 2) +
                    decimalSeparator + cleanedValue.takeLast(2)
            else -> {
                val integerPart = cleanedValue.substring(0, cleanedValue.length - 5)
                val thousandPart = cleanedValue.substring(
                    cleanedValue.length - 5,
                    cleanedValue.length - 2
                )
                val decimalPart = cleanedValue.takeLast(2)
                val formattedInteger =
                    integerPart.chunked(3).joinToString(thousandsSeparator) { it }
                "$formattedInteger$thousandsSeparator$thousandPart$decimalSeparator$decimalPart"
            }
        }
    }

    @JvmStatic
    fun maskKiloGram(value: String): String {

        val formattedValue = replaceAll(value).toDouble() / 1000
        return String.format("%.3f", formattedValue).replace(',', '.')

    }
}
