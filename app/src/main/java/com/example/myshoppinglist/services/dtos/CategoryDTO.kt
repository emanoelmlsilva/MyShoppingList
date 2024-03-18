package com.example.myshoppinglist.services.dtos

import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.ui.theme.card_red_dark
import com.google.gson.annotations.SerializedName

class CategoryDTO(
    @SerializedName("id")
    var id: Long = 0L,
    var idCategory: Long = 0L,
    @SerializedName("category")
    var category: String = "",
    @SerializedName("idImage")
    var idImage: String = "fastfood.png",
    @SerializedName("color")
    var color: Int = card_red_dark.toArgb(),
    @SerializedName("user")
    var userDTO: UserDTO = UserDTO(),
    @SerializedName("dayClosedInvoice")
    var dayClosedInvoice: Int = 0
) {

    fun toCategoryApi(): Category{
        val category = toCategory()
        category.myShoppingId = this.id
        return category
    }

    fun toCategory(): Category{
        return Category(userDTO.email, id, idCategory, category, idImage, color, dayClosedInvoice)
    }
}