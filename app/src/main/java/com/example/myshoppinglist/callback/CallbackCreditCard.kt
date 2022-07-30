package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.CreditCard

interface CallbackCreditCard {

    fun onChangeValueCreditCard(creditCard: CreditCard){

    }
}