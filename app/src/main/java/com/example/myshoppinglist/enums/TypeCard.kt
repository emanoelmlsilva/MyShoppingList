package com.example.myshoppinglist.enums

enum class TypeCard {

    MONEY, CREDIT;

    constructor()

    companion object {
        fun getTypeCard(idOriginal: Int) : TypeCard{
            return when (idOriginal){
                CREDIT.ordinal -> CREDIT
                else -> MONEY
            }
        }
    }
}