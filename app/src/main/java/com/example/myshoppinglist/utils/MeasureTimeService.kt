package com.example.myshoppinglist.utils

import android.os.Handler
import android.os.Looper
import com.example.myshoppinglist.callback.Callback

object MeasureTimeService {

    @JvmStatic
    val messageWaitService = "Tentando comunicação com o servidor."

    @JvmStatic
    val messageNoService = "Não foi possivel conectar. \n\n Tente atualizar mais tarde!"

    @JvmStatic
    val messageNoConnection = "Sem conexão com internet. \n\n Tente atualizar mais tarde!"

    @JvmStatic
    val TIME_DELAY_CONNECTION = 7000L

    @JvmStatic
    val TIME_DELAY_DEFAULT = 600L

    @JvmStatic
    val TIME_DELAY_RESET = 600L

    private var handler: Handler? = null

    fun init() {
        handler = Handler(Looper.getMainLooper())
    }

    fun startMeasureTime(time: Long? = TIME_DELAY_DEFAULT, callback: Callback) {
        if (time == 0L) {
            callback.onChangeValue(true)
        } else {
            handler?.postDelayed({
                kotlin.run {
                    callback.onChangeValue(true)
                }
            }, time!!)
        }
    }

    fun resetMeasureTime(time: Long? = TIME_DELAY_RESET, callback: Callback) {
        handler?.postDelayed({
            kotlin.run {
                callback.onChangeValue(false)
            }
        }, time!!)
    }

    fun resetMeasureTimeErrorConnection(callback: Callback){
        callback.onChangeValue(messageNoConnection)
        cancelHandler()
        startMeasureTime(0L, callback)
    }

    fun cancelHandler() {
        handler?.removeCallbacksAndMessages(null)
    }

}