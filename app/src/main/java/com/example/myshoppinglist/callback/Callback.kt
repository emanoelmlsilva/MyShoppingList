package com.example.myshoppinglist.callback

interface Callback {

    fun onClick(){}

    fun onChangeIdCard(idCard: Long){

    }

    fun onChangeValueId(value: Int){

    }

    fun onChangeValueMong(newMonth: String){

    }
}