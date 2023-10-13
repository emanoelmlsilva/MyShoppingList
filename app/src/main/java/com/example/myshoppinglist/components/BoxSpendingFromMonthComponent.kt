package com.example.myshoppinglist.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.text_primary_light
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils


@Composable
fun BoxSpendingFromMonthComponent(
    lifecycleOwner: LifecycleOwner,
    purchaseController: PurchaseController,
    price: Double,
    currentCreditCard: CreditCard?,
    creditCards: List<CreditCard>,
    callbackCreditCard: CallbackCreditCard
) {
    val monthList = remember { mutableStateListOf<String>() }

    var monthCurrent by remember { mutableStateOf("") }

    @RequiresApi(Build.VERSION_CODES.N)
    fun monthIdCard(idCard: Long) {
        purchaseController.getMonthByIdCardDB(idCard).observe(lifecycleOwner) { months ->
            monthList.removeAll(monthList)

            try{
                val convertedMonth = months.groupBy {
                    val separaterDate = it.split("-")
                    "${separaterDate[0]}-${separaterDate.get(1)}"
                }.map { group -> FormatUtils().getMonth("${group.key}-01") }

                monthList.addAll(convertedMonth)

                val monthCurrentDefault = FormatUtils().getMonthCurrent()

                val isNotExist =
                    months.find { FormatUtils().getMonth("${it}-01") == monthCurrentDefault } == null

                if (isNotExist && (monthCurrent.isBlank() || FormatUtils().getNumberMonth(
                        monthCurrentDefault
                    )!!.toInt() > FormatUtils().getNumberMonth(monthCurrent)!!.toInt())
                ) {
                    monthCurrent = FormatUtils().getNumberMonth(monthCurrentDefault).toString()
                    val yearCurrent = FormatUtils().getYearCurrent()

                    val formatMonth = if (monthCurrent.toInt() > 9) monthCurrent else "0$monthCurrent"

                    monthCurrent = "$yearCurrent-$formatMonth-01"

                    monthList.add(0, monthCurrentDefault)

                } else if (convertedMonth.isNotEmpty()) {
                    val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(convertedMonth[0])

                    monthCurrent = "$monthAndYearNumber-01"
                }

                val convertMonthNumberCurrent = FormatUtils().getMonth(monthCurrent)

                if (convertMonthNumberCurrent.isNotBlank()) {
                    callbackCreditCard.onChangeDataMonth(convertMonthNumberCurrent)
                }

            }catch (e: NullPointerException){

                monthCurrent = "${FormatUtils().getMonthAndYearNumber(FormatUtils().getMonthCurrent())}-01"

                e.printStackTrace()
            }

        }

    }

    LaunchedEffect(key1 = currentCreditCard) {
        if (currentCreditCard != null) monthIdCard(currentCreditCard.myShoppingId)
    }

    LaunchedEffect(key1 = monthCurrent) {

        if (monthCurrent.isNotBlank()) {
            callbackCreditCard.onChangeValue(FormatUtils().getMonth(monthCurrent))
        }

    }

    Column(verticalArrangement = Arrangement.Center) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "Gastos do mês",
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 16.sp,
                fontFamily = LatoBlack,
                color = text_primary_light
            )
            CustomDropDownMonthComponent(
                object : CustomTextFieldOnClick {
                    override fun onChangeValue(newValue: String) {
                        monthCurrent = "${FormatUtils().getMonthAndYearNumber(newValue)}-01"
                    }
                }, monthList, FormatUtils().getMonth(monthCurrent)
            )

        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(price))}",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            if (currentCreditCard != null) BoxDropdownCardCredit(
                creditCards,
                currentCreditCard,
                callbackCreditCard
            )
        }
    }
}