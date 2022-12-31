package com.example.myshoppinglist.callback

interface Callback {

    fun onClick(){}

    fun onChangeValue(idCard: Long){

    }

    fun onChangeValue(value: Int){

    }

    fun onChangeValue(newMonth: String){

    }
}