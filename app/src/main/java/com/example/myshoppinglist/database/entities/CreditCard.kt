package com.example.myshoppinglist.database.entities

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import org.jetbrains.annotations.NotNull

@Entity(tableName = "credit_cards")
class CreditCard {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "idCard")
    var id: Long = 0
    @ColumnInfo(name = "holderName")
    var holderName: String = ""
    @ColumnInfo(name = "cardName")
    var cardName: String = ""
    @ColumnInfo(name = "value")
    var value: Float = 0F
//    @ColumnInfo(name = "colorCard")
//    var colorCard: Color = card_blue
    @ColumnInfo(name = "typeCard")
    var typeCard: TypeCard = TypeCard.CREDIT
    @ColumnInfo(name = "cardUserId")
    var cardUserId: String = ""

    constructor()

    constructor(
        holderName: String,
        cardName: String,
        value: Float,
//        colorCard: Color,
        typeCard: TypeCard,
        cardUserId: String
    ) {
        this.holderName = holderName
        this.cardName = cardName
        this.value = value
//        this.colorCard = colorCard
        this.typeCard = typeCard
        this.cardUserId = cardUserId
    }

    override fun toString(): String {
        return "CreditCard(id=$id, holderName='$holderName', cardName='$cardName', value=$value, typeCard=$typeCard, cardUserId='$cardUserId')"
    }


}