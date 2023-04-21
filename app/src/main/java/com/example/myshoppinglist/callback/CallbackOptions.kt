package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.Purchase

interface CallbackOptions: Callback{

    fun onEditable(idCardCurrent: Long){}

    fun onDelete(){}

    fun onTransfer(value: Boolean, purchase: Purchase, idCardCurrent: Long){}
}