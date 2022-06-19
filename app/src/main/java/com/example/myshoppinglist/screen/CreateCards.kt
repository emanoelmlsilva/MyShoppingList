package com.example.myshoppinglist.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.CardCredit
import com.example.myshoppinglist.ui.theme.*

data class CardCredit(
    val color: androidx.compose.ui.graphics.Color,
    val value: Float,
    val name: String,
    val nickName: String
)

@ExperimentalComposeUiApi
@Composable
fun CreateCards(navController: NavController?) {
    val cardCreditViewModel: CardCreditViewModel = viewModel()
    val name: String by cardCreditViewModel.name.observeAsState("")
    val nameCard: String by cardCreditViewModel.nameCard.observeAsState(initial = "")

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, bottom = 16.dp, end = 16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            CardCredit(
                isClicable = false,
                isDefault = false,
                isChoiceColor = true,
                cardCredit = CardCredit(card_blue, 0F, name, nameCard),cardCreditViewModel
            )
            Column(modifier = Modifier.height(230.dp), verticalArrangement = Arrangement.SpaceBetween){
                Column(modifier = Modifier.fillMaxHeight(0.14f), verticalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Dados do Cartão", fontWeight = FontWeight.Bold)

                    Divider(
                        color = secondary_dark,

                        )
                }
                TextFieldContent(cardCreditViewModel)

            }

            ButtonsFooterContent(btnTextAccept = "SALVAR", btnTextCancel = "CANCELAR", onClickAccept = {navController?.navigate("createUser")}, onClickCancel = {})
        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(cardCreditViewModel: CardCreditViewModel){
    val name: String by cardCreditViewModel.name.observeAsState("")
    val nameCard: String by cardCreditViewModel.nameCard.observeAsState(initial = "")
    var isErrorName by remember { mutableStateOf(false) }
    var isErrorNameCard by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column (Modifier.height(190.dp).fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween){
        Column{
            OutlinedTextField(
                value = name,
                onValueChange = {
                    isErrorName = it.isBlank()
                    cardCreditViewModel.onChangeName(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = isErrorName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }

        Column{
            OutlinedTextField(
                value = nameCard,
                onValueChange = {
                    isErrorNameCard = it.isBlank()
                    cardCreditViewModel.onChangeNameCard(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome Cartão") },
                singleLine = true,
                isError = isErrorNameCard,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }
    }

}

class CardCreditViewModel: ViewModel(){
    var name: MutableLiveData<String> = MutableLiveData("")
    var nameCard: MutableLiveData<String> = MutableLiveData("")
    var colorCurrent: MutableLiveData<Color> = MutableLiveData(card_blue)

    fun onChangeColorCurrent(newColorCurrent: Color) {
        colorCurrent.value = newColorCurrent
    }

    fun onChangeName(newName: String) {
        name.value = newName
    }

    fun onChangeNameCard(newNameCard: String) {
        nameCard.value = newNameCard
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PreviewCreateCards() {
    CreateCards(null)
}