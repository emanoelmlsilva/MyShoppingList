package com.example.myshoppinglist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.viewModels.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatDateUtils

@Composable
fun CardCreditComponent(
    navController: NavController? = null,
    isClicable: Boolean,
    isDefault: Boolean,
    isChoiceColor: Boolean,
    flagCardCredit: Int = CardCreditFlag.MONEY.flag,
    typeCard: TypeCard,
    cardCreditDTO: CreditCardDTODB,
    createCardCreditViewModel: CreateCardCreditFieldViewModel,
    modifier: Modifier?,
    callbackColor: CallbackColor?
) {

    var colorCurrent: Color = createCardCreditViewModel.colorCurrent.observeAsState(card_blue).value

    if (isChoiceColor) {

        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            CustomCardCredit(
                null,
                isDefault,
                isClicable,
                typeCard,
                flagCardCredit,
                colorCurrent,
                cardCreditDTO,
                null
            )

            ColorPicker(colorCurrent = colorCurrent,callback = callbackColor!!)
        }
    } else {
        colorCurrent = Color(cardCreditDTO.colorCard)
        CustomCardCredit(
            navController,
            isDefault,
            isClicable,
            typeCard,
            flagCardCredit,
            colorCurrent,
            cardCreditDTO,
            modifier
        )

    }

}

@Composable
fun CustomCardCredit(
    navController: NavController?,
    isDefault: Boolean,
    isClicable: Boolean,
    typeCard: TypeCard,
    flagCardCredit: Int,
    cardColor: Color,
    cardCreditDTO: CreditCardDTODB,
    modifier: Modifier?
) {

    Card(
        backgroundColor = ((if (isDefault) background_card else cardColor)),
        modifier = (modifier ?: Modifier)
            .fillMaxWidth(.85f)
            .fillMaxHeight(.9f)
            .clickable(isClicable) {
                navController?.navigate(
                    "${Screen.CreateCards.name}?hasToolbar=${true}?holderName=${cardCreditDTO.holderName}?isUpdate=${false}?creditCardDTO=${""}"
                )
            },
        elevation = 2.dp, shape = RoundedCornerShape(28.dp)
    ) {
        if (isDefault) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Adicionar")
            }
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (cardCreditDTO.cardName.isBlank()) "Nome Cartão" else cardCreditDTO.cardName,
                            modifier = Modifier.fillMaxWidth(.9f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = text_secondary
                        )
                        Image(
                            painter = painterResource(id = flagCardCredit),
                            contentDescription = "tipo de cartao",
                            modifier = Modifier
                                .shadow(21.dp, CircleShape)
                                .size(if (typeCard == TypeCard.MONEY) 20.dp else 35.dp)
                        )

                    }
                    Text(
                        FormatDateUtils().getFormatValue(cardCreditDTO.value),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.5f)
                            .padding(bottom = 16.dp),
                        color = text_secondary,
                        fontSize = 40.sp,
                        fontFamily = LatoBold
                    )
                    Text(
                        if (cardCreditDTO.holderName.isBlank()) "Titular" else cardCreditDTO.holderName,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .padding(top = 8.dp, bottom = 38.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = text_secondary
                    )
                }


            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardCredit() {

    CustomCardCredit(
        null,
        isClicable = false,
        isDefault = false,
        typeCard = TypeCard.CREDIT,
        flagCardCredit = CardCreditFlag.MONEY.flag,
        cardColor = card_green_dark,
        cardCreditDTO = CreditCardDTODB(
            colorCard = card_purple.toArgb(),
            value = 123.4F,
            cardName = "Teste",
            holderName = "teste"
        ),
        modifier = null
    )
}