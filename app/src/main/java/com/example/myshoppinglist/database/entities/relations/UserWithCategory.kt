package com.example.myshoppinglist.database.entities.relations

import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

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
    val color: Int = 0
) {

    constructor() : this(User(), "", "", 0) {}

    fun toCategory(): Category {
        return Category(
            category,
            idImage,
            color
        )
    }

    override fun toString(): String {
        return "UserWithCategory(category='$category', idImage='$idImage', color=$color)"
    }


}