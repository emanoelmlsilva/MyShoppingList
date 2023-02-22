package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.jetbrains.annotations.NotNull

@JsonClass(generateAdapter = true)
@Entity(tableName = "category")
class Category{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L

    @ColumnInfo(name = "category")
    var category: String = ""

    @ColumnInfo(name = "idImage")
    var idImage: String = ""

    @ColumnInfo(name = "color")
    var color: Int = 0

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

    override fun toString(): String {
        return "Category(id=$id, category='$category', idImage='$idImage', color=$color)"
    }


}