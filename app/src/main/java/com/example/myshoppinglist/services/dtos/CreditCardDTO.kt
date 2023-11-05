package com.example.myshoppinglist.services.dtos

import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import com.google.gson.annotations.SerializedName

class CreditCardDTO(
    @SerializedName("id")
    var id: Long = 0L,
    var idCard: Long = 0L,
    @SerializedName("cardName")
    var cardName: String = "",
    @SerializedName("holderName")
    var holderName: String = "",
    @SerializedName("value")
    var value: Float = 0F,
    @SerializedName("colorCard")
    var colorCard: Int = card_blue.toArgb(),
    @SerializedName("typeCard")
    var typeCard: Int = 0,
    @SerializedName("user")
    var userDTO: UserDTO = UserDTO(),
    @SerializedName("flag")
    var flag: Int = CardCreditFlag.MONEY.flag,
    @SerializedName("position")
    var position: Int = 0
) {

    fun toCreditCardIdApi() : CreditCard{
        val creditCard = toCreditCard()
        creditCard.myShoppingId = creditCard.idMyShoppingApi
        return creditCard
    }

    fun toCreditCard() : CreditCard{
        return CreditCard(
            idCard,
            id,
            cardName,
            holderName,
            value,
            colorCard,
            TypeCard.getTypeCard(typeCard),
            userDTO.email,
            flag,
            position)
    }
}