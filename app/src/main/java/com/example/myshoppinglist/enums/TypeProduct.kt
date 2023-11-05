package com.example.myshoppinglist.enums

enum class TypeProduct {

    KILO, QUANTITY;

    companion object {
        fun typeProductId(id: Int): TypeProduct {
            return when (id) {
                0 -> KILO
                1 -> QUANTITY
                // 'else' is not required because all cases are covered
                else -> KILO
            }
        }
    }
}