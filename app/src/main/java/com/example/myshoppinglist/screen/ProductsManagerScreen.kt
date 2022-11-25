package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.callback.CallbackFilter
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.CustomerChip
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import java.util.*
import kotlin.collections.HashMap

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(navController: NavController?) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val productManagerFieldViewModel: ProductManagerFieldViewModel = viewModel()
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner.value)

    LaunchedEffect(Unit) {
        creditCardViewModel.getAll()
    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner.value) { creditCardCollection ->
        productManagerFieldViewModel.onChangeCardCreditCollection(creditCardCollection.map { creditCard ->
            CreditCardDTO().fromCreditCardDTO(
                creditCard
            )
        })
    }

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, content = {

        Column(modifier = Modifier.padding(12.dp)) {
            SearchProduct(context, lifecycleOwner.value, productManagerFieldViewModel)
        }
    })
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchProduct(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    productManagerFieldViewModel: ProductManagerFieldViewModel
) {
    val product = remember { mutableStateOf("") }
    val listProductText = remember { mutableStateListOf<String>() }
    val reset = remember { mutableStateOf(false) }
    var enableDialog by remember { mutableStateOf(false) }
    val creditCardDTOCollection = remember { mutableListOf<CreditCardDTO>() }
    var filter by remember { mutableStateOf(ObjectFilter())}

    productManagerFieldViewModel.product.observe(lifecycleOwner) {
        if (it.isNotBlank() && reset.value) {
            reset.value = false
        }
        product.value = it
    }

    productManagerFieldViewModel.cardCreditCollection.observe(lifecycleOwner) {
        creditCardDTOCollection.removeAll(creditCardDTOCollection)
        creditCardDTOCollection.addAll(it)
    }

    Card(
        backgroundColor = background_card_gray_light, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.13f),
        shape = RoundedCornerShape(8.dp)
    ) {
        AlertDialogFilter(enableDialog, filter, creditCardDTOCollection, lifecycleOwner,
            object : CallbackFilter {
                override fun onClick() {
                    enableDialog = false
                }

                override fun onChangeObjectFilter(value: ObjectFilter) {

                    filter.categoryCollection.forEach { productCategory ->
                        listProductText.remove(productCategory)
                    }

                    value.categoryCollection.forEach { category ->
                        listProductText.add(category)
                    }

                    val formarFilterMin = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(filter.priceMin.toDouble()))
                    val formarFilterMax = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(filter.priceMax.toDouble()))

                    listProductText.remove("$formarFilterMin á $formarFilterMax")

                    val formarValueMin = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(value.priceMin.toDouble()))
                    val formarValueMax = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(value.priceMax.toDouble()))

                    listProductText.add("$formarValueMin á $formarValueMax")

                    listProductText.remove(filter.month)

                    listProductText.add(value.month)

                    filter = value
                }
            })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Card(
                backgroundColor = background_card_light,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.53f),
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextInputComponent(modifier = Modifier
                        .fillMaxWidth(.85f)
                        .fillMaxHeight(),
                        value = product.value,
                        reset = reset.value,
                        label = "Produto",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = text_primary,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                listProductText.add(product.value)
                                reset.value = true
                                productManagerFieldViewModel.onChangeProduct("")
                            }
                        ),
                        error = false,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                productManagerFieldViewModel.onChangeProduct(newValue)
                            }
                        })

                    IconButton(
                        onClick = {
                            enableDialog = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_filter_alt_24),
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                }
            }

            LazyRow(modifier = Modifier.padding(top = 4.dp)) {
                items(listProductText) { productItem ->
                    CustomerChip(label = productItem, callback = object : Callback {
                        override fun onClick() {
                            listProductText.remove(productItem)
                        }
                    })
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AlertDialogFilter(
    enableDialog: Boolean,
    filter: ObjectFilter,
    cardCreditCollection: List<CreditCardDTO>,
    lifecycleOwner: LifecycleOwner,
    callback: CallbackFilter
) {
    val categoryChoices = remember { mutableStateListOf<String>() }
    var priceMin by remember { mutableStateOf(filter.priceMin) }
    var priceMax by remember { mutableStateOf(filter.priceMax) }
    var month by remember { mutableStateOf("") }
    var idCardCredit by remember { mutableStateOf(if (cardCreditCollection.isNotEmpty()) cardCreditCollection[0].idCard else 0) }

    var categoryCollections = listOf(
        TypeCategory.HYGIENE,
        TypeCategory.CLEARNING,
        TypeCategory.FOOD,
        TypeCategory.DRINKS,
        TypeCategory.OTHERS
    )

    LaunchedEffect(Unit){
        categoryChoices.addAll(filter.categoryCollection)
        month = filter.month
        idCardCredit = filter.idCard
    }

    if (enableDialog) {
        Dialog(onDismissRequest = { }) {
            Card(elevation = 0.dp, shape = RoundedCornerShape(8.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(.85f)
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp)
                ) {
                    Text(text = "Filtros", fontFamily = LatoBold, fontSize = 24.sp)
                    Divider(
                        color = divider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    Spacer(Modifier.size(44.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxHeight(.2f)
                            .fillMaxWidth(),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxHeight(.4f)
                                    .fillMaxWidth(),
                                elevation = 0.dp,
                                backgroundColor = background_card_light,
                                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Categorias:",
                                        fontFamily = LatoBold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                            LazyRow(
                                modifier = Modifier
                                    .background(background_card_gray_light)
                                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(categoryCollections) { category ->
                                    val isChoiceCurrent = !categoryChoices.find { categoryChoice ->
                                        categoryChoice == category.category
                                    }.isNullOrBlank()
                                    CustomerChip(
                                        paddingVertical = 0.dp,
                                        label = category.category,
                                        iconId = category.imageCircle,
                                        isEnabled = true,
                                        isChoice = isChoiceCurrent,
                                        color = primary_dark,
                                        callback = object : Callback {
                                            override fun onClick() {
                                                val isChoiceOld =
                                                    !categoryChoices.find { categoryChoice ->
                                                        categoryChoice.equals(category.category)
                                                    }.isNullOrBlank()
                                                if (isChoiceOld) {
                                                    categoryChoices.remove(category.category)
                                                } else {
                                                    categoryChoices.add(category.category)
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    }
                    ChoicePrice(priceMin, priceMax, callbackMin = object : CustomTextFieldOnClick {
                        override fun onChangeValueFloat(newValue: Float) {
                            priceMin = newValue
                        }
                    }, callbackMax = object : CustomTextFieldOnClick {
                        override fun onChangeValueFloat(newValue: Float) {
                            priceMax = newValue
                        }
                    })

                    ChoiceCard(filter.idCard, cardCreditCollection, object : CallbackCreditCard {
                        override fun onChangeIdCard(idCard: Long) {
                            idCardCredit = idCard
                        }
                    })

                    ChoiceData(idCardCredit, filter.month, lifecycleOwner, object : Callback {
                        override fun onChangeValueMong(newMonth: String) {
                            month = newMonth
                        }
                    })

                    Column(modifier = Modifier.padding(top = 26.dp)) {
                        ButtonsFooterContent(
                            btnTextCancel = "CANCELAR",
                            onClickCancel = { callback.onClick() },
                            btnTextAccept = "SALVAR",
                            onClickAccept = {
                                val saveFilter = ObjectFilter(
                                    categoryChoices,
                                    priceMin,
                                    priceMax,
                                    idCardCredit,
                                    month
                                )
                                callback.onChangeObjectFilter(saveFilter)
                                callback.onClick()
                            })
                    }

                }
            }

        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ChoicePrice(
    priceMin: Float,
    priceMax: Float,
    maxStepDefault: Float = 100.0f,
    callbackMin: CustomTextFieldOnClick,
    callbackMax: CustomTextFieldOnClick
) {
    var maxStepCurrent by remember { mutableStateOf(maxStepDefault) }
    var sliderPosition by remember { mutableStateOf(priceMin..if(priceMax > 0f) priceMax else maxStepDefault) }
    var valueMin by remember { mutableStateOf(priceMin) }
    var valueMax by remember { mutableStateOf(if(priceMax > 0f) priceMax else maxStepDefault) }
    var enableEditMaxStep by remember { mutableStateOf(false) }
    var maxStep by remember {
        mutableStateOf(
            MaskUtils.maskValue(
                String.format(
                    "%.2f",
                    maxStepDefault
                )
            )
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        Spacer(Modifier.size(24.dp))

        Text(text = "Preço:", fontFamily = LatoBlack)

        Column(
            modifier = Modifier
                .fillMaxHeight(.2f)
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            RangeSlider(
                enabled = !enableEditMaxStep,
                values = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    valueMin = sliderPosition.start
                    valueMax = sliderPosition.endInclusive

                    callbackMin.onChangeValueFloat(valueMin)
                    callbackMax.onChangeValueFloat(valueMax)

                },
                valueRange = 0f..maxStepCurrent,
                onValueChangeFinished = {

                },
                colors = SliderDefaults.colors(
                    thumbColor = card_orange,
                    activeTrackColor = card_green
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(valueMin.toDouble())))

                if (enableEditMaxStep) {
                    Column(modifier = Modifier.fillMaxWidth(.2f)) {
                        BasicTextField(
                            cursorBrush = SolidColor(primary),
                            value = TextFieldValue(
                                text = maxStep,
                                selection = TextRange(maxStep.length)
                            ),
                            onValueChange = {
                                var text = it.text
                                if (text.length < 10) {
                                    val value = if (text.isNotBlank()) {
                                        text
                                    } else {
                                        "0"
                                    }
                                    maxStep = MaskUtils.maskValue(value)
                                }
                            },
                            textStyle = TextStyle(color = text_primary, textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            decorationBox = { innerTextField ->
                                innerTextField()
                            },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val maskValueToNumber =
                                        maxStep.replace(".", "").replace(",", ".").toFloat()
                                    maxStepCurrent = maskValueToNumber
                                    sliderPosition = 0f..maskValueToNumber
                                    valueMin = sliderPosition.start
                                    valueMax = sliderPosition.endInclusive

                                    callbackMin.onChangeValueFloat(valueMin)
                                    callbackMax.onChangeValueFloat(valueMax)

                                    enableEditMaxStep = false
                                    keyboardController?.hide()
                                }
                            ),
                        )

                        Divider(
                            color = primary_dark,
                            modifier = Modifier
                                .height(2.dp)
                        )
                    }
                }

                if (!enableEditMaxStep) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    valueMax.toDouble()
                                )
                            )
                        )

                        IconButton(
                            onClick = {
                                enableEditMaxStep = true
                                keyboardController?.hide()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = null,
                                tint = primary_light,
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ChoiceCard(cardCurrent: Long, cardCreditCollection: List<CreditCardDTO>, callbackCard: CallbackCreditCard) {
    var cardCreditChoice by remember { mutableStateOf(CreditCardDTO(idCard = cardCurrent)) }

    Column {
        Spacer(Modifier.size(24.dp))

        Text(text = "Cartões:", fontFamily = LatoBlack)

        Column(
            modifier = Modifier
                .fillMaxHeight(.2f)
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                items(cardCreditCollection) { cardCredit ->
                    val isChoiceCurrent = cardCreditChoice.idCard == cardCredit.idCard

                    CustomerChip(
                        paddingVertical = 0.dp,
                        label = cardCredit.cardName,
                        iconId = cardCredit.flag,
                        isEnabled = true,
                        isBackgroundCircle = true,
                        isChoice = isChoiceCurrent,
                        color = primary_dark,
                        callback = object : Callback {
                            override fun onClick() {
                                cardCreditChoice = cardCredit
                                callbackCard.onChangeIdCard(cardCreditChoice.idCard)
                            }
                        })
                }
            }

        }

    }

}

@ExperimentalMaterialApi
@Composable
fun ChoiceData(idCard: Long, dataCurrent: String, lifecycleOwner: LifecycleOwner, callback: Callback) {
    val context = LocalContext.current
    val purchaseViewModel = PurchaseViewModel(context)
    var expanded by remember { mutableStateOf(false) }
    var splitDataCurrent = if(dataCurrent.isNotBlank()) dataCurrent.split("-") else listOf()
    var yearCurrent by remember { mutableStateOf(if(splitDataCurrent.isNotEmpty()) splitDataCurrent[0] else "")}
    var monthCurrent by remember { mutableStateOf(if(splitDataCurrent.isNotEmpty()) splitDataCurrent[1] else "")}
    val dataCollection = remember { mutableStateListOf<String>() }
    val dateMonthAndYear = remember { mutableStateMapOf<String, String>() }

    fun splitMonthOfYead(): HashMap<String, String> {
        val mapDate = HashMap<String, String>()
        dataCollection.forEach {
            val splitDate = it.split("-")
            val year = splitDate[0]
            val month = splitDate[1]

            mapDate[year] = month
        }

        return mapDate
    }

    LaunchedEffect(key1 = idCard) {
        purchaseViewModel.getMonthDistinctByIdCard(idCard)
    }

    purchaseViewModel.searchResultMonths.observe(lifecycleOwner) { it ->
        dataCollection.removeAll(dataCollection)
        dataCollection.addAll(it)

        dateMonthAndYear.entries.forEach { date -> dateMonthAndYear.remove(date.key) }

        dateMonthAndYear.putAll(splitMonthOfYead())
        yearCurrent = ""
        monthCurrent = ""

        if (it.isNotEmpty()) {
            yearCurrent = dateMonthAndYear.keys.toList()[0]
            monthCurrent = if(splitDataCurrent.isNotEmpty()) splitDataCurrent[1] else ""
        }
    }

    Column {
        Spacer(Modifier.size(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Data:", fontFamily = LatoBlack)

            Card(
                elevation = 0.dp, modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable(onClick = { expanded = true })
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = yearCurrent,
                        color = text_primary,
                        fontSize = 12.sp
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = text_primary,
                    )

                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    dateMonthAndYear.keys.forEach { yearDate ->
                        DropdownMenuItem(modifier = Modifier.height(25.dp), onClick = {
                            expanded = false
                            yearCurrent = yearDate
                        }) {
                            Text(text = yearDate, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(.27f)
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                items(dateMonthAndYear.values.toList()) { month ->
                    val isChoiceCurrent = monthCurrent == month
                    CustomerChip(
                        paddingVertical = 0.dp,
                        label = FormatUtils().getNameMonth(month)
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        isEnabled = true,
                        isBackgroundCircle = true,
                        isChoice = isChoiceCurrent,
                        color = primary_dark,
                        callback = object : Callback {
                            override fun onClick() {
                                if (monthCurrent == month) {
                                    monthCurrent = ""
                                } else {
                                    monthCurrent = month

                                    callback.onChangeValueMong("$yearCurrent-$monthCurrent")
                                }

                            }
                        })
                }
            }

        }

    }
}

class ProductManagerFieldViewModel : BaseFieldViewModel() {
    val product: MutableLiveData<String> = MutableLiveData("")
    var cardCreditCollection: MutableLiveData<List<CreditCardDTO>> =
        MutableLiveData(mutableListOf())

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
    }

    fun onChangeCardCreditCollection(newCardCreditCollection: List<CreditCardDTO>) {
        cardCreditCollection.value = newCardCreditCollection
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}