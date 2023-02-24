package com.example.myshoppinglist.model

import com.example.myshoppinglist.database.entities.Category

class ObjectFilter(var categoryCollection: MutableList<Category> = mutableListOf(), var priceMin: Float? = null, var priceMax: Float? = null, var idCard: Long = 0L, var month: String = "", var textCollection: MutableList<String> = mutableListOf(), var cardFilter: CardCreditFilter = CardCreditFilter()){
    override fun toString(): String {
        return "ObjectFilter(categoryCollection=${categoryCollection.joinToString()}, priceMin=$priceMin, priceMax=$priceMax, idCard=$idCard, month='$month', text='$textCollection', cardFilter=$cardFilter)"
    }
}