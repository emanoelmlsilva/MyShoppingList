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
    var idImage: Int = 0

    @ColumnInfo(name = "imageCircle")
    var imageCircle: Int = 0

    constructor(): super(){
    }

    constructor(category: String, idImage: Int, imageCircle: Int){
        this.category = category
        this.idImage = idImage
        this.imageCircle = imageCircle
    }

    override fun toString(): String {
        return "Category(category='$category', idImage=$idImage, imageCircle=$imageCircle)"
    }


}