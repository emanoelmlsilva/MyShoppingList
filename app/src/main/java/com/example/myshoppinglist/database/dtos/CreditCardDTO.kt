package com.example.myshoppinglist.database.dtos

import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue

class CreditCardDTO(
    var cardName: String = "",
    var holderName: String = "",
    var value: Float = 0F,
    var colorCard: Int = card_blue.toArgb(),
    var typeCard: TypeCard = TypeCard.CREDIT,
    var flag: Int = CardCreditFlag.MONEY.flag) {
}