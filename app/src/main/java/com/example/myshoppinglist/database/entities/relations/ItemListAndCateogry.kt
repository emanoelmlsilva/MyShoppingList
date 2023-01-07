package com.example.myshoppinglist.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList

class ItemListAndCateogry(@Embedded val itemList: ItemList, @Relation(parentColumn = "categoryOwnerIdItem", entityColumn = "id") var category: Category) {
}