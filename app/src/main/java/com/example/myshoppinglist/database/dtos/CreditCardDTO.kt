package com.example.myshoppinglist.database.dtos

import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue

class CreditCardDTO(
    var idApi: Long = 0L,
    var idCard: Long = 0L,
    var cardName: String = "",
    var holderName: String = "",
    var value: Float = 0F,
    var colorCard: Int = card_blue.toArgb(),
    var typeCard: TypeCard = TypeCard.CREDIT,
    var flag: Int = CardCreditFlag.MONEY.flag,
    var flagBlack: Int = CardCreditFlag.MONEY.flagBlack,
    var position: Int = 0,
    var email: String = ""){

     fun fromCreditCardDTO(creditCard: CreditCard): CreditCardDTO{
        return CreditCardDTO(idApi, creditCard.id, creditCard.cardName, creditCard.holderName, creditCard.value, creditCard.colorCard, creditCard.typeCard, creditCard.flag, fromFlagBlack(creditCard.flag), creditCard.position, creditCard.cardUserId)
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
        val creditCard = CreditCard(idApi, holderName, cardName, value, colorCard, typeCard, email, flag, position)
        creditCard.id = idCard

        return creditCard
    }
}