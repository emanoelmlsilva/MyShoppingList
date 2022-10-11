package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class CardCreditFlag(val flag: Int) {
//adicionar imagens com fundo branco
    MASTER(R.drawable.master),
    AMEX(R.drawable.amex),
    APPLE(R.drawable.apple_pay),
    DINERS(R.drawable.diners_club),
    GOOGLE(R.drawable.google_pay),
    INTERAC(R.drawable.interac),
    PAY_PAL(R.drawable.pay_pal),
    STRIPE(R.drawable.stripe),
    VERIFONE(R.drawable.verifone),
    VISA(R.drawable.visa),
    MONEY(R.drawable.icon_add)

}