package com.example.myshoppinglist.database.entities
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "credit_cards")
class CreditCard {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @SerializedName("idCard")
    @ColumnInfo(name = "idCard")
    var id: Long = 0

    @SerializedName("idApi")
    @ColumnInfo(name = "idApi")
    var idApi: Long = 0

    @SerializedName("holderName")
    @ColumnInfo(name = "holderName")
    var holderName: String = ""

    @SerializedName("cardName")
    @ColumnInfo(name = "cardName")
    var cardName: String = ""

    @SerializedName("value")
    @ColumnInfo(name = "value")
    var value: Float = 0F

    @SerializedName("colorCard")
    @ColumnInfo(name = "colorCard")
    var colorCard: Int = card_blue.toArgb()

    @SerializedName("typeCard")
    @ColumnInfo(name = "typeCard")
    var typeCard: TypeCard = TypeCard.CREDIT

    @SerializedName("cardUserId")
    @ColumnInfo(name = "cardUserId")
    var cardUserId: String = ""

    @SerializedName("flag")
    @ColumnInfo(name = "flag")
    var flag: Int = CardCreditFlag.MONEY.flag

    @SerializedName("position")
    @ColumnInfo(name = "position", defaultValue = 0.toString())
    var position: Int = 0

    constructor()

    constructor(
        holderName: String,
        cardName: String,
        value: Float,
        colorCard: Int,
        typeCard: TypeCard,
        cardUserId: String,
        flag: Int,
        position: Int
    ) {
        this.holderName = holderName
        this.cardName = cardName
        this.value = value
        this.colorCard = colorCard
        this.typeCard = typeCard
        this.cardUserId = cardUserId
        this.flag = flag
        this.position = position
    }

    constructor(
        idApi: Long,
        holderName: String,
        cardName: String,
        value: Float,
        colorCard: Int,
        typeCard: TypeCard,
        cardUserId: String,
        flag: Int,
        position: Int
    ) {
        this.idApi = idApi
        this.holderName = holderName
        this.cardName = cardName
        this.value = value
        this.colorCard = colorCard
        this.typeCard = typeCard
        this.cardUserId = cardUserId
        this.flag = flag
        this.position = position
    }

    override fun toString(): String {
        return "CreditCard(id=$id, idApi=$idApi, holderName='$holderName', cardName='$cardName', value=$value, colorCard=$colorCard, typeCard=$typeCard, cardUserId='$cardUserId', flag=$flag, position=$position)"
    }

}