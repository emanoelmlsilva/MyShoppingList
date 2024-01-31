package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.callback.CallbackOptions
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.ui.theme.*

@Composable
fun DialogTransferPurchaseComponent(
    context: Context,
    visibilityDialog: Boolean,
    idCard: Long,
    purchase: Purchase,
    callbackOptions: CallbackOptions
) {
    val creditCardDTOCollection = remember { mutableListOf<CreditCardDTODB>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    var idCardCurrent by remember { mutableStateOf(idCard) }

    LaunchedEffect(Unit) {
        creditCardController.findAllDB().observe(lifecycleOwner) {
            if (it.isNotEmpty()) {
                creditCardDTOCollection.removeAll(creditCardDTOCollection)
                creditCardDTOCollection.addAll(it.map { creditCard ->
                    CreditCardDTODB().fromCreditCardDTODB(
                        creditCard
                    )
                })
            }
        }
    }

    fun reset() {
        idCardCurrent = idCard
    }

    DialogCustom(visibilityDialog = visibilityDialog, percentHeight = 2f) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.1f)
                .background(text_secondary),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    Modifier
                        .height(25.dp)
                )

                Text(
                    "Transferir Compra",
                    fontFamily = LatoBlack,
                    color = primary_dark,
                    fontSize = 18.sp
                )

                Spacer(
                    Modifier
                        .height(20.dp)
                )

                Divider(
                    color = secondary_dark,
                    modifier = Modifier
                        .height(1.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.8f)
            ) {
                Text(
                    text = "Escolha para qual cartão desejá transferir a compra.",
                    fontSize = 14.sp,
                    fontFamily = LatoRegular,
                    modifier = Modifier.padding(top = 18.dp)
                )

                ChoiceCardComponent(
                    idCardCurrent,
                    disability = false,
                    creditCardDTOCollection,
                    object : CallbackCreditCard {
                        override fun onChangeFilterCreditCard(cardCreditFilter: CardCreditFilter) {
                            idCardCurrent = cardCreditFilter.id
                        }
                    },
                    Modifier
                        .fillMaxHeight(.3f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                ButtonsFooterContent(
                    isClickable = true,
                    btnTextCancel = "CANCELAR",
                    btnTextAccept = "SALVAR",
                    onClickCancel = {
                        callbackOptions.onTransfer(false, Purchase(), 0L)
                        reset()
                    },
                    onClickAccept = {
                        callbackOptions.onTransfer(false, purchase, idCardCurrent)
                        reset()
                    })
            }
        }
    }
}