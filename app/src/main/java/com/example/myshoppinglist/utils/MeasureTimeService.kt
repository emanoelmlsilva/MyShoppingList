package com.example.myshoppinglist.utils

import com.example.myshoppinglist.callback.Callback

object MeasureTimeService {

    @JvmStatic
    val messageWaitService = "Tentando comunicação com o servidor"
    @JvmStatic
    val messageNoService = "Não foi possivel conectar. \n\n Tente atualizar mais tartde!"

    @JvmStatic
    fun startMeasureTime(callback: Callback) {
        android.os.Handler().postDelayed({
            kotlin.run {
                callback.onChangeValue(true)
            }
        }, 500)
    }

    fun resetMeasureTime(callback: Callback){
        android.os.Handler().postDelayed({
            kotlin.run {
                callback.onChangeValue(false)
            }
        }, 5000)
    }
}