package com.example.myshoppinglist.model

import com.example.myshoppinglist.enums.TypeCategory

class ObjectFilter(var categoryCollection: MutableList<TypeCategory> = mutableListOf(), var priceMin: Float? = null, var priceMax: Float? = null, var idCard: Long = 0L, var month: String = "", var text: String = "", var cardFilter: CardCreditFilter = CardCreditFilter()){
    override fun toString(): String {
        return "ObjectFilter(categoryCollection=${categoryCollection.joinToString()}, priceMin=$priceMin, priceMax=$priceMax, idCard=$idCard, month='$month', text='$text', cardFilter=$cardFilter)"
    }
}