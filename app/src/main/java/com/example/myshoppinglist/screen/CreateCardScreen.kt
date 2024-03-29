package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.viewModels.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.services.CreditCardService
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.services.repository.CreditCardRepository
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.services.viewModel.CreditCardViewModel
import com.example.myshoppinglist.utils.ConversionUtils
import com.example.myshoppinglist.utils.MeasureTimeService

@ExperimentalComposeUiApi
@Composable
fun CreateCardScreen(
    navController: NavController?,
    hasToolbar: Boolean,
    isUpdate: Boolean = false,
    holderNameUser: String,
    creditCardDTOJson: String
) {
    val LOG = "CREATE_CARD_SCREEN"
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModelDB = CreditCardViewModelDB(context, lifecycleOwner)
    val holderName: String by createCardCreditViewModel.name.observeAsState(initial = holderNameUser)
    val nameCard: String by createCardCreditViewModel.nameCard.observeAsState(initial = "")
    val colorCurrent: Color by createCardCreditViewModel.colorCurrent.observeAsState(initial = card_red_light)
    val flagCurrent: Int by createCardCreditViewModel.flagCurrent.observeAsState(initial = CardCreditFlag.MASTER.flagBlack)
    val userDTO by createCardCreditViewModel.getUser(context).observeAsState(initial = UserDTO())

    val creditCardViewModel = CreditCardViewModel(
        CreditCardRepository(CreditCardService.getCreditCardService()),
        creditCardViewModelDB
    )

    var visibleWaiting by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    val callback = object : CallbackObject<CreditCardDTO> {
        override fun onSuccess() {

            if (!isUpdate) {
                if (hasToolbar) {
                    navController?.navigate(Screen.Home.name) {
                        popUpTo(Screen.Home.name) { inclusive = false }
                    }
                } else {
                    navController?.popBackStack()
                }
            } else {
                navController?.navigate("${Screen.SettingsScreen.name}?idAvatar=${userDTO.idAvatar}?nickName=${userDTO.nickName}")
                {
                    popUpTo(Screen.Home.name) { inclusive = false }
                }
            }
            visibleWaiting = false

        }

        override fun onFailed(messageError: String) {

        }

        override fun onClick() {
            visibleWaiting = false
        }

        override fun onChangeValue(newValue: Boolean) {
            visibleWaiting = true
        }

        override fun onChangeValue(newValue: String) {
            messageError = newValue
        }
    }

    LaunchedEffect(key1 = holderNameUser) {
        createCardCreditViewModel.onChangeName(holderNameUser)
    }

    LaunchedEffect(key1 = creditCardDTOJson) {

        if (creditCardDTOJson.isNotBlank()) {
            val creditCardDTO =
                ConversionUtils<CreditCardDTODB>(CreditCardDTODB::class.java).fromJsonList(
                    creditCardDTOJson
                )!![0]

            createCardCreditViewModel.onChangeName(creditCardDTO.holderName)
            createCardCreditViewModel.onChangeNameCard(creditCardDTO.cardName)
            createCardCreditViewModel.onChangeValue(creditCardDTO.value)
            createCardCreditViewModel.onChangeColorCurrent(Color(creditCardDTO.colorCard))
            createCardCreditViewModel.onChangeFlagCurrent(creditCardDTO.flagBlack)
            createCardCreditViewModel.onChangeTypeCard(creditCardDTO.typeCard)
            createCardCreditViewModel.onChangeIdCreditCard(creditCardDTO.myShoppingId)
            createCardCreditViewModel.onChangeLastPosition(creditCardDTO.position)
            createCardCreditViewModel.onChangeIdCardApi(creditCardDTO.idMyShoppingApi)
            createCardCreditViewModel.onChangeDayClosedInvoice(creditCardDTO.dayClosedInvoice)
        }
    }

    fun saveCreditCard() {
        if (createCardCreditViewModel.checkFields()) {
            val lastPosition = createCardCreditViewModel.lastPosition.value
            val valueCreditCard = createCardCreditViewModel.value.value
            val typeCardRecover = createCardCreditViewModel.typeCard.value
            val idCreditCard = createCardCreditViewModel.idCreditCard.value
            val idCreditCardApi = createCardCreditViewModel.idCardApi.value
            val dayClosedInvoice = createCardCreditViewModel.dayClosedInvoice.value

            val creditCardDTO = CreditCardDTO(
                idCreditCardApi!!,
                0,
                holderName.trim(),
                nameCard.trim(),
                valueCreditCard!!,
                colorCurrent.toArgb(),
                typeCardRecover?.ordinal!!,
                userDTO,
                flagCurrent,
                lastPosition!!,
                dayClosedInvoice!!
            )


            if (!isUpdate) {
                creditCardViewModel.save(creditCardDTO, callback)
            } else {
                creditCardDTO.idCard = idCreditCard!!

                creditCardViewModel.update(
                    creditCardDTO,
                    callback
                )
            }
        }
    }

    TopAppBarScreen(
        enableScroll = true,
        hasBackButton = !hasToolbar,
        hasDoneButton = hasToolbar,
        onClickIcon = { navController?.popBackStack() },
        onClickIconDone = { saveCreditCard() },
        hasToolbar = hasToolbar,
        content = {

            WaitingProcessComponent(visibleWaiting, messageError, callback)

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column() {
                    CardCreditComponent(
                        isClicable = false,
                        isDefault = false,
                        typeCard = TypeCard.CREDIT,
                        isChoiceColor = true,
                        cardCreditDTO = CreditCardDTODB(
                            colorCard = card_blue.toArgb(),
                            value = 0F,
                            cardName = nameCard,
                            holderName = holderName
                        ),
                        flagCardCredit = flagCurrent,
                        createCardCreditViewModel = createCardCreditViewModel,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 26.dp),
                        callbackColor = object :
                            CallbackColor {
                            override fun onChangeValue(color: Color) {
                                createCardCreditViewModel.onChangeColorCurrent(color)
                            }
                        }
                    )
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Dados do Cartão", fontFamily = LatoBold)
                            Divider(color = divider, modifier = Modifier.padding(top = 8.dp))
                        }
                        TextFieldContent(
                            lifecycleOwner,
                            createCardCreditViewModel,
                            hasToolbar,
                            object : Callback {
                                override fun onClick() {
                                    saveCreditCard()
                                }
                            })
                        ChoiceFlag(flagCurrent, object : Callback {
                            override fun onClick() {

                            }

                            override fun onChangeValue(value: Int) {
                                createCardCreditViewModel.onChangeFlagCurrent(value)
                            }
                        })

                    }
                }

                DayClosedInvoiceComponent(createCardCreditViewModel.dayClosedInvoice.observeAsState().value, object : Callback {
                    override fun onChangeValue(value: Int) {
                        createCardCreditViewModel.onChangeDayClosedInvoice(value)
                    }

                })

                if (!hasToolbar) {
                    ButtonsFooterContent(
                        modifierButton = Modifier.padding(top = 26.dp),
                        btnTextAccept = "SALVAR",
                        onClickAccept = {
                            saveCreditCard()
                        })
                }
            }

        }
    )

}

@Composable
fun ChoiceFlag(flagIdCurrent: Int, callback: Callback) {
    val flagCollection = arrayOf(
        CardCreditFlag.AMEX.flagBlack,
        CardCreditFlag.MASTER.flagBlack,
        CardCreditFlag.ELO.flagBlack,
        CardCreditFlag.PAY_PAL.flagBlack,
        CardCreditFlag.HIPER.flagBlack,
        CardCreditFlag.VISA.flagBlack
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 6.dp)
    ) {
        Text(text = "Bandeiras", fontFamily = LatoBold)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                flagCollection.map { flag ->
                    ItemFlag(flagIdCurrent, flag, callback)
                }
            }
        }

    }

}

@Composable
fun ItemFlag(flagIdCurrent: Int, flagId: Int, callback: Callback) {
    var isFlagChoice = flagId == flagIdCurrent

    Card(elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = if (isFlagChoice) primary_dark else secondary_light,
        modifier = Modifier
            .width(45.dp)
            .height(30.dp)
            .clickable {
                isFlagChoice = true
                callback.onChangeValue(flagId)
            }) {
        Image(
            painter = painterResource(id = flagId),
            contentDescription = null,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(
    lifecycleOwner: LifecycleOwner,
    cardCreditViewModel: CreateCardCreditFieldViewModel,
    isEnableClick: Boolean,
    callback: Callback
) {
    var name: String by remember { mutableStateOf("") }
    var nameCard: String by remember { mutableStateOf("") }
    var isErrorName by remember { mutableStateOf(false) }
    var isErrorNameCard by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    cardCreditViewModel.name.observe(lifecycleOwner) {
        name = it
    }

    cardCreditViewModel.nameCard.observe(lifecycleOwner) {
        nameCard = it
    }

    cardCreditViewModel.isErrorName.observe(lifecycleOwner) {
        isErrorName = it
    }

    cardCreditViewModel.isErrorNameCard.observe(lifecycleOwner) {
        isErrorNameCard = it
    }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 28.dp), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputComponent(modifier = Modifier.fillMaxWidth(),
            isEnableClick = isEnableClick,
            value = name,
            label = "Titular",
            isMandatory = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            error = isErrorName,
            customOnClick = object : CustomTextFieldOnClick {
                override fun onChangeValue(newValue: String) {
                    cardCreditViewModel.onChangeName(newValue)
                }
            })

        TextInputComponent(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            value = nameCard,
            label = "Nome Cartão",
            isMandatory = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onNext = {
                    callback.onClick()
                }
            ),
            error = isErrorNameCard,
            customOnClick = object : CustomTextFieldOnClick {
                override fun onChangeValue(newValue: String) {
                    cardCreditViewModel.onChangeNameCard(newValue)
                }
            })
    }

}