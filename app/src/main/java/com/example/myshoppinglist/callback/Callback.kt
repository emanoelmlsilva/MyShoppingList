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

    fun onSucess(){}

    fun onCancel(){}
}