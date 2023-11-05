package com.example.myshoppinglist.utils

import android.util.Log

object SeparateDateUtils {

    @JvmStatic
    fun separateMonthAndYear(dates: List<String>): HashMap<String, MutableList<String>> {
        val dateMonthAndYear: HashMap<String, MutableList<String>> = HashMap()

        dates.forEach { monthAndYear ->
            val separateDate = monthAndYear.split("-")
            val year = separateDate[0]
            val hasSelectedYear = dateMonthAndYear.keys.contains(year)

            if (!hasSelectedYear) {
                val filterYearCollection = dates.filter { it.contains(year) }
                val monthCollection = mutableListOf<String>()

                filterYearCollection.forEach() {
                    val month = it.split("-")[1]
                    if(!monthCollection.contains(month)){
                        monthCollection.add(month)
                    }
                }

                dateMonthAndYear[year] = monthCollection

            }
        }

        return dateMonthAndYear
    }
}