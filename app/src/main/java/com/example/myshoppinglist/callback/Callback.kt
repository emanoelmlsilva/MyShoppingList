package com.example.myshoppinglist.callback

import com.example.myshoppinglist.enums.StatusSaveData

interface Callback {

    fun onClick(){}

    fun onChangeValue(value: Long){

    }

    fun onChangeValue(newValue: Boolean){

    }

    fun onChangeValue(value: Int){

    }

    fun onChangeValue(newValue: String){

    }

    fun onChangeValue(newValue: String, time: Long){

    }

    fun onSuccess(){}

    fun onFailed(messageError: String){}

    fun onCancel(){}

    fun onChangeStatus(status: StatusSaveData) {}

}