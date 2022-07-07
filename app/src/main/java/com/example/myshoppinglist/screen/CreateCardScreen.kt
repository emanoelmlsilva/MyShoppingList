package com.example.myshoppinglist.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.myshoppinglist.controller.Callback
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.CardCreditComponent
import com.example.myshoppinglist.controller.CallbackColor
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@ExperimentalComposeUiApi
@Composable
fun CreateCardScreen(navController: NavController?, typeCard: TypeCard) {
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val context = LocalContext.current
    val creditCardViewModel = CreditCardViewModel(context)
    val userViewModel = UserViewModel(context)
    val name: String by createCardCreditViewModel.name.observeAsState("")
    val nameCard: String by createCardCreditViewModel.nameCard.observeAsState(initial = "")
    val colorCurrent: Color by createCardCreditViewModel.colorCurrent.observeAsState(initial = card_blue)

    userViewModel.getUserCurrent()

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

            CardCreditComponent(
                isClicable = false,
                isDefault = false,
                typeCard = typeCard,
                isChoiceColor = true,
                cardCreditDTO = CreditCardDTO(colorCard = card_blue.toArgb(), value = 0F, cardName = name, holderName = nameCard),
                createCardCreditViewModel = createCardCreditViewModel, modifier = null, callbackColor = object :
                    CallbackColor() {
                    override fun setColorCurrent(color: Color) {
                        createCardCreditViewModel.onChangeColorCurrent(color)
                    }
                }
            )
            Column(modifier = Modifier.height(230.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Column(
                    modifier = Modifier.fillMaxHeight(0.14f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Dados do Cartão", fontWeight = FontWeight.Bold)

                    Divider(
                        color = secondary_dark,

                        )
                }
                TextFieldContent(createCardCreditViewModel)

            }

            ButtonsFooterContent(
                btnTextAccept = "SALVAR",
                btnTextCancel = "CANCELAR",
                onClickAccept = {
                    userViewModel.searchResult.observeForever {
                            if (createCardCreditViewModel.checkFileds()) {
                                creditCardViewModel.insertCreditCard(
                                    CreditCard(
                                        name,
                                        nameCard,
                                        0F,
                                        colorCurrent.toArgb(),
                                        typeCard,
                                        it.name
                                    )
                                )
                                if(typeCard == TypeCard.MONEY){
                                    navController?.navigate("home"){
                                        popUpTo(0)
                                    }
                                }else{
                                    navController?.popBackStack()
                                }
                            }
                    }
                },
                onClickCancel = {
                })
        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(cardCreditViewModel: CreateCardCreditFieldViewModel) {
    val name: String by cardCreditViewModel.name.observeAsState("")
    val nameCard: String by cardCreditViewModel.nameCard.observeAsState(initial = "")
    val isErrorName by cardCreditViewModel.isErrorName.observeAsState(initial = false)
    val isErrorNameCard by cardCreditViewModel.isErrorNameCard.observeAsState(initial = false)
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .height(190.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    cardCreditViewModel.onChangeName(it)
                },
                label = { Text("Titular") },
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

class CreateCardCreditFieldViewModel : BaseFieldViewModel() {
    var name: MutableLiveData<String> = MutableLiveData("")
    var nameCard: MutableLiveData<String> = MutableLiveData("")
    var colorCurrent: MutableLiveData<Color> = MutableLiveData(card_blue)
    var isErrorName: MutableLiveData<Boolean> = MutableLiveData(false)
    var isErrorNameCard: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onChangeColorCurrent(newColorCurrent: Color) {
        colorCurrent.value = newColorCurrent
    }

    fun onChangeName(newName: String) {
        onChangeIsErrorName(newName.isBlank())
        name.value = newName
    }

    fun onChangeNameCard(newNameCard: String) {
        onChangeIsErrorNameCard(newNameCard.isBlank())
        nameCard.value = newNameCard
    }

    fun onChangeIsErrorName(newIsError: Boolean) {
        isErrorName.value = newIsError
    }

    fun onChangeIsErrorNameCard(newIsError: Boolean) {
        isErrorNameCard.value = newIsError
    }

    override fun checkFileds(): Boolean {
        if (name.value!!.isBlank()) {
            onChangeIsErrorName(true)
            return false
        }

        if (nameCard.value!!.isBlank()) {
            onChangeIsErrorNameCard(true)
            return false
        }

        return true
    }
}