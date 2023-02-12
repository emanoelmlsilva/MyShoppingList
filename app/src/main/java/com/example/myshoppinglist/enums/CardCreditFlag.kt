package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class CardCreditFlag(val flag: Int, val flagBlack: Int) {

    ELO(R.drawable.elo, R.drawable.elo_black),
    HIPER(R.drawable.hiper, R.drawable.hiper_black),
    MASTER(R.drawable.master, R.drawable.master_black),
    AMEX(R.drawable.amex, R.drawable.amex_black),
    PAY_PAL(R.drawable.pay_pal, R.drawable.pay_pal_black),
    VISA(R.drawable.visa, R.drawable.visa_black),
    MONEY(R.drawable.icon_add, R.drawable.cash_black)

}