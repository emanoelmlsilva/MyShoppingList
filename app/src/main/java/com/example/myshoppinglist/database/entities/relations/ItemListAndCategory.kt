package com.example.myshoppinglist.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ItemListAndCategory(@Embedded val itemList: ItemList, @Relation(parentColumn = "categoryOwnerIdItem", entityColumn = "myShoppingIdCategory") var category: Category) {

}