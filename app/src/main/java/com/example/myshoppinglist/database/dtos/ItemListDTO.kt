package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.ItemList
import com.google.gson.annotations.SerializedName

class ItemListDTO() {

    var myShoppingId: Long = 0
    var idMyShoppingApi: Long = 0
    @SerializedName("item")
    var item: String = ""
    @SerializedName("isRemoved")
    var isRemoved: Boolean = false
    @SerializedName("categoryOwnerIdItem")
    var categoryOwnerIdItem: Long = 0
    @SerializedName("creditCardOwnerIdItem")
    var creditCardOwnerIdItem: Long = 0
    @SerializedName("isSynchronized")
    var isSynchronized: Boolean = false

    fun toItemListDTO(itemList: ItemList) {
        this.idMyShoppingApi = itemList.idMyShoppingApi
        this.myShoppingId = itemList.myShoppingId
        this.item = itemList.item
        this.isRemoved = itemList.isRemoved
        this.categoryOwnerIdItem = itemList.categoryOwnerIdItem
        this.creditCardOwnerIdItem = itemList.creditCardOwnerIdItem
        this.isSynchronized = itemList.isSynchronized
    }

    fun toItemList(): ItemList{
        val itemList = ItemList()
        itemList.idMyShoppingApi = this.idMyShoppingApi
        itemList.myShoppingId = this.myShoppingId
        itemList.item = this.item
        itemList.isRemoved = this.isRemoved
        itemList.categoryOwnerIdItem = this.categoryOwnerIdItem
        itemList.creditCardOwnerIdItem = this.creditCardOwnerIdItem
        itemList.isSynchronized = this.isSynchronized
        return itemList
    }

}