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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.Format

@Composable
fun CardCredit(isClicable: Boolean, isDefault: Boolean, cardColor: Color, isChoiceColor: Boolean) {
    if (isChoiceColor) {

        val cardCreditViewModel: CardCreditViewModel = viewModel()
        val colorCurrent: Color by cardCreditViewModel.colorCurrent.observeAsState(card_blue)

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth()
        ) {

            CustomCardCredit(isDefault, isClicable, colorCurrent, true)

            ChoiceColor(cardCreditViewModel)
        }
    } else {
        CustomCardCredit(isDefault, isClicable, cardColor, false)

    }

}

@Composable
fun CustomCardCredit(
    isDefault: Boolean,
    isClicable: Boolean,
    cardColor: Color,
    isChoiceColor: Boolean
) {
    Card(
        backgroundColor = (if (isDefault) secondary_light else cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(if (isChoiceColor) 0.8f else 0.25f)
            .padding(horizontal = 32.dp, vertical = 4.dp)
            .clickable(isClicable) {},
        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
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
                        CustomTextColumn(title = "Titular", subTitle = "Emanoel", 0.5f, 0.4f)
                        CustomTextColumn(title = "Nome Cartão", subTitle = "El barto", 1f, 1f)
                    }

                    Footer(value = 0F)
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
fun ChoiceColor(cardCreditViewModel: CardCreditViewModel) {
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

    val colorCurrent: Color by cardCreditViewModel.colorCurrent.observeAsState(card_blue)

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 30.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        items(colorCollection) { color ->
            ItemColor(
                color,
                color == colorCurrent
            ) { if (colorCurrent != color) cardCreditViewModel.onChangeColorCurrent(color) }
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
            .fillMaxHeight(0.3f)
            .width(20.dp)
            .padding(0.dp),
        onClick = onClickListener
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardCredit() {
    CardCredit(isClicable = false, isDefault = false, cardColor = card_blue, isChoiceColor = true)
}

class CardCreditViewModel : ViewModel() {
    var colorCurrent: MutableLiveData<Color> = MutableLiveData(card_blue)

    fun onChangeColorCurrent(newColorCurrent: Color) {
        colorCurrent.value = newColorCurrent
    }
}