package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class Screen(val drawable: Int, val id: Int) {

    Home(R.drawable.ic_home, 0),
    CreateUser(R.drawable.default_avatar, 1),
    CreateCards(R.drawable.default_avatar, 2),
    RegisterPurchase(R.drawable.default_avatar, 3),
    Spending(R.drawable.default_avatar, 4),
    Finance(R.drawable.ic_financial, 5),
    ProductsManager(R.drawable.ic_prototype, 6),
    Categories(R.drawable.ic_outline_category_24, 7),
    RegisterCategory(R.drawable.default_avatar, 8),
    MakingMarketScreen(R.drawable.default_avatar, 9),
    ListPurchase(R.drawable.default_avatar, 10),
    SettingsScreen(R.drawable.default_avatar, 11),
    ChoiceLogin(R.drawable.default_avatar, 12),
    Login(R.drawable.default_avatar, 13),
    Register(R.drawable.default_avatar, 14);

    companion object {

        fun enableScreenBottomBarState(name: String): Boolean {
            return name == Home.name || name == ProductsManager.name || name == Categories.name
        }
    }
    }