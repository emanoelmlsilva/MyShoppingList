package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.jetbrains.annotations.NotNull

@JsonClass(generateAdapter = true)
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

    @ColumnInfo(name = "creditCardOwnerIdItem")
    var creditCardOwnerIdItem: Long = 0

    constructor(): super(){
    }

    constructor(item: String, categoryOwnerIdItem: Long, cartOwnerIdItem: Long) {
        this.item = item
        this.categoryOwnerIdItem = categoryOwnerIdItem
        this.creditCardOwnerIdItem = cartOwnerIdItem
    }

    override fun toString(): String {
        return "ItemList(item='$item', categoryOwnerIdItem=$categoryOwnerIdItem, cartOwnerIdItem=$creditCardOwnerIdItem)"
    }


}