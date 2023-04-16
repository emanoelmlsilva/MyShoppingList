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
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.background_card_light


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChoiceCardComponent(
    cardCurrent: Long,
    cardCreditCollection: List<CreditCardDTO>,
    callbackCard: CallbackCreditCard, modifier: Modifier = Modifier
        .fillMaxHeight(.2f)
        .fillMaxWidth()
        .padding(start = 16.dp)
) {
    var cardCreditChoice by remember { mutableStateOf(CreditCardDTO(idCard = cardCurrent)) }

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
                    val isChoiceCurrent = cardCreditChoice.idCard == cardCredit.idCard

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
                                cardCreditChoice = cardCredit
                                callbackCard.onChangeValue(cardCreditChoice.idCard)
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