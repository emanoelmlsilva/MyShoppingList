package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.screen.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils

@Composable
fun CardCreditComponent(
    navController: NavController? = null,
    isClicable: Boolean,
    isDefault: Boolean,
    isChoiceColor: Boolean,
    flagCardCredit: Int = CardCreditFlag.MONEY.flag,
    typeCard: TypeCard,
    cardCreditDTO: CreditCardDTO,
    createCardCreditViewModel: CreateCardCreditFieldViewModel,
    modifier: Modifier?,
    callbackColor: CallbackColor?
) {

    var colorCurrent: Color = createCardCreditViewModel.colorCurrent.observeAsState(card_blue).value

    if (isChoiceColor) {

        Row(modifier = Modifier.padding(top = 32.dp)) {

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

            ChoiceColor(createCardCreditViewModel, callbackColor = callbackColor)
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
            null
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
    cardCreditDTO: CreditCardDTO,
    modifier: Modifier?
) {

    Card(
        backgroundColor = ((if (isDefault) background_card else cardColor)),
        modifier = (modifier?: Modifier)
            .fillMaxWidth(.85f)
            .fillMaxHeight(.9f)
            .clickable(isClicable) {
                navController?.navigate("createCards?hasToolbar=${true}")
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

            Box (modifier = Modifier
                .fillMaxSize()){
                Column(
                    modifier = Modifier
                        .fillMaxSize().padding(horizontal = 24.dp), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row (modifier = Modifier.fillMaxWidth().padding(top = 28.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        Text(if(cardCreditDTO.cardName.isBlank()) "Nome Cartão" else cardCreditDTO.cardName, modifier = Modifier.fillMaxWidth(.9f), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = text_secondary)
                        Image(
                            painter = painterResource(id = flagCardCredit),
                            contentDescription = "tipo de cartao",
                            modifier = Modifier
                                .size( if (typeCard == TypeCard.MONEY) 20.dp else 35.dp)
                        )
                    }
                    Text(
                        FormatUtils().getFormatValue(cardCreditDTO.value), modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp), color = text_secondary, fontSize = 40.sp, fontFamily = LatoBold
                    )
                    Text(if(cardCreditDTO.holderName.isBlank()) "Titular" else cardCreditDTO.holderName, modifier = Modifier.fillMaxWidth(.8f).padding(top = 8.dp, bottom = 38.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = text_secondary)
                }


            }

        }

    }
}

@Composable
fun ChoiceColor(
    createCardCreditViewModel: CreateCardCreditFieldViewModel,
    callbackColor: CallbackColor?
) {
    val colorCollection: List<Color> = listOf(
        card_blue,
        card_purple,
        card_pink_dark,
        card_red_light,
        card_blue_dark,
        card_green_dark,
        card_green,
        card_pink,
        card_blue_light,
        card_ruby,
        card_red_dark,
        card_orange
    )

    val colorCurrent: Color by createCardCreditViewModel.colorCurrent.observeAsState(card_blue)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
    ) {
        repeat(colorCollection.size) { index ->
            val color = colorCollection[index]
            ItemColor(
                color,
                color == colorCurrent
            ) {
                if (colorCurrent != color) {
                    callbackColor?.setColorCurrent(color)
                    createCardCreditViewModel.onChangeColorCurrent(color)
                }
            }
        }
    }
}

@Composable
fun ItemColor(color: Color, isChoiced: Boolean, onClickListener: () -> Unit) {

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        shape = RoundedCornerShape(0.dp),
        border = if (isChoiced) BorderStroke(1.dp, text_primary) else null,
        modifier = Modifier
            .padding(start = 2.dp)
            .height(18.dp)
            .width(20.dp)
            .padding(0.dp),
        onClick = onClickListener
    ) {
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
        flagCardCredit = CardCreditFlag.MASTER.flag,
        cardColor = card_blue,
        cardCreditDTO = CreditCardDTO(
            colorCard = card_purple.toArgb(),
            value = 123.4F,
            cardName = "Teste",
            holderName = "teste"
        ),
        modifier = null
    )
}