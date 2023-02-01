package com.example.myshoppinglist.callback

import com.example.myshoppinglist.enums.TypeProduct

interface CallbackMarketItem : Callback {

    fun onChangePrice(price: Double){}

    fun onChangeAmount(amount: String){}

    fun onChangeType(typeProduct: TypeProduct){}
}