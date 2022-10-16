package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class Screen(val id: Int) {

    Home(R.drawable.ic_home),
    CreateUser(R.drawable.default_avatar),
    CreateCards(R.drawable.default_avatar),
    CreditCollection(R.drawable.ic_credit_card),
    RegisterPurchase(R.drawable.default_avatar),
    Spending(R.drawable.default_avatar),
    Finance(R.drawable.ic_financial),
    Products(R.drawable.ic_prototype)
}