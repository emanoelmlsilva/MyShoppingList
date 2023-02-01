package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.ItemList

class ItemListDTO() {

    var id: Long = 0
    var item: String = ""
    var isRemoved: Boolean = false
    var categoryOwnerIdItem: Long = 0
    var creditCardOwnerIdItem: Long = 0

    fun toItemListDTO(itemList: ItemList) {
        this.id = itemList.id
        this.item = itemList.item
        this.isRemoved = itemList.isRemoved
        this.categoryOwnerIdItem = itemList.categoryOwnerIdItem
        this.creditCardOwnerIdItem = itemList.creditCardOwnerIdItem
    }

    fun toItemList(): ItemList{
        val itemList = ItemList()
        itemList.id = this.id
        itemList.item = this.item
        itemList.isRemoved = this.isRemoved
        itemList.categoryOwnerIdItem = this.categoryOwnerIdItem
        itemList.creditCardOwnerIdItem = this.creditCardOwnerIdItem
        return itemList
    }

}