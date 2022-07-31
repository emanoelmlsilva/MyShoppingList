package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.CreditCard

interface CallbackCreditCard : Callback{

    fun onChangeValueCreditCard(creditCard: CreditCard){

    }

    override fun onClick(){

    }
}