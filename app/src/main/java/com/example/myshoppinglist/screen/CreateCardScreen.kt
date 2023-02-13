package com.example.myshoppinglist.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
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
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.ui.theme.*

@ExperimentalComposeUiApi
@Composable
fun CreateCardScreen(navController: NavController?, hasToolbar: Boolean, nameUser: String) {
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
    val userViewModel = UserViewModel(context)
    val name: String by createCardCreditViewModel.name.observeAsState(if(hasToolbar) nameUser else "")
    val nameCard: String by createCardCreditViewModel.nameCard.observeAsState(initial = "")
    val colorCurrent: Color by createCardCreditViewModel.colorCurrent.observeAsState(initial = card_blue)
    val flagCurrent: Int by createCardCreditViewModel.flagCurrent.observeAsState(initial = CardCreditFlag.MONEY.flag)
    var userDTO by remember {
        mutableStateOf<UserDTO?>(null)
    }

    LaunchedEffect(Unit){
        userViewModel.getUserCurrent()
    }

    LaunchedEffect(key1 = hasToolbar){
        createCardCreditViewModel.onChangeName(nameUser)
    }

    userViewModel.searchResult.observe(lifecycleOwner) {
        userDTO = UserDTO(it)
    }

    val typeCard = if(hasToolbar) TypeCard.CREDIT else TypeCard.MONEY

    fun saveCreditCard() {
        if(userDTO != null){
            if (createCardCreditViewModel.checkFileds()) {
                creditCardViewModel.insertCreditCard(
                    CreditCard(
                        name.trim(),
                        nameCard.trim(),
                        0F,
                        colorCurrent.toArgb(),
                        typeCard,
                        userDTO!!.name,
                        flagCurrent
                    )
                )
                if (typeCard == TypeCard.MONEY) {
                    UserInstanceImpl.getInstance(context)
                    navController?.navigate(Screen.Home.name) {
                        popUpTo(0)
                    }
                } else {
                    navController?.popBackStack()
                }
            }
        }
    }

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, hasToolbar = hasToolbar, isScrollable = true, content = {
        Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column{
                if (typeCard == TypeCard.MONEY) {
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.message_info),
                            fontSize = 13.sp,
                            fontFamily = LatoRegular,
                        )
                        Divider(color = secondary_dark,modifier = Modifier
                            .padding(horizontal = 34.dp)
                            .background(card_pink),)
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
                    flagCardCredit = flagCurrent,
                    createCardCreditViewModel = createCardCreditViewModel,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(card_pink),
                    callbackColor = object :
                        CallbackColor() {
                        override fun setColorCurrent(color: Color) {
                            createCardCreditViewModel.onChangeColorCurrent(color)
                        }
                    }
                )
                Column(
                    modifier = Modifier.padding(top = 32.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Dados do Cartão", fontFamily = LatoBold)
                        Divider(color = divider, modifier = Modifier.padding(top = 8.dp))
                    }
                    TextFieldContent(lifecycleOwner, createCardCreditViewModel, hasToolbar, object : Callback {
                        override fun onClick() {
                            saveCreditCard()
                        }
                    })
                    if (typeCard != TypeCard.MONEY) { ChoiceFlag(flagCurrent ,object : Callback{
                        override fun onClick() {

                        }

                        override fun onChangeValue(value: Int) {
                            createCardCreditViewModel.onChangeFlagCurrent(value)
                        }
                    }) }
                }
            }

        ButtonsFooterContent(
            btnTextAccept = "SALVAR",
            onClickAccept = {
                saveCreditCard()
            })
        }

    }
)

}

@Composable
fun ChoiceFlag(flagIdCurrent: Int, callback: Callback){
    val flagCollection = arrayOf(CardCreditFlag.MONEY.flag, CardCreditFlag.AMEX.flag, CardCreditFlag.MASTER.flag,
        CardCreditFlag.ELO.flag, CardCreditFlag.PAY_PAL.flag, CardCreditFlag.HIPER.flag, CardCreditFlag.VISA.flag)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 18.dp)){
        Text(text = "Bandeiras", fontFamily = LatoBold)
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)){
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                flagCollection.map{ flag ->
                    ItemFlag(flagIdCurrent, flag, callback)
                }
            }
        }

    }

}

@Composable
fun ItemFlag(flagIdCurrent: Int, flagId: Int, callback: Callback){
    var isFlagChoice = flagId == flagIdCurrent

    Card(elevation = 2.dp, shape = RoundedCornerShape(8.dp), backgroundColor = if(isFlagChoice) primary_dark else secondary_light,
        modifier = Modifier
            .width(45.dp)
            .height(30.dp)
            .clickable {
                isFlagChoice = true
                callback.onChangeValue(flagId)
            }){
        Image(
            painter = painterResource(id = flagId),
            contentDescription = null,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(lifecycleOwner: LifecycleOwner, cardCreditViewModel: CreateCardCreditFieldViewModel, hasToolbar: Boolean, callback: Callback) {
    var name: String by remember{ mutableStateOf("") }
    var nameCard: String by remember{ mutableStateOf("") }
    var isErrorName by remember{ mutableStateOf(false) }
    var isErrorNameCard by remember{ mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    cardCreditViewModel.name.observe(lifecycleOwner){
        name = it
    }

    cardCreditViewModel.nameCard.observe(lifecycleOwner){
        nameCard = it
    }

    cardCreditViewModel.isErrorName.observe(lifecycleOwner){
        isErrorName = it
    }

    cardCreditViewModel.isErrorNameCard.observe(lifecycleOwner){
        isErrorNameCard = it
    }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 28.dp), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputComponent(modifier = Modifier.fillMaxWidth(), isEnableClick = hasToolbar, value = name, label = "Titular", isMandatory = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ), error = isErrorName, customOnClick = object : CustomTextFieldOnClick {
            override fun onChangeValue(newValue: String) {
                cardCreditViewModel.onChangeName(newValue)
            }
        })

        TextInputComponent(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), value = nameCard, label = "Nome Cartão", isMandatory = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(
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
    var flagCurrent: MutableLiveData<Int> = MutableLiveData(CardCreditFlag.MONEY.flag)

    fun onChangeFlagCurrent(newFlagCurrent: Int) {
        flagCurrent.value = newFlagCurrent
    }

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