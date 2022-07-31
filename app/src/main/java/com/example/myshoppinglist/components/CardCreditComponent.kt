package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.screen.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils

@Composable
fun CardCreditComponent(navController: NavController? = null, isClicable: Boolean, isDefault: Boolean, isChoiceColor: Boolean, typeCard: TypeCard, cardCreditDTO: CreditCardDTO, createCardCreditViewModel: CreateCardCreditFieldViewModel, modifier: Modifier?, callbackColor: CallbackColor?) {

    var colorCurrent: Color = createCardCreditViewModel.colorCurrent.observeAsState(card_blue).value

    if (isChoiceColor) {

        Column{

            CustomCardCredit(null, isDefault, isClicable, typeCard, colorCurrent,  cardCreditDTO, null)

            ChoiceColor(createCardCreditViewModel, callbackColor = callbackColor)
        }
    } else {
        colorCurrent = Color(cardCreditDTO.colorCard)
        CustomCardCredit(navController,isDefault, isClicable, typeCard, colorCurrent, cardCreditDTO, modifier)

    }

}

@Composable
fun CustomCardCredit(
    navController: NavController?,
    isDefault: Boolean,
    isClicable: Boolean,
    typeCard: TypeCard,
    cardColor: Color,
    cardCreditDTO: CreditCardDTO,
    modifier: Modifier?
) {
    val hasToobar = true

    var modifierAux = modifier
        ?: Modifier
            .width(400.dp)
            .height(200.dp)

    Card(
        backgroundColor = ((if (isDefault) background_card else cardColor)),
        modifier = modifierAux
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable(isClicable) {
                navController?.navigate("createCards?hasToolbar=${hasToobar}")
            },
        elevation = 2.dp, shape = RoundedCornerShape(8.dp)
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
            ) {
                Column( modifier = Modifier
                    .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
                ){
                    Icon(modifier = Modifier.padding(start = 6.dp, top = 24.dp), painter = painterResource(id = if(typeCard == TypeCard.MONEY) R.drawable.ic_baseline_monetization_on_24 else R.drawable.ic_baseline_credit_card_24), contentDescription = "tipo de cartao",)
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.79f)
                    ) {
                        Row(
                            Modifier
                                .fillMaxHeight(0.5f)
                                .fillMaxWidth(0.9f)
                                .padding(start = 16.dp, 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomTextColumn(title = "Titular", subTitle = cardCreditDTO.holderName, 0.5f, 0.4f)
                            CustomTextColumn(title = "Nome Cartão", subTitle = cardCreditDTO.cardName, 1f, 1f)
                        }

                        Footer(value = cardCreditDTO!!.value)
                    }
                }


            }

        }

    }
}

@Composable
fun CustomTextColumn(title: String, subTitle: String, width: Float = 1f, columnWidth: Float = 1f) {
    Column(
        modifier = Modifier
            .fillMaxWidth(columnWidth)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(width)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(), verticalAlignment = Alignment.Bottom
        ) {
            Text(subTitle, color = secondary, fontSize = 12.sp)
        }
    }
}

@Composable
fun Footer(value: Float) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth()
    ) {
        Divider(
            color = text_primary,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.006f)
        )
        Text(FormatUtils().getFormatValue(value), modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 7.dp), color = text_secondary)
    }
}

@Composable
fun ChoiceColor(createCardCreditViewModel: CreateCardCreditFieldViewModel, callbackColor: CallbackColor?) {
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

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 5.dp, top = 20.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        items(colorCollection) { color ->
            ItemColor(
                color,
                color == colorCurrent
            ) { if (colorCurrent != color) {
                callbackColor?.setColorCurrent(color)
                createCardCreditViewModel.onChangeColorCurrent(color)
            } }
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

    CustomCardCredit(null, isClicable = false, isDefault = false, typeCard = TypeCard.CREDIT, cardColor = card_blue, cardCreditDTO = CreditCardDTO(
        colorCard = card_purple.toArgb(), value = 123.4F, cardName = "Teste", holderName =  "teste"),
        modifier = null
    )
}