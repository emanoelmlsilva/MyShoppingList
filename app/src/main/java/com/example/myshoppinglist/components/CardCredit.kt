package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshoppinglist.screen.CardCreditDTO
import com.example.myshoppinglist.screen.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.Format

@Composable
fun CardCredit(isClicable: Boolean, isDefault: Boolean, isChoiceColor: Boolean, cardCreditDTO: CardCreditDTO, createCardCreditViewModel: CreateCardCreditFieldViewModel) {
    val colorCurrent: Color by createCardCreditViewModel.colorCurrent.observeAsState(card_blue)

    if (isChoiceColor) {

        Column{

            CustomCardCredit(isDefault, isClicable, colorCurrent,  cardCreditDTO)

            ChoiceColor(createCardCreditViewModel)
        }
    } else {
        CustomCardCredit(isDefault, isClicable, colorCurrent, cardCreditDTO)

    }

}

@Composable
fun CustomCardCredit(
    isDefault: Boolean,
    isClicable: Boolean,
    cardColor: Color,
    cardCreditDTO: CardCreditDTO
) {
    Card(
        backgroundColor = ((if (isDefault) secondary_light else cardColor)!!),
        modifier = Modifier
            .width(400.dp)
            .height(200.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable(isClicable) {},
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
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                ) {
                    Row(
                        Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(0.9f)
                            .padding(start = 16.dp, 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomTextColumn(title = "Titular", subTitle = cardCreditDTO.name, 0.5f, 0.4f)
                        CustomTextColumn(title = "Nome Cartão", subTitle = cardCreditDTO.nickName, 1f, 1f)
                    }

                    Footer(value = cardCreditDTO!!.value)
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
            .fillMaxHeight(0.7f)
            .fillMaxWidth()
    ) {
        Divider(
            color = text_primary,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.006f)
        )
        Text(Format().getFormatValue(value), modifier = Modifier.padding(start = 16.dp, bottom = 14.dp), color = text_secondary)
    }
}

@Composable
fun ChoiceColor(createCardCreditViewModel: CreateCardCreditFieldViewModel) {
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
            ) { if (colorCurrent != color) createCardCreditViewModel.onChangeColorCurrent(color) }
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
            .fillMaxHeight(0.03f)
            .width(20.dp)
            .padding(0.dp),
        onClick = onClickListener
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardCredit() {
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    CardCredit(isClicable = false, isDefault = false, isChoiceColor = true, CardCreditDTO(
        card_purple, 123.4F, "Teste", "teste"), createCardCreditViewModel)
}