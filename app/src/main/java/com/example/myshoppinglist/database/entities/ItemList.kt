package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "itemLists")
class ItemList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idItem")
    var id: Long = 0

    @ColumnInfo(name = "item")
    @NotNull
    var item: String = ""

    @ColumnInfo(name = "categoryOwnerIdItem")
    var categoryOwnerIdItem: Long = 0

    constructor(): super(){
    }

    constructor(item: String, categoryOwnerIdItem: Long) {
        this.item = item
        this.categoryOwnerIdItem = categoryOwnerIdItem
    }

    override fun toString(): String {
        return "ItemList(id=$id, item='$item', categoryOwnerIdItem=$categoryOwnerIdItem)"
    }


}