package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class TypeCategory(val category: String, val idImage: Int, val imageCircle: Int) { 
        CLEARNING("Limpeza", R.drawable.clearning, R.drawable.clearning_circle), DRINKS("Bebidas", R.drawable.drink, R.drawable.drinks_circle), FOOD("Comidas", R.drawable.food, R.drawable.food_circle), HYGIENE("Higiene", R.drawable.higiene, R.drawable.higiene_circle), OTHERS("Outros", R.drawable.others, R.drawable.others_circle);

        fun toTypeCategory(category: String): TypeCategory{
                return valueOf(category)
        }
}