package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun BoxDropdownCardCredit(
    cardCredits: List<CreditCard>,
    creditCard: CreditCard?,
    callback: CallbackCreditCard
) {
    var expanded by remember { mutableStateOf(false) }
    var creditCardCurrent by remember { mutableStateOf(creditCard) }

    Card(elevation = 0.dp,
        backgroundColor = Color(creditCardCurrent!!.colorCard),
        modifier = Modifier
            .padding(start = 6.dp)
            .clickable(onClick = { expanded = true })
    ) {
        Text(
            text = creditCardCurrent!!.cardName,
            modifier = Modifier.padding(4.dp),
            color = text_secondary,
            fontSize = 12.sp
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            cardCredits.forEach { cardCredit ->
                DropdownMenuItem(modifier = Modifier.height(25.dp), onClick = {
                    expanded = false
                    callback.onChangeValueCreditCard(cardCredit)
                    creditCardCurrent = cardCredit
                }) {
                    Text(text = cardCredit.cardName, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxDropdownCardCredit() {
    BoxDropdownCardCredit(
        listOf(
            CreditCard(
                "Emanoel S. Medeiros",
                "Nubank asdf",
                0.0F,
                card_blue_dark.toArgb(),
                TypeCard.CREDIT,
                "emanoel",
                CardCreditFlag.MONEY.flag
            )
        ),
        CreditCard(
            "Emanoel S. Medeiros",
            "Nubank asdfd eeee",
            0.0F,
            card_blue_dark.toArgb(),
            TypeCard.CREDIT,
            "emanoel",
            CardCreditFlag.MONEY.flag
        ),
        object : CallbackCreditCard {
        })
}