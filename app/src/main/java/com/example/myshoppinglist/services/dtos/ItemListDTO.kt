package com.example.myshoppinglist.services.dtos

import android.os.Parcelable
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.ItemList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemListDTO() : Parcelable {


    @SerializedName("myShoppingId")
    var myShoppingId: Long = 0
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("item")
    var item: String = ""
    @SerializedName("isRemoved")
    var isRemoved: Boolean = false
    @SerializedName("creditCard")
    var creditCardDTO: CreditCardDTO = CreditCardDTO()
    @SerializedName("category")
    var categoryDTO: CategoryDTO = CategoryDTO()

    constructor(itemList: ItemList, category: Category) : this(){
        val categoryDTO = CategoryDTO()
        categoryDTO.toCategoryDTO(category)

        this.myShoppingId = itemList.myShoppingId
        this.id = itemList.idMyShoppingApi
        this.item = itemList.item
        this.isRemoved = itemList.isRemoved
        this.categoryDTO = categoryDTO
    }

    fun toItemListApi() : ItemList {
        val itemList = toItemList()
        itemList.categoryOwnerIdItem = this.categoryDTO.id
        itemList.creditCardOwnerIdItem = this.creditCardDTO.id

        itemList.myShoppingId = this.id
        return itemList
    }

    fun toItemList() : ItemList {
        return ItemList(id, myShoppingId, item, isRemoved, categoryDTO.myShoppingId, creditCardDTO.idCard)
    }

    override fun toString(): String {
        return "ItemListDTO(myShoppingId=$myShoppingId, item='$item', isRemoved=$isRemoved, creditCardDTO=$creditCardDTO, categoryDTO=$categoryDTO)"
    }


}