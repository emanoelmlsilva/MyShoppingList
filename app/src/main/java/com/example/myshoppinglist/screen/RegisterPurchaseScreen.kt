package com.example.myshoppinglist.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.CustomDropdownMenu
import com.example.myshoppinglist.components.NumberInputComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(navController: NavHostController?) {
    var valueProduct by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Menu Btn",
                        tint = secondary_dark
                    )
                }
            },
            backgroundColor = text_secondary,
            elevation = 0.dp
        )
    }) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInputComponent(
                label = "Produto",
                value = valueProduct,
                customOnClick = object : CustomTextFieldOnClick {
                    override fun onChangeValeu(newValue: String) {
                        Log.d("TESTE", "RECUPERANDO VALOR ${newValue}")
                        valueProduct = newValue
                    }
                })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberInputComponent(maxChar = 13,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .padding(end = 16.dp),
                    label = "Preço",
                    customOnClick = object :
                        CustomTextFieldOnClick {
                        override fun onChangeValeu(newValue: String) {
                            Log.d("TESTE", "RECUPERANDO VALOR ${newValue}")
                        }
                    })
                BoxChoiceValue()
            }

            PurchaseAndPaymentComponent()

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp, top = 16.dp),
                onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = text_secondary
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("ADICIONAR", color = text_secondary)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun PurchaseAndPaymentComponent() {
    var expanded by remember { mutableStateOf(true) }
    var isBlock by remember { mutableStateOf(false) }
    var isFormPaymentCredit by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val creditCardViewModel = CreditCardViewModel(context)
    val cardCreditCollection = creditCardViewModel.searchCollectionResult.observeAsState()

    creditCardViewModel.getAll()

    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        backgroundColor = text_secondary,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                    Text(
                        text = "Local da Compra & Pagamento",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
                    onClick = { },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_block_24),
                        contentDescription = null,
                        tint = text_primary,
                    )
                }

            }
            if (expanded) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                TextInputComponent(
                    label = "Local",
                    customOnClick = object : CustomTextFieldOnClick {
                        override fun onChangeValeu(newValue: String) {

                        }

                    })

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Forma de Pagamento")
                        IconButton(
                            modifier = Modifier.then(Modifier.size(24.dp)),
                            onClick = { isFormPaymentCredit = !isFormPaymentCredit },
                        ) {
                            Icon(
                                painter = painterResource(id = if (isFormPaymentCredit) R.drawable.ic_baseline_credit_card_24 else R.drawable.ic_baseline_monetization_on_24),
                                contentDescription = null,
                                tint = text_primary,
                            )
                        }
                    }
                    Divider(
                        color = text_primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                    if (isFormPaymentCredit) CustomDropdownMenu(
                        getNameCard(cardCreditCollection.value!!),
                        object : CustomTextFieldOnClick {
                            override fun onChangeValeu(newValue: String) {

                            }
                        })

                }
            }

        }
    }

}

fun getNameCard(creditCardColelction: List<CreditCard>): List<String> {
    var cardCreditFormated: MutableList<String> =
        creditCardColelction.map { creditCard -> creditCard.cardName } as MutableList<String>
    cardCreditFormated.add(0, "Cartões")
    return cardCreditFormated
}

@Composable
fun CustomButton(callback: Callback, icon: Int) {
    Card(elevation = 2.dp, shape = RoundedCornerShape(6.dp), backgroundColor = background_card) {
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { callback.onClick() },
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = text_primary,
            )
        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun BoxChoiceValue() {
    var isMoney by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("0") }
    var convertedValue = 0

    var customOnClick = object : CustomTextFieldOnClick {
        override fun onChangeValeu(newValue: String) {
            value = newValue

        }

        override fun onClick() {
            isMoney = !isMoney
        }
    }

    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(callback = object : Callback {
                override fun onClick() {
                    convertedValue = MaskUtils.replaceAll(value).toInt()
                    if (convertedValue > 0) {
                        convertedValue -= 1
                        value = convertedValue.toString()
                    }
                }
            }, icon = R.drawable.ic_baseline_remove_24)
            NumberInputComponent(
                maxChar = 11,
                hasIcon = true,
                value = value,
                isMandatory = false,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .fillMaxWidth(0.79f),
                label = if (isMoney) "Quantidade" else "Quilograma",
                customOnClick = customOnClick
            )
            CustomButton(callback = object : Callback {
                override fun onClick() {
                    convertedValue = MaskUtils.replaceAll(value).toInt()
                    convertedValue += 1
                    value = convertedValue.toString()
                }
            }, icon = R.drawable.ic_baseline_add_24)
        }
    }

}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PreviewRegisterPurchaseScreen() {
    RegisterPurchaseScreen(null)
}