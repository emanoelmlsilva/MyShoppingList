package com.example.myshoppinglist.callback

import com.example.myshoppinglist.enums.TypeState


abstract class CallbackPurchase : Callback{
    override fun onClick() {
        TODO("Not yet implemented")
    }

    abstract fun onChangeIndex(index: Int, typeState: TypeState)
}