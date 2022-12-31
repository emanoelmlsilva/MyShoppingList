package com.example.myshoppinglist.callback

import com.example.myshoppinglist.model.ObjectFilter

interface CallbackFilter : Callback {

    fun onChangeObjectFilter(value: ObjectFilter){}
}