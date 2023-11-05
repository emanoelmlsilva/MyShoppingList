package com.example.myshoppinglist.callback

interface Callback {

    fun onClick(){}

    fun onChangeValue(idCard: Long){

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
}