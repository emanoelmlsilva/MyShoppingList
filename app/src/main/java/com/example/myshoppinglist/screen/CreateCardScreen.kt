package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.CardCreditComponent
import com.example.myshoppinglist.components.TextInputComponent
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

    fun saveCreditCard() {
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
                if (typeCard == TypeCard.MONEY) {
                    navController?.navigate("home") {
                        popUpTo(0)
                    }
                } else {
                    navController?.popBackStack()
                }
            }
        }
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxHeight(.7f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (typeCard == TypeCard.MONEY) {
                    Column(modifier = Modifier.padding(vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = stringResource(R.string.message_info), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Divider(color = secondary_dark,)
                    }
                }

                CardCreditComponent(
                    isClicable = false,
                    isDefault = false,
                    typeCard = typeCard,
                    isChoiceColor = true,
                    cardCreditDTO = CreditCardDTO(
                        colorCard = card_blue.toArgb(),
                        value = 0F,
                        cardName = nameCard,
                        holderName = name
                    ),
                    createCardCreditViewModel = createCardCreditViewModel,
                    modifier = null,
                    callbackColor = object :
                        CallbackColor() {
                        override fun setColorCurrent(color: Color) {
                            createCardCreditViewModel.onChangeColorCurrent(color)
                        }
                    }
                )
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(0.14f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Dados do Cartão", fontWeight = FontWeight.Bold)

                        Divider(
                            color = secondary_dark,

                            )
                    }
                    TextFieldContent(createCardCreditViewModel, object : Callback {
                        override fun onClick() {
                            saveCreditCard()
                        }
                    })

                }
            }
            item {
                ButtonsFooterContent(
                    btnTextAccept = "SALVAR",
                    onClickAccept = {
                        saveCreditCard()
                    })
            }

        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(cardCreditViewModel: CreateCardCreditFieldViewModel, callback: Callback) {
    val name: String by cardCreditViewModel.name.observeAsState("")
    val nameCard: String by cardCreditViewModel.nameCard.observeAsState(initial = "")
    val isErrorName by cardCreditViewModel.isErrorName.observeAsState(initial = false)
    val isErrorNameCard by cardCreditViewModel.isErrorNameCard.observeAsState(initial = false)
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .height(190.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputComponent(modifier = Modifier.fillMaxWidth(), value = name, label = "Titular", isMandatory = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ), error = isErrorName, customOnClick = object : CustomTextFieldOnClick {
            override fun onChangeValue(newValue: String) {
                cardCreditViewModel.onChangeName(newValue)
            }
        })

        TextInputComponent(modifier = Modifier.fillMaxWidth(), value = nameCard, label = "Nome Cartão", isMandatory = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(
            onNext = {
                callback.onClick()
            }
        ), error = isErrorNameCard, customOnClick = object : CustomTextFieldOnClick {
            override fun onChangeValue(newValue: String) {
                cardCreditViewModel.onChangeNameCard(newValue)
            }
        })
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