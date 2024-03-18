package com.example.myshoppinglist.database.entities.relations

import androidx.room.ColumnInfo
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.User
import com.google.gson.annotations.SerializedName

class UserWithCategory(
    @SerializedName("user")
    val user: User,
    @SerializedName("idMyShoppingApi")
    @ColumnInfo(name = "idMyShoppingApi")
    var idMyShoppingApi: Long = 0L,
    @SerializedName("category")
    @ColumnInfo(name = "category")
    var category: String = "",
    @SerializedName("idImage")
    @ColumnInfo(name = "idImage")
    val idImage: String = "",
    @SerializedName("color")
    @ColumnInfo(name = "color")
    val color: Int = 0,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Long = 0L,
    @SerializedName("idCategory")
    @ColumnInfo(name = "idCategory")
    var idCategory: Long = 0L,
    @SerializedName("dayClosedInvoice")
    @ColumnInfo(name = "dayClosedInvoice")
    var dayClosedInvoice: Int = 0
) {

    constructor() : this(User(), category = "", idImage = "", color = 0) {}

    constructor(user: User, idMyShoppingApi: Long, category: String, idImage: String, color: Int, dayClosedInvoice: Int) : this(user = user, category = category, idImage =  idImage, color = color) {
        this.idMyShoppingApi = idMyShoppingApi
        this.dayClosedInvoice = dayClosedInvoice
    }

    constructor(user: User, idCategory: Long, idMyShoppingApi: Long, category: String, idImage: String, color: Int, dayClosedInvoice: Int) : this(
        user,
        idMyShoppingApi,
        category,
        idImage,
        color
    ) {
        this.idCategory = idCategory
        this.dayClosedInvoice = dayClosedInvoice
    }

    fun toCategory(): Category {
        return Category(
            user.email,
            idMyShoppingApi,
            category,
            idImage,
            color,
            dayClosedInvoice
        )
    }

    fun toCategoryId(): Category {
        return Category(
            user.email,
            idMyShoppingApi,
            idCategory,
            category,
            idImage,
            color,
            dayClosedInvoice
        )
    }

    override fun toString(): String {
        return "UserWithCategory(category='$category', idImage='$idImage', color=$color)"
    }


}