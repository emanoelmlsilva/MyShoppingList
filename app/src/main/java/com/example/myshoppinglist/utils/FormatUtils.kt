package com.example.myshoppinglist.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class FormatUtils {

    private val patternDate = "dd-MM-yyyy"
    private val patternNameDate = "EEEE"
    private val dayOfWeek = 3

    fun getDateString(): String{
        val formatter = SimpleDateFormat(patternDate)
        return formatter.format(Date())
    }

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }

    fun getDate(valueDate: String): String{
        val yearCurrent = Date().year
        val date = SimpleDateFormat(patternDate).parse(valueDate)
        val pattern = if(date.year == yearCurrent) "dd MMM" else "dd MMM yyyy"
        return SimpleDateFormat(pattern).format(date)
    }

    fun getNameDay(dateFull: String): String {
        val formatter = SimpleDateFormat(patternDate)
        val date = formatter.parse(dateFull)
        val dateCurrent = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(patternNameDate, Locale("pt", "BR"))
        val dayCurrent = dateCurrent.date

        if((dayCurrent - date.date) < dayOfWeek){
            if(date.month == dateCurrent.month && date.year == dateCurrent.year){
                if(date.date == dayCurrent){
                    return "Hoje"
                }else if(date.date == (dayCurrent - 1)){
                    return "Ontem"
                }
            }

            return simpleDateFormat.format(date)
        }

        return getDate(dateFull)
    }
}