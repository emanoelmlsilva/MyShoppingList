package com.example.myshoppinglist.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.Purchase

data class PurchaseAndCategory(
    @Embedded val purchase: Purchase,
    @Relation(parentColumn = "categoryOwnerId", entityColumn = "myShoppingIdCategory") var category: Category
) {
}