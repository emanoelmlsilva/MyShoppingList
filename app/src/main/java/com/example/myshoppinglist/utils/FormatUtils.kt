package com.example.myshoppinglist.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt


class FormatUtils {

    private val patternDate = "dd-MM-yyyy"
    private val patternNameDate = "EEEE"
    private val dayOfWeek = 3

    fun getDateString(): String{
        val formatter = SimpleDateFormat(patternDate)
        return formatter.format(Date())
    }

    fun getMonthAndYear(): String{
        val formatter = SimpleDateFormat("MM-yyyy")
        return formatter.format(Date())
    }

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }

    fun getDate(valueDate: String): String{
        val yearCurrent = Date().year
        val date = SimpleDateFormat(patternDate, Locale("pt", "BR")).parse(valueDate)
        val pattern = if(date.year == yearCurrent) "dd MMM" else "dd MMM yyyy"
        return SimpleDateFormat(pattern).format(date)
    }

    fun getMonth(valueMonth: String): String{
        val yearCurrent = Date().year
        val formatter = SimpleDateFormat(patternDate)
        val date = formatter.parse(valueMonth)
        val pattern = if(date.year == yearCurrent) "MMMM" else "MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale("pt", "BR"))
        return simpleDateFormat.format(date)
    }

    fun getMonthAndYearNumber(month: String): String{
        val splitMonthOfYear = month.split(" ")
        val yearCureent = SimpleDateFormat("yyyy").format(Date());
        val formatter = SimpleDateFormat("MMMM", Locale("pt", "BR"))
        val date = formatter.parse(month)
        val formatred = "${if(date.month < 10) "0${(date.month + 1)}" else (date.month + 1)}-${if(splitMonthOfYear.size > 1) splitMonthOfYear.get(1) else yearCureent}"
        return formatred
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