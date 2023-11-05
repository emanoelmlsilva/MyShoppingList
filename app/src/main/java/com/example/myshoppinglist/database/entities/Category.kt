package com.example.myshoppinglist.database.entities

import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.ui.theme.card_red_dark
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "category")
class Category{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "myShoppingIdCategory")
    var myShoppingId: Long = 0L

    @SerializedName("idMyShoppingApi")
    @ColumnInfo(name = "idMyShoppingApi")
    var idMyShoppingApi: Long = 0

    @ColumnInfo(name = "category")
    var category: String = ""

    @ColumnInfo(name = "idImage")
    var idImage: String = "fastfood.png"

    @ColumnInfo(name = "color")
    var color: Int = card_red_dark.toArgb()

    @ColumnInfo(name = "categoryUserId")
    var categoryUserId: String = ""

    @ColumnInfo(name = "isSynchronized")
    var isSynchronized: Boolean = false

    constructor(): super(){
    }

    constructor(category: String, idImage: String){
        this.category = category
        this.idImage = idImage
    }

    constructor(category: String, idImage: String, color: Int):this(category, idImage){
        this.color = color
    }

    constructor(emailUser: String, category: String, idImage: String, color: Int):this(category, idImage, color){
        this.categoryUserId = emailUser
    }

    constructor(emailUser: String, idApi: Long, category: String, idImage: String, color: Int):this(category, idImage, color){
        this.categoryUserId = emailUser
        this.idMyShoppingApi = idApi
    }

    constructor(emailUser: String, idMyShoppingApi: Long, id: Long, category: String, idImage: String, color: Int):this(emailUser, idMyShoppingApi, category, idImage, color){
        this.myShoppingId = id
    }

    constructor(emailUser: String, idMyShoppingApi: Long, id: Long, category: String, idImage: String, color: Int, isSynchronized: Boolean):this(emailUser, idMyShoppingApi, id, category, idImage, color){
        this.isSynchronized = isSynchronized
    }

    override fun toString(): String {
        return "Category(myShoppingId=$myShoppingId, idMyShoppingApi=$idMyShoppingApi, category='$category', idImage='$idImage', color=$color, categoryUserId='$categoryUserId')"
    }


}