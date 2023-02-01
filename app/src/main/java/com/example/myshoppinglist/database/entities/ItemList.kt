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

    @ColumnInfo(name = "isRemoved")
    var isRemoved: Boolean = false

    constructor(): super(){
    }

    constructor(item: String, isRemoved: Boolean, categoryOwnerIdItem: Long, cartOwnerIdItem: Long) {
        this.item = item
        this.isRemoved = isRemoved
        this.categoryOwnerIdItem = categoryOwnerIdItem
        this.creditCardOwnerIdItem = cartOwnerIdItem
    }

    override fun toString(): String {
        return "ItemList(id=$id, item='$item', categoryOwnerIdItem=$categoryOwnerIdItem, creditCardOwnerIdItem=$creditCardOwnerIdItem, isRemoved=$isRemoved)"
    }


}