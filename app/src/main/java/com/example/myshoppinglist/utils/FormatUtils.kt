package com.example.myshoppinglist.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class FormatUtils {
    private val patternReverseDate = "yyyy-MM-dd"
    private val patternNameDate = "EEEE"
    private val dayOfWeek = 3
    private val localeBr = Locale("pt", "BR")

    fun getDateFromatted(date: Date): String{
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return getDateFormatted(dayOfMonth, month, year, true)
    }

    fun getDateFormatted(currentDayOfMonth: Int? = null, currentMonth: Int? = null, currentYear: Int? = null, formatPtBR: Boolean? = false): String{
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val year = currentYear?:calendar.get(Calendar.YEAR)
        val month = currentMonth?:calendar.get(Calendar.MONTH)
        val dayOfMonth = currentDayOfMonth?:calendar.get(Calendar.DAY_OF_MONTH)

        return if(formatPtBR!!) {
            "${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/${if (month < 10) "0${month + 1}" else (month + 1)}/$year"
        }else{
            "$year-${if ((month + 1) < 10) "0${month + 1}" else (month + 1)}-${if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}"
        }
    }

    fun getDateString(date: Date? = Date()): String{
        val dateFormat1 = SimpleDateFormat(patternReverseDate);
        return dateFormat1.format(date)
    }

    fun getMonthAndYear(): String{
        val formatter = SimpleDateFormat("yyyy-MM", localeBr)
        return formatter.format(Date())
    }

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(localeBr).format(value)
    }

    fun getDate(valueDate: String): String{
        val yearCurrent = Date().year
        val date = SimpleDateFormat(patternReverseDate, localeBr).parse(valueDate)
        val pattern = if(date.year == yearCurrent) "dd MMM" else "dd MMM yyyy"
        return SimpleDateFormat(pattern, localeBr).format(date)
    }

    fun getMonth(valueMonth: String): String{
        val yearCurrent = Date().year
        val formatter = SimpleDateFormat(patternReverseDate, localeBr)
        val date = formatter.parse(valueMonth)
        val pattern = if(date.year == yearCurrent) "MMMM" else "MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, localeBr)
        return simpleDateFormat.format(date)
    }

    fun getMonthAndYearNumber(month: String): String{
        val splitMonthOfYear = month.split(" ")
        val yearCureent = SimpleDateFormat("yyyy").format(Date());
        val formatter = SimpleDateFormat("MMMM", localeBr)
        val date = formatter.parse(month)
        val month = if(date.month < 9) "0${(date.month + 1)}" else (date.month + 1)
        val year = if(splitMonthOfYear.size > 1) splitMonthOfYear.get(1) else yearCureent
        val formatred = "${year}-${month}"
        return formatred
    }

    fun getNameDay(dateFull: String): String {
        val formatter = SimpleDateFormat(patternReverseDate)
        val date = formatter.parse(dateFull)
        val dateCurrent = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(patternNameDate, localeBr)
        val dayCurrent = dateCurrent.date
        val yearCurrent = dateCurrent.year
        val monthCurrent = dateCurrent.month

        if((dayCurrent - date.date) < dayOfWeek && yearCurrent == date.year && monthCurrent == date.month){
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

    fun getNameMonth(month: String): String{
        val dateCurrent = Date("1990/$month/01")
        val formatter = SimpleDateFormat("MMMM", localeBr)
        val nameMonth = formatter.format(dateCurrent)
        return nameMonth
    }
}