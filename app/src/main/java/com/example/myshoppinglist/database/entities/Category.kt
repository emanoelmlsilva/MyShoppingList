package com.example.myshoppinglist.database.entities

import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.myshoppinglist.ui.theme.card_red_dark
import com.squareup.moshi.JsonClass
import org.jetbrains.annotations.NotNull

@JsonClass(generateAdapter = true)
@Entity(tableName = "category")
class Category{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L

    @ColumnInfo(name = "idApi")
    var idApi: Long = 0L

    @ColumnInfo(name = "category")
    var category: String = ""

    @ColumnInfo(name = "idImage")
    var idImage: String = "fastfood.png"

    @ColumnInfo(name = "color")
    var color: Int = card_red_dark.toArgb()

    @ColumnInfo(name = "categoryUserId")
    var categoryUserId: String = ""


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
        this.idApi = idApi
    }

    constructor(emailUser: String, idApi: Long, id: Long, category: String, idImage: String, color: Int):this(emailUser, idApi, category, idImage, color){
        this.id = id
    }

    override fun toString(): String {
        return "Category(id=$id, idApi=$idApi, category='$category', idImage='$idImage', color=$color, categoryUserId='$categoryUserId')"
    }


}