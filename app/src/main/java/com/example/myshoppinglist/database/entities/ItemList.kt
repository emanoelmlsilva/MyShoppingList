package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import org.jetbrains.annotations.NotNull

@JsonClass(generateAdapter = true)
@Entity(tableName = "itemLists")
class ItemList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "myShoppingId")
    var myShoppingId: Long = 0

    @SerializedName("idMyShoppingApi")
    @ColumnInfo(name = "idMyShoppingApi")
    var idMyShoppingApi: Long = 0

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

    constructor(item: String, isRemoved: Boolean, categoryOwnerIdItem: Long, cardOwnerIdItem: Long) {
        this.item = item
        this.isRemoved = isRemoved
        this.categoryOwnerIdItem = categoryOwnerIdItem
        this.creditCardOwnerIdItem = cardOwnerIdItem
    }

    constructor(myShoppingId: Long, item: String, isRemoved: Boolean, categoryOwnerIdItem: Long, cardOwnerIdItem: Long) : this(item, isRemoved, categoryOwnerIdItem, cardOwnerIdItem) {
        this.myShoppingId = myShoppingId
    }

    constructor(
        idMyShoppingApi: Long,
        myShoppingId: Long,
        item: String,
        isRemoved: Boolean,
        categoryOwnerIdItem: Long,
        creditCardOwnerIdItem: Long
    ):this(myShoppingId, item, isRemoved, categoryOwnerIdItem, creditCardOwnerIdItem) {
        this.idMyShoppingApi = idMyShoppingApi
    }

    override fun toString(): String {
        return "ItemList(myShoppingId=$myShoppingId, item='$item', categoryOwnerIdItem=$categoryOwnerIdItem, creditCardOwnerIdItem=$creditCardOwnerIdItem, isRemoved=$isRemoved)"
    }


}