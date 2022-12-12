package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.model.CardCreditFilter

interface CallbackCreditCard : Callback{

    fun onChangeValueCreditCard(creditCard: CreditCard){

    }

    override fun onClick(){

    }

    fun onChangeFilterCreditCard(cardCreditFilter: CardCreditFilter)
}