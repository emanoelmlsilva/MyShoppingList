package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.Category
import com.google.gson.annotations.SerializedName

class CategoryDTO () {

    var myShoppingId: Long = 0L
    @SerializedName("id")
    var id: Long = 0L
    @SerializedName("category")
    var category: String = ""
    @SerializedName("idImage")
    var idImage: String = ""
    @SerializedName("color")
    var color: Int = 0
    @SerializedName("user")
    var userDTO: UserDTO = UserDTO()

    fun toCategoryApi(): Category{
        val category = toCategory()
        category.myShoppingId = this.id
        return category
    }

    fun toCategoryDTO(category: Category){
        this.myShoppingId = category.myShoppingId
        this.id = category.idMyShoppingApi
        this.category = category.category
        this.idImage = category.idImage
        this.color = category.color
    }

    fun toCategory(): Category{
        val category = Category()
        category.myShoppingId = this.myShoppingId
        category.idMyShoppingApi = this.id
        category.idImage = this.idImage
        category.color = this.color
        category.category = this.category

        return category
    }

    override fun toString(): String {
        return "CategoryDTO(myShoppingId=$myShoppingId, id=$id, category='$category', idImage='$idImage', color=$color, userDTO=$userDTO)"
    }


}