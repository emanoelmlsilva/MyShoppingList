package com.example.myshoppinglist.callback

import android.util.Log
import com.example.myshoppinglist.enums.TypeProduct

interface CustomTextFieldOnClick : Callback{
    fun onChangeValueFloat(newValue: Float){}
    fun onChangeValue(newValue: String){}
    fun onChangeValueLong(newValue: Long){}
    override fun onClick(){

    }

    fun onChangeTypeProduct(newProduct: TypeProduct){
        Log.d("TESTE", "onChangeTypeProduct $newProduct")
    }
}