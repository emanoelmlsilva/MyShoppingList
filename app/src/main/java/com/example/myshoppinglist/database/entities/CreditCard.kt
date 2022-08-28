package com.example.myshoppinglist.database.entities
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.enums.CardCreditFlag
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
    @ColumnInfo(name = "colorCard")
    var colorCard: Int = card_blue.toArgb()
    @ColumnInfo(name = "typeCard")
    var typeCard: TypeCard = TypeCard.CREDIT
    @ColumnInfo(name = "cardUserId")
    var cardUserId: String = ""
    @ColumnInfo(name = "flag")
    var flag: Int = CardCreditFlag.MONEY.flag

    constructor()

    constructor(
        holderName: String,
        cardName: String,
        value: Float,
        colorCard: Int,
        typeCard: TypeCard,
        cardUserId: String,
        flag: Int
    ) {
        this.holderName = holderName
        this.cardName = cardName
        this.value = value
        this.colorCard = colorCard
        this.typeCard = typeCard
        this.cardUserId = cardUserId
        this.flag = flag
    }

    override fun toString(): String {
        return "CreditCard(id=$id, holderName='$holderName', cardName='$cardName', value=$value, colorCard=$colorCard, typeCard=$typeCard, cardUserId='$cardUserId', flag=$flag)"
    }

}