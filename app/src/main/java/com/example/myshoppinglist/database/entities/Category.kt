package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "category")
class Category{

    @PrimaryKey()
    @NotNull
    @ColumnInfo(name = "category")
    var category: String = ""

    @ColumnInfo(name = "idImage")
    var idImage: String = ""

    @ColumnInfo(name = "color")
    var color: Int = 0

    constructor(): super(){
    }

    constructor(category: String, idImage: String){
        this.category = category
        this.idImage = idImage
    }

    constructor(category: String, idImage: String, color: Int):this(category, idImage){
        this.color = color
    }

    override fun toString(): String {
        return "Category(category='$category', idImage=$idImage"
    }


}