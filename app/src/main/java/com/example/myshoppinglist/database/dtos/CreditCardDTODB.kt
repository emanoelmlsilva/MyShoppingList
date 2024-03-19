package com.example.myshoppinglist.database.dtos

import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.ui.theme.card_blue
import com.google.gson.annotations.SerializedName

class CreditCardDTODB(
    @SerializedName("idMyShoppingApi")
    var idMyShoppingApi: Long = 0L,
    var myShoppingId: Long = 0L,
    @SerializedName("cardName")
    var cardName: String = "",
    @SerializedName("holderName")
    var holderName: String = "",
    @SerializedName("value")
    var value: Float = 0F,
    @SerializedName("colorCard")
    var colorCard: Int = card_blue.toArgb(),
    @SerializedName("typeCard")
    var typeCard: TypeCard = TypeCard.CREDIT,
    @SerializedName("flag")
    var flag: Int = CardCreditFlag.MASTER.flagBlack,
    @SerializedName("flagBlack")
    var flagBlack: Int = CardCreditFlag.MASTER.flagBlack,
    @SerializedName("position")
    var position: Int = 0,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("dayClosedInvoice")
    var dayClosedInvoice: Int = 0){

    @SerializedName("user")
    var userDTO: UserDTO = UserDTO()
    @SerializedName("typeCardApi")
    var typeCardApi: Int = 0

    fun fromCreditCardDTO(): CreditCardDTO {
        return CreditCardDTO(this.idMyShoppingApi, this.myShoppingId, this.cardName, this.holderName, this.value, this.colorCard, this.typeCard.ordinal, userDTO, this.flagBlack, this.position, this.dayClosedInvoice)
    }

    fun fromCreditCardDTODB(creditCard: CreditCard): CreditCardDTODB{
         typeCardApi = creditCard.typeCard.ordinal
        return CreditCardDTODB(creditCard.idMyShoppingApi, creditCard.myShoppingId, creditCard.cardName, creditCard.holderName, creditCard.value, creditCard.colorCard, creditCard.typeCard, creditCard.flag, creditCard.flag, creditCard.position, creditCard.cardUserId, creditCard.dayClosedInvoice)
    }

    fun fromFlagBlack(): Int{
        return fromFlagBlack(this.flag)
    }

    private fun fromFlagBlack(int: Int): Int{
        return when(int){
            CardCreditFlag.ELO.flag -> CardCreditFlag.ELO.flagBlack
            CardCreditFlag.HIPER.flag -> CardCreditFlag.HIPER.flagBlack
            CardCreditFlag.MASTER.flag -> CardCreditFlag.MASTER.flagBlack
            CardCreditFlag.AMEX.flag -> CardCreditFlag.AMEX.flagBlack
            CardCreditFlag.PAY_PAL.flag -> CardCreditFlag.PAY_PAL.flagBlack
            CardCreditFlag.VISA.flag -> CardCreditFlag.VISA.flagBlack
            else -> CardCreditFlag.MONEY.flagBlack
        }
    }

    fun toCreditCard(): CreditCard{
        typeCardApi = typeCard.ordinal
        val creditCard = CreditCard(idMyShoppingApi, holderName, cardName, value, colorCard, typeCard, if(email.isBlank()) userDTO.email else email, flag, position)
        creditCard.myShoppingId = myShoppingId
        creditCard.dayClosedInvoice = dayClosedInvoice
        return creditCard
    }

    override fun toString(): String {
        return "CreditCardDTO(id=$idMyShoppingApi, idCard=$myShoppingId, cardName='$cardName', holderName='$holderName', value=$value, colorCard=$colorCard, typeCard=$typeCard, flag=$flag, flagBlack=$flagBlack, position=$position, email='$email', userDTO=$userDTO)"
    }


}