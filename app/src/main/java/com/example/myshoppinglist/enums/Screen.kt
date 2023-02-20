package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class Screen(val id: Int) {

    Home(R.drawable.ic_home),
    CreateUser(R.drawable.default_avatar),
    CreateCards(R.drawable.default_avatar),
    RegisterPurchase(R.drawable.default_avatar),
    Spending(R.drawable.default_avatar),
    Finance(R.drawable.ic_financial),
    ProductsManager(R.drawable.ic_prototype),
    Categories(R.drawable.ic_outline_category_24),
    RegisterCategory(R.drawable.default_avatar),
    ListPurchase(R.drawable.default_avatar),
    MakingMarketScreen(R.drawable.default_avatar),
    SettingsScreen(R.drawable.default_avatar),
    ChoiceLogin(R.drawable.default_avatar)
}