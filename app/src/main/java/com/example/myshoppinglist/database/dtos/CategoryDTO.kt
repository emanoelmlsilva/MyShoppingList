package com.example.myshoppinglist.database.dtos

import androidx.room.ColumnInfo
import com.example.myshoppinglist.database.entities.Category
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
class CategoryDTO () {

    var id: Long = 0L

    var category: String = ""

    var idImage: String = ""

    var color: Int = 0

    fun toCategoryDTO(category: Category){
        this.id = category.id
        this.category = category.category
        this.idImage = category.idImage
        this.color = category.color
    }

    fun toCategory(): Category{
        val category = Category()
        category.id = this.id
        category.idImage = this.idImage
        category.color = this.color
        category.category = this.category

        return category
    }
}