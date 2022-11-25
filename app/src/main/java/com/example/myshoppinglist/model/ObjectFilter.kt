package com.example.myshoppinglist.model
class ObjectFilter(var categoryCollection: MutableList<String> = mutableListOf(), var priceMin: Float = 0f, var priceMax: Float = 0f, var idCard: Long = 0L, var month: String = "", var text: String = ""){
}