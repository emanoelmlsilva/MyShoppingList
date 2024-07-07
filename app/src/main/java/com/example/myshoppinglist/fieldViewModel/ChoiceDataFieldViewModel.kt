package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.SeparateDateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChoiceDataFieldViewModel(context: Context, val lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)

    val yearCurrent = MutableStateFlow("")
    val monthCurrent = MutableStateFlow("")
    val dateMonthAndYear = MutableStateFlow(emptyMap<String, MutableList<String>>())
    val monthCollection = MutableStateFlow(emptyList<String>())

    fun updateMonthCollection() {
        monthCollection.value = emptyList()
        if (yearCurrent.value.isNotBlank() && dateMonthAndYear.value.isNotEmpty()) {
            val monthOfYearCollection = if (dateMonthAndYear.value.size > 1) dateMonthAndYear.value[yearCurrent.value]!!.toList() else dateMonthAndYear.value.values.first().map { it.trim() }
            monthCollection.value = (monthOfYearCollection.ifEmpty { listOf() })
        } else {
            monthCollection.value = emptyList()
        }
    }

    fun updateYear(newValue: String){
        yearCurrent.value = newValue
    }

    fun updateMonth(newMonth: String){
        monthCurrent.value = newMonth
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateMonths(idCard: Long){
        viewModelScope.launch {
            dateMonthAndYear.value = emptyMap()

            if(idCard == -1L){
                val mothsAndYearCollection = SeparateDateUtils.separateMonthAndYear(FormatDateUtils().getMonthYearAllAtCurrent())
                delay(100L)
                updateYear(FormatDateUtils().getYearCurrent())
                dateMonthAndYear.value = mothsAndYearCollection
            }else {

                purchaseController.getMonthByIdCardDB(idCard).observe(lifecycleOwner) { dates ->
                    val mothsAndYearCollection = SeparateDateUtils.separateMonthAndYear(dates)
                    dateMonthAndYear.value = mothsAndYearCollection
                    updateYear(FormatDateUtils().getYearCurrent())
                }
            }
        }
    }
}