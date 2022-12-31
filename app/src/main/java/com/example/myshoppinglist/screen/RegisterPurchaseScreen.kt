package com.example.myshoppinglist.screen

import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackPurchase
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.enums.TypeState
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(navController: NavHostController?, idCardCurrent: Long) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val purchaseViewModel = PurchaseViewModel(context)
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val reset = remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val registerTextFieldViewModel: RegisterTextFieldViewModel = viewModel()
    val purchaseInfoCollection = remember { mutableStateListOf<PurchaseInfo>() }
    val countProduct = remember { mutableStateOf(0) }
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    LaunchedEffect(key1 = idCardCurrent) {
        categoryViewModel.getAll()
        registerTextFieldViewModel.onChangeIdCard(idCardCurrent)
    }

    registerTextFieldViewModel.purchaseCollection.observe(lifecycleOwner) {
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        purchaseInfoCollection.addAll(it)
        countProduct.value = purchaseInfoCollection.map { purchase ->
            purchase.purchaseCollection.map { item -> item }.count()
        }.sum()
    }

    registerTextFieldViewModel.resetDate.observe(lifecycleOwner) {
        reset.value = it
    }

    categoryViewModel.searchCollectionResult.observe(lifecycleOwner) {
        registerTextFieldViewModel.onChangeCategoryCollection(it)
    }

    suspend fun savePurchases() {

        val purcharseSaveCoroutine = coroutineScope.async {
            purchaseInfoCollection.map { purchaseInfo ->
                purchaseViewModel.insertPurchase(purchaseInfo.purchaseCollection)
            }
        }

        purcharseSaveCoroutine.await()
    }

    Box {

        BottomSheetScaffold(
            sheetBackgroundColor = background_card,
            sheetContent = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.94f)
                        .padding(top = 16.dp, bottom = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(6.dp),
                        backgroundColor = text_primary,
                        modifier = Modifier
                            .fillMaxWidth(.2f)
                            .height(5.dp)
                    ) {}
                    Spacer(Modifier.height(20.dp))
                    Row {
                        Text(
                            text = "Produtos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp, end = 16.dp)
                        )
                        Text(
                            text = "${countProduct.value}".padStart(3, '0'),
                            color = text_secondary,
                            modifier = Modifier
                                .drawBehind {
                                    drawCircle(
                                        color = text_primary,
                                        radius = this.size.minDimension
                                    )
                                },
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Divider(
                        color = text_primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    BoxProductRegisterComponent(
                        purchaseInfoCollection,
                        object : CallbackPurchase() {
                            override fun onChangeIndex(
                                indexInfo: Int,
                                index: Int,
                                typeState: TypeState
                            ) {
                                if (typeState == TypeState.EDIT) {
                                    val purchaseEdit =
                                        purchaseInfoCollection[indexInfo].purchaseCollection[index]
                                    registerTextFieldViewModel.updateData(
                                        purchaseEdit,
                                        index,
                                        indexInfo
                                    )
                                } else {
                                    registerTextFieldViewModel.removerPurchase(indexInfo, index)
                                }
                            }
                        })
                }
            },
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navController?.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = secondary_dark
                            )
                        }
                    },
                    backgroundColor = text_secondary,
                    elevation = 0.dp
                )
            },
            sheetPeekHeight = 140.dp,
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    TextInputComponent(
                        label = "Produto",
                        value = registerTextFieldViewModel.product.observeAsState().value!!,
                        reset = reset.value,
                        maxChar = 45,
                        isCountChar = true,
                        isMandatory = true,
                        error = registerTextFieldViewModel.productError.observeAsState().value,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                registerTextFieldViewModel.onChangeProduct(newValue)
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
                            value = registerTextFieldViewModel.price.observeAsState()!!.value,
                            reset = reset.value,
                            modifier = Modifier
                                .fillMaxWidth(0.45f)
                                .padding(end = 16.dp),
                            label = "Preço",
                            error = registerTextFieldViewModel.priceError.observeAsState().value,
                            customOnClick = object :
                                CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {
                                    registerTextFieldViewModel.onChangePrice(newValue)
                                }

                                override fun onChangeTypeProduct(newProduct: TypeProduct) {
                                    registerTextFieldViewModel.onChangeTypeProduct(newProduct)
                                }
                            })
                        BoxChoiceValue(registerTextFieldViewModel)
                    }

                    CategoryProduct(
                        registerTextFieldViewModel,
                        registerTextFieldViewModel.typeCategoryError.observeAsState().value
                    )

                    PurchaseAndPaymentComponent(
                        registerTextFieldViewModel,
                        registerTextFieldViewModel.idCardError.observeAsState().value
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 186.dp, end = 16.dp, top = 16.dp),
                        onClick = {
                            if (registerTextFieldViewModel.checkFileds()) {
                                registerTextFieldViewModel.addPurchase()
                                registerTextFieldViewModel.onChangeResetDate()
                            }
                        }) {
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

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, placeable.height / 2)
                    }
                }
                .padding(bottom = 52.dp)
                .background(text_secondary),
        ) {
            ButtonsFooterContent(
                isClickable = countProduct.value > 0,
                btnTextCancel = "CANCELAR",
                btnTextAccept = "SALVAR",
                onClickCancel = { navController?.popBackStack() },
                onClickAccept = {
                    coroutineScope.launch {
                        savePurchases()
                        navController!!.popBackStack()
                    }
                })
        }
    }

}

@Composable
fun CategoryProduct(
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    error: Boolean? = false
) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    val categoryCollections = remember { mutableStateListOf<Category>() }

    val categoryChoice = registerTextFieldViewModel.category.observeAsState().value

    val context = LocalContext.current


    registerTextFieldViewModel.categoryCollection.observe(lifecycleOwner) {
        categoryCollections.removeAll(categoryCollections)
        categoryCollections.addAll(it)
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Categorias",
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 4.dp),
                fontWeight = FontWeight.Bold
            )
            if (error!!) Icon(
                painter = painterResource(id = R.drawable.ic_baseline_error_24),
                modifier = Modifier.size(16.dp),
                contentDescription = null,
                tint = message_error
            )
        }
        LazyRow(modifier = Modifier.padding(start = 8.dp)) {
            items(categoryCollections) { category ->
                Card(modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable {
                        registerTextFieldViewModel.onChangeCategory(category.id)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .height(33.dp)
                            .background(if (category.id == categoryChoice) background_card_light else background_card),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconCategoryComponent(
                            iconCategory = AssetsUtils.readIconBitmapById(
                                context,
                                category.idImage
                            )!!
                                .asImageBitmap(),
                            colorIcon = Color(category.color),
                            size = 40.dp,
                            enableClick = true,
                            enabledBackground = false,
                            callback = object : Callback {
                                override fun onClick() {
                                    registerTextFieldViewModel.onChangeCategory(category.id)
                                }
                            }
                        )

                        Text(
                            text = category.category,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp, end = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun PurchaseAndPaymentComponent(
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    error: Boolean? = false
) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner.value)
    val cardCreditCollection =
        creditCardViewModel.searchCollectionResult.observeAsState(initial = listOf()).value
    val reset by registerTextFieldViewModel.resetDate.observeAsState(initial = false)
    creditCardViewModel.getAll()
    val isBlock = registerTextFieldViewModel.isBlock.observeAsState()
    val colorBackground = if (isBlock.value!!) text_primary.copy(alpha = 0.6f) else primary_dark

    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(6.dp),
        backgroundColor = colorBackground,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Local da Compra & Pagamento",
                    modifier = Modifier.padding(start = 16.dp)
                )

                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
                    onClick = { registerTextFieldViewModel.onChangeIsBlock(!isBlock.value!!) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_block_24),
                        contentDescription = null,
                        tint = if (isBlock.value!!) text_title_secondary else text_primary,
                    )
                }

            }
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextInputComponent(
                    textColor = text_primary,
                    isEnableClick = !isBlock.value!!,
                    backgroundColor = text_secondary_light,
                    label = "Local",
                    value = registerTextFieldViewModel.locale.observeAsState().value!!,
                    reset = reset && !isBlock.value!!,
                    modifier = Modifier.fillMaxWidth(.63f),
                    maxChar = 30,
                    isCountChar = true,
                    error = registerTextFieldViewModel.localeError.observeAsState().value,
                    customOnClick = object : CustomTextFieldOnClick {
                        override fun onChangeValue(newValue: String) {
                            registerTextFieldViewModel.onChangeLocale(newValue)
                        }

                    })
                DatePickerCustom(
                    registerTextFieldViewModel,
                    reset && !isBlock.value!!,
                    isBlock.value!!,
                    context
                )
            }
            CustomDropdownMenu(
                backgroundColor = if (isBlock.value!!) text_primary.copy(alpha = 0.6f) else background_text_field,
                idCardEditable = registerTextFieldViewModel.idCard.observeAsState().value,
                valueCollection = getNameCard(cardCreditCollection),
                error = error,
                isEnableClick = !isBlock.value!!,
                callback = object : CustomTextFieldOnClick {
                    override fun onChangeValueLong(newValue: Long) {
                        registerTextFieldViewModel.onChangeIdCard(newValue)
                    }
                }, reset = reset && !isBlock.value!!
            )
        }
    }

}

fun getNameCard(creditCardColelction: List<CreditCard>): HashMap<String, Long> {
    val cardCreditFormated: HashMap<String, Long> = HashMap<String, Long>()

    creditCardColelction.forEachIndexed { index, creditCard ->
        cardCreditFormated.put(
            creditCard.cardName,
            creditCard.id
        )
    }

    return cardCreditFormated.entries.sortedBy { it.value }
        .associate { it.toPair() } as HashMap<String, Long>

}

@Composable
fun CustomButton(callback: Callback, icon: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        backgroundColor = background_card
    ) {
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
fun DatePickerCustom(
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    reset: Boolean,
    isEnableClick: Boolean? = false,
    context: Context
) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val formatedDate = FormatUtils().getDateFormatted(formatPtBR = true)

    val date = remember { mutableStateOf(formatedDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = FormatUtils().getDateFormatted(dayOfMonth, month, year, true)
            registerTextFieldViewModel.onChangeDateCurrent(
                FormatUtils().getDateFormatted(
                    dayOfMonth,
                    month,
                    year
                )
            )
        },
        year,
        month,
        dayOfMonth
    )

    datePickerDialog.datePicker.maxDate = calendar.time.time

    LaunchedEffect(Unit) {
        registerTextFieldViewModel.onChangeDateCurrent(
            FormatUtils().getDateFormatted(
                dayOfMonth,
                month,
                year,
            )
        )
    }

    registerTextFieldViewModel.dateCurrent.observeForever {
        if (it.isNotBlank()) {
            date.value = FormatUtils().getDateFromatted(Date(it.toString().replace("-", "/")))
        }
    }

    TextInputComponent(
        backgroundColor = text_secondary_light,
        label = "Data da Compra",
        reset = reset,
        value = date.value,
        maxChar = 30,
        isEnableClick = false,
        modifier = Modifier.fillMaxWidth(.98f),
        customOnClick = object : CustomTextFieldOnClick {
            override fun onClick() {
                if (!isEnableClick!!) {
                    val splitedDate = date.value.split("/")
                    datePickerDialog.updateDate(
                        splitedDate[2].toInt(),
                        splitedDate[1].toInt() - 1,
                        splitedDate[0].toInt()
                    )
                    datePickerDialog.show()
                }
            }
        })
}

@ExperimentalComposeUiApi
@Composable
fun BoxChoiceValue(registerTextFieldViewModel: RegisterTextFieldViewModel) {
    var isMoney by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("") }
    var convertedValue = 0
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    var focusRequester by remember { mutableStateOf(FocusRequester()) }

    registerTextFieldViewModel.resetDate.observe(lifecycleOwner.value, {
        if (it) {
            value = ""
        }
    })

    registerTextFieldViewModel.quantOrKilo.observe(lifecycleOwner.value, {
        if (it != null) {
            value = it
        }
    })

    var customOnClick = object : CustomTextFieldOnClick {
        override fun onChangeValue(newValue: String) {
            value = newValue
            registerTextFieldViewModel.onChangeQuantOrKilo(newValue)
        }

        override fun onClick() {
            isMoney = !isMoney
        }

        override fun onChangeTypeProduct(newProduct: TypeProduct) {
            registerTextFieldViewModel.onChangeTypeProduct(newProduct)
        }
    }

    Card(
        elevation = 0.dp,
        modifier = Modifier.padding(top = 6.dp),
        backgroundColor = text_secondary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberInputComponent(
                maxChar = 11,
                hasIcon = true,
                value = value,
                focusRequester = focusRequester,
                error = registerTextFieldViewModel.quantOrKiloError.observeAsState().value,
                isMandatory = false,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .fillMaxWidth(0.79f),
                label = if (isMoney) "Quantidade" else "Quilograma",
                customOnClick = customOnClick
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp), verticalArrangement = Arrangement.SpaceBetween
            ) {
                CustomButton(
                    modifier = Modifier.padding(bottom = 4.dp),
                    callback = object : Callback {
                        override fun onClick() {
                            focusRequester.requestFocus()
                            convertedValue =
                                if (value.isBlank()) 0 else MaskUtils.replaceAll(value).toInt()
                            convertedValue += 1
                            value = convertedValue.toString()
                            registerTextFieldViewModel.onChangeQuantOrKilo(value)
                        }
                    },
                    icon = R.drawable.ic_baseline_add_24
                )

                CustomButton(callback = object : Callback {
                    override fun onClick() {
                        focusRequester.requestFocus()
                        convertedValue =
                            if (value.isBlank()) 0 else MaskUtils.replaceAll(value).toInt()
                        if (convertedValue > 0) {
                            convertedValue -= 1
                            value = convertedValue.toString()
                            registerTextFieldViewModel.onChangeQuantOrKilo(value)
                        }
                    }
                }, icon = R.drawable.ic_baseline_remove_24)
            }

        }
    }

}

class RegisterTextFieldViewModel : BaseFieldViewModel() {
    var product: MutableLiveData<String> = MutableLiveData("")
    var price: MutableLiveData<String> = MutableLiveData("")
    var quantOrKilo: MutableLiveData<String> = MutableLiveData("")
    var locale: MutableLiveData<String> = MutableLiveData("")
    var idCard: MutableLiveData<Long?> = MutableLiveData(-1)
    var dateCurrent: MutableLiveData<String> = MutableLiveData("")
    var category: MutableLiveData<Long> = MutableLiveData(null)
    var isBlock: MutableLiveData<Boolean> = MutableLiveData(false)
    var resetDate: MutableLiveData<Boolean> = MutableLiveData(false)
    var typeProduct: MutableLiveData<TypeProduct> = MutableLiveData(TypeProduct.QUANTITY)
    val purchaseCollection: MutableLiveData<MutableList<PurchaseInfo>> =
        MutableLiveData<MutableList<PurchaseInfo>>(mutableListOf())

    //variavel de error
    val productError: MutableLiveData<Boolean> = MutableLiveData(false)
    val priceError: MutableLiveData<Boolean> = MutableLiveData(false)
    val localeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val idCardError: MutableLiveData<Boolean> = MutableLiveData(false)
    val typeCategoryError: MutableLiveData<Boolean> = MutableLiveData(false)
    val quantOrKiloError: MutableLiveData<Boolean> = MutableLiveData(false)

    private val index: MutableLiveData<Int> = MutableLiveData(-1)
    private val indexInfo: MutableLiveData<Int> = MutableLiveData(-1)
    val categoryCollection = MutableLiveData<MutableList<Category>>(mutableListOf())

    fun updateData(purchase: Purchase, newIndex: Int, newIndexInfo: Int) {
        product.value = purchase.name
        price.value = purchase.price.toString()
        category.value = purchase.categoryOwnerId
        typeProduct.value = purchase.typeProduct
        locale.value = purchase.locale
        dateCurrent.value = purchase.date
        idCard.value = purchase.purchaseCardId
        quantOrKilo.value = purchase.quantiOrKilo

        index.value = newIndex
        indexInfo.value = newIndexInfo
    }

    override fun checkFileds(): Boolean {

        productError.value = product.value!!.isBlank()

        priceError.value = price.value!!.isBlank()

        localeError.value = locale.value!!.isBlank()

        idCardError.value = idCard.value == -1L

        typeCategoryError.value = category.value == null

        quantOrKiloError.value =
            quantOrKilo.value!!.isBlank() || MaskUtils.replaceAll(quantOrKilo.value!!).toInt() == 0

        if (product.value!!.isBlank()) return false

        if (price.value!!.isBlank()) return false

        if (quantOrKilo.value!!.isBlank()) return false

        if (locale.value!!.isBlank()) return false

        if (idCard.value == -1L) return false

        if (category.value == null) return false

        if (quantOrKilo.value!!.isBlank() || MaskUtils.replaceAll(quantOrKilo.value!!)
                .toInt() == 0
        ) return false

        return true
    }

    fun removerPurchase(indexInfo: Int, index: Int) {
        purchaseCollection.value?.get(indexInfo)?.purchaseCollection?.removeAt(index)
        if (purchaseCollection.value?.get(indexInfo)?.purchaseCollection?.size == 0) {
            purchaseCollection.value?.removeAt(indexInfo)
        }
    }

    fun onChangeCategoryCollection(newCategoryCollection: List<Category>) {
        categoryCollection.value?.removeAll(newCategoryCollection)
        categoryCollection.value?.addAll(newCategoryCollection)
    }

    fun onChangeResetDate() {
        resetDate.value = true

        product.value = ""
        price.value = ""
        category.value = null
        index.value = -1
        indexInfo.value = -1
        quantOrKilo.value = ""

        if (!isBlock.value!!) {
            locale.value = ""
            dateCurrent.value = FormatUtils().getDateFormatted(formatPtBR = false)
        }

        Handler().postDelayed({
            kotlin.run {
                resetDate.value = false
            }
        }, 200)
    }

    fun onChangeCategory(newCategoty: Long) {
        category.value = newCategoty
        typeCategoryError.value = false

    }

    fun onChangeTypeProduct(newTypeProduct: TypeProduct) {
        typeProduct.value = newTypeProduct
    }

    fun addPurchase() {

        val purchase = Purchase(
            product.value!!,
            locale.value!!,
            idCard.value!!,
            quantOrKilo.value!!,
            typeProduct.value!!,
            dateCurrent.value!!,
            MaskUtils.convertValueStringToDouble(
                price.value!!
            ),
            category.value!!
        )

        if (index.value != -1 && indexInfo.value != -1 && purchaseCollection.value!!.size > 0) {
            purchaseCollection.value!![indexInfo.value!!].purchaseCollection[index.value!!] =
                purchase

        } else {
            if (purchaseCollection.value != null && purchaseCollection.value!!.isNotEmpty() && purchaseCollection.value!!.size > 0) {
                val indexCurrent =
                    purchaseCollection.value!!.indexOfFirst { it.title.equals(locale.value!!) }
                if (indexCurrent != -1) {
                    purchaseCollection.value!!.get(indexCurrent).purchaseCollection.add(purchase)
                } else {
                    var newPurchaseCollection = purchaseCollection.value
                    newPurchaseCollection!!.add(
                        PurchaseInfo(
                            purchase.locale,
                            mutableListOf(purchase)
                        )
                    )
                    purchaseCollection.value = newPurchaseCollection
                }

            } else {
                purchaseCollection.value =
                    mutableListOf(PurchaseInfo(purchase.locale, mutableListOf(purchase)))
            }
        }


    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
        productError.value = newProduct.isBlank()
    }

    fun onChangePrice(newPrice: String) {
        price.value = newPrice
    }

    fun onChangeQuantOrKilo(newQuantOrKilo: String) {
        quantOrKilo.value = newQuantOrKilo
        quantOrKiloError.value =
            newQuantOrKilo.isBlank() || MaskUtils.replaceAll(newQuantOrKilo).toInt() == 0
    }

    fun onChangeLocale(newLocale: String) {
        locale.value = newLocale
        localeError.value = newLocale.isBlank()
    }

    fun onChangeIdCard(newIdCard: Long?) {
        idCard.value = newIdCard
        idCardError.value = newIdCard == -1L
    }

    fun onChangeIsBlock(newIsBlock: Boolean) {
        isBlock.value = newIsBlock
    }

    fun onChangeDateCurrent(newDateCurrent: String) {
        dateCurrent.value = newDateCurrent
    }

}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PreviewRegisterPurchaseScreen() {
//    RegisterPurchaseScreen(null)
}