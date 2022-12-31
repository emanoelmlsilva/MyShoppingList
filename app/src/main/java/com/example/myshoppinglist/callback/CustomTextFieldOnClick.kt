package com.example.myshoppinglist.callback

import android.util.Log
import com.example.myshoppinglist.enums.TypeProduct

interface CustomTextFieldOnClick : Callback{
    fun onChangeValueFloat(newValue: Float){}
    fun onChangeValueLong(newValue: Long){}

    fun onChangeTypeProduct(newProduct: TypeProduct){}
}