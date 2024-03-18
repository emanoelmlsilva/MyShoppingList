package com.example.myshoppinglist.database.entities.relations

import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

class UserWithCreditCard(
    @SerializedName("user")
    val user: User,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @SerializedName("idMyShoppingApi")
    @ColumnInfo(name = "idMyShoppingApi")
    var idMyShoppingApi: Long = 0,
    @SerializedName("holderName")
    @ColumnInfo(name = "holderName")
    val holderName: String = "",
    @SerializedName("cardName")
    @ColumnInfo(name = "cardName")
    val cardName: String = "",
    @SerializedName("value")
    @ColumnInfo(name = "value")
    val value: Float = 0F,
    @SerializedName("colorCard")
    @ColumnInfo(name = "colorCard")
    val colorCard: Int = card_blue.toArgb(),
    @SerializedName("typeCard")
    @ColumnInfo(name = "typeCard")
    val typeCard: Int = 0,
    @SerializedName("flagBlack")
    @ColumnInfo(name = "flagBlack")
    val flag: Int = CardCreditFlag.MASTER.flagBlack,
    @SerializedName("dayClosedInvoice")
    @ColumnInfo(name = "dayClosedInvoice")
    val dayClosedInvoice: Int = 0,
    @SerializedName("position")
    @ColumnInfo(name = "position", defaultValue = 0.toString())
    val position: Int = 0
) {

    constructor() : this(User(), 0, 0, "", "", 0F, 0, 0, 0, 0) {}

    fun toCreditCard(): CreditCard {
        return CreditCard(
            id,
            idMyShoppingApi,
            holderName,
            cardName,
            value,
            colorCard,
            TypeCard.getTypeCard(typeCard),
            user.email,
            flag,
            position,
            dayClosedInvoice
        )
    }

    override fun toString(): String {
        return "UserWithCreditCard(user=$user, id=$id, holderName='$holderName', cardName='$cardName', value=$value, colorCard=$colorCard, typeCard=$typeCard, flag=$flag, position=$position)"
    }


}