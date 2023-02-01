package com.example.myshoppinglist.database.dtos

import androidx.room.ColumnInfo
import com.example.myshoppinglist.database.entities.ItemList
import com.squareup.moshi.JsonClass
import org.jetbrains.annotations.NotNull

//@JsonClass(generateAdapter = true)
class ItemListDTO() {

    var id: Long = 0
    var item: String = ""
    var categoryOwnerIdItem: Long = 0
    var creditCardOwnerIdItem: Long = 0

    fun toItemListDTO(itemList: ItemList) {
        this.id = itemList.id
        this.item = itemList.item
        this.categoryOwnerIdItem = itemList.categoryOwnerIdItem
        this.creditCardOwnerIdItem = itemList.creditCardOwnerIdItem
    }

    fun toItemList(): ItemList{
        val itemList = ItemList()
        itemList.id = this.id
        itemList.item = this.item
        itemList.categoryOwnerIdItem = this.categoryOwnerIdItem
        itemList.creditCardOwnerIdItem = this.creditCardOwnerIdItem
        return itemList
    }

}