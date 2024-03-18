package com.example.myshoppinglist.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FormatDateUtils {
    private val patternReverseDate = "yyyy-MM-dd"
    private val patternNameDate = "EEEE"
    private val dayOfWeek = 3
    private val localeBr = Locale("pt", "BR")

    fun getDateFormatted(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return getDateFormatted(dayOfMonth, month, year, true)
    }

    fun getDateFormatted(
        currentDayOfMonth: Int? = null,
        currentMonth: Int? = null,
        currentYear: Int? = null,
        formatPtBR: Boolean? = false
    ): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val year = currentYear ?: calendar.get(Calendar.YEAR)
        val month = currentMonth ?: calendar.get(Calendar.MONTH)
        val dayOfMonth = currentDayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

        return if (formatPtBR!!) {
            "${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/${if (month < 10) "0${month + 1}" else (month + 1)}/$year"
        } else {
            "$year-${if ((month + 1) < 10) "0${month + 1}" else (month + 1)}-${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}"
        }
    }

    fun getDateString(date: Date? = Date()): String {
        val dateFormat1 = SimpleDateFormat(patternReverseDate);
        return dateFormat1.format(date)
    }

    fun getMonthAndYear(): String {
        val formatter = SimpleDateFormat("yyyy-MM", localeBr)
        return formatter.format(Date())
    }

    fun getCurrentMonth(): Int{
        val formatter = SimpleDateFormat("MM", localeBr)
        return formatter.format(Date()).toInt()
    }

    fun getCurrentYear(): Int{
        val formatter = SimpleDateFormat("yyyy", localeBr)
        return formatter.format(Date()).toInt()
    }

    fun getCurrentDay(): Int{
        val formatter = SimpleDateFormat("dd", localeBr)
        return formatter.format(Date()).toInt()
    }

    fun getDateDayClosedInvoiceCurrent(dayClosedInvoice: Int): String{
        val monthCurrent = getCurrentMonth()
        val yearCurrent = getCurrentYear()

        return "$yearCurrent-${if (monthCurrent < 10) "0$monthCurrent" else monthCurrent}-${if (dayClosedInvoice < 10) "0$dayClosedInvoice" else dayClosedInvoice}"
    }

    fun getYearAndMonthCurrent(): String{
        val monthCurrent = getCurrentMonth()
        val yearCurrent = getCurrentYear()

        return "$yearCurrent-${if (monthCurrent < 10) "0$monthCurrent" else monthCurrent}-"
    }

    fun getDateLastMonth(dayClosedInvoice: Int): String{
        var lastDayClosedInvoice = dayClosedInvoice - 1

        var monthLast = getCurrentMonth()
        var yearLast = getCurrentYear()

        if(dayClosedInvoice < 0){
            lastDayClosedInvoice = 31
            yearLast = if(monthLast == 1) yearLast - 1 else yearLast
            monthLast = if(monthLast == 1) 12 else monthLast - 1
        } else {
            monthLast = if(monthLast + 1 > 12) 1 else monthLast + 1
            yearLast = if(monthLast + 1 > 12) yearLast + 1 else yearLast
        }

        return "$yearLast-${if (monthLast < 10) "0$monthLast" else monthLast}-${if (lastDayClosedInvoice < 10) "0$lastDayClosedInvoice" else lastDayClosedInvoice}"
    }

    fun getNextMonthAndYear(month: Int, year: Int): String{

        var monthLast = month
        var yearLast = year

        monthLast = if(monthLast + 1 > 12) 1 else monthLast + 1
        yearLast = if(monthLast + 1 > 12) yearLast + 1 else yearLast

        return "$yearLast-${if (monthLast < 10) "0$monthLast" else monthLast}-"
    }

    fun getNextMonthAndYear(): String{

        val monthLast = getCurrentMonth()
        val yearLast = getCurrentYear()

        return getNextMonthAndYear(monthLast, yearLast)
    }

    fun getFormatValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(localeBr).format(value)
    }

    fun getDate(valueDate: String): String {
        val yearCurrent = Date().year
        val date = SimpleDateFormat(patternReverseDate, localeBr).parse(valueDate)
        val pattern = if (date.year == yearCurrent) "dd MMM" else "dd MMM yyyy"
        return SimpleDateFormat(pattern, localeBr).format(date)
    }

    fun getMonth(valueMonth: String): String {
        try {
            val yearCurrent = Date().year
            val formatter = SimpleDateFormat(patternReverseDate, localeBr)
            val date = formatter.parse(valueMonth)
            val pattern = if (date.year == yearCurrent) "MMMM" else "MMMM yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern, localeBr)
            return simpleDateFormat.format(date)
        } catch (parseException: ParseException) {
            return ""
        }

    }

    fun getMonthAndYearNumber(month: String): String {
        val splitMonthOfYear = month.split(" ")
        val yearCureent = SimpleDateFormat("yyyy").format(Date());
        val formatter = SimpleDateFormat("MMMM", localeBr)
        val date = formatter.parse(month)
        val month = if (date.month < 9) "0${(date.month + 1)}" else (date.month + 1)
        val year = if (splitMonthOfYear.size > 1) splitMonthOfYear.get(1) else yearCureent
        val formatred = "${year}-${month}"
        return formatred
    }

    fun getNameDay(dateFull: String, enabled: Boolean = true): String {
        val formatter = SimpleDateFormat(patternReverseDate)
        val date = formatter.parse(dateFull)
        val dateCurrent = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(patternNameDate, localeBr)
        val dayCurrent = dateCurrent.date
        val yearCurrent = dateCurrent.year
        val monthCurrent = dateCurrent.month

        if (enabled && (dayCurrent - date.date) < dayOfWeek && yearCurrent == date.year && monthCurrent == date.month) {
            if (date.month == dateCurrent.month && date.year == dateCurrent.year) {
                if (date.date == dayCurrent) {
                    return "Hoje"
                } else if (date.date == (dayCurrent - 1)) {
                    return "Ontem"
                }
            }

            return simpleDateFormat.format(date)
        }

        return getDate(dateFull)
    }

    fun getNameMonth(month: String): String {
        val dateCurrent = Date("1990/$month/01")
        val formatter = SimpleDateFormat("MMMM", localeBr)
        val nameMonth = formatter.format(dateCurrent)
        return nameMonth
    }

    fun getMonthCurrent(): String {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("MMMM", Locale("pt", "BR"))
        return format.format(cal.time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getYearCurrent(): String {
        val cal = Calendar.getInstance()
//        val format = SimpleDateFormat("yyyy", Locale("pt", "BR"))
        return cal.weekYear.toString()
    }

    fun getNumberMonth(month: String): Int? {
        val meses = mapOf(
            "janeiro" to 1,
            "fevereiro" to 2,
            "março" to 3,
            "abril" to 4,
            "maio" to 5,
            "junho" to 6,
            "julho" to 7,
            "agosto" to 8,
            "setembro" to 9,
            "outubro" to 10,
            "novembro" to 11,
            "dezembro" to 12
        )

        return meses[month.toLowerCase()]
    }
}