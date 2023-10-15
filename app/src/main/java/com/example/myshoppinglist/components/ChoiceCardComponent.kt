package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.background_card_light


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChoiceCardComponent(
    cardCurrent: Long,
    disability: Boolean = true,
    cardCreditCollection: List<CreditCardDTODB>,
    callbackCard: CallbackCreditCard, modifier: Modifier = Modifier
        .fillMaxHeight(.2f)
        .fillMaxWidth()
        .padding(start = 16.dp)
) {
    var cardCreditChoice by remember { mutableStateOf(CreditCardDTODB(myShoppingId = cardCurrent)) }

    Column {
        Spacer(Modifier.size(24.dp))

        Text(text = "Cartões:", fontFamily = LatoBlack)

        Column(
            modifier = modifier
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                items(cardCreditCollection) { cardCredit ->
                    val isChoiceCurrent = cardCreditChoice.myShoppingId == cardCredit.myShoppingId

                    CustomerChip(
                        paddingVertical = 0.dp,
                        label = cardCredit.cardName,
                        iconId = cardCredit.flag,
                        isEnabled = true,
                        isBackgroundCircle = true,
                        isChoice = isChoiceCurrent,
                        color = background_card_light,
                        callback = object : Callback {
                            override fun onClick() {
                                cardCreditChoice = if(cardCreditChoice.myShoppingId == cardCredit.myShoppingId && disability) CreditCardDTODB(myShoppingId = 0L, flag = 0) else cardCredit

                                callbackCard.onChangeValue(cardCreditChoice.myShoppingId)
                                callbackCard.onChangeFilterCreditCard(
                                    CardCreditFilter(
                                        cardCreditChoice
                                    )
                                )

                            }
                        })
                }
            }

        }

    }

}