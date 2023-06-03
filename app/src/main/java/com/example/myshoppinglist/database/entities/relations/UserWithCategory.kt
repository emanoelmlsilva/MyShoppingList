package com.example.myshoppinglist.database.entities.relations

import androidx.room.ColumnInfo
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.User
import com.google.gson.annotations.SerializedName

class UserWithCategory(
    @SerializedName("user")
    val user: User,
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
    var idCategory: Long = 0L
) {

    constructor() : this(User(), "", "", 0) {}

    constructor(user: User, id: Long, category: String, idImage: String, color: Int) : this(
        user,
        category,
        idImage,
        color
    ) {
        this.id = id
    }

    constructor(user: User, idCategory: Long, id: Long, category: String, idImage: String, color: Int) : this(
        user,
        id,
        category,
        idImage,
        color
    ) {
        this.idCategory = idCategory
    }

    fun toCategory(): Category {
        return Category(
            user.email,
            id,
            category,
            idImage,
            color
        )
    }

    fun toCategoryId(): Category {
        return Category(
            user.email,
            id,
            idCategory,
            category,
            idImage,
            color
        )
    }

    override fun toString(): String {
        return "UserWithCategory(category='$category', idImage='$idImage', color=$color)"
    }


}