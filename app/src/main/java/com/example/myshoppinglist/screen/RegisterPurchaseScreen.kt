package com.example.myshoppinglist.screen

import android.os.Handler
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackPurchase
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(navController: NavHostController?, idCardCurrent: Long, isEditable: Boolean? = false, purchaseEdit: Purchase? = null) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val purchaseViewModel = PurchaseViewModel(context)
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val reset = remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val registerTextFieldViewModel: RegisterTextFieldViewModel = viewModel()
    val purchaseInfoCollection =
        registerTextFieldViewModel.purchaseInfoCollection.observeAsState(initial = mutableListOf()).value
    var countProduct by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var visibilityBackHandler by remember { mutableStateOf(false) }
    var isCheck by remember {mutableStateOf(false)}

    LaunchedEffect(key1 = idCardCurrent) {
        categoryViewModel.getAll()
        registerTextFieldViewModel.onChangeIdCard(idCardCurrent)
    }

    LaunchedEffect(key1 = purchaseEdit){
        if(purchaseEdit != null){
            countProduct = 1

            val price = if(purchaseEdit.typeProduct == TypeProduct.KILO) MaskUtils.maskKiloGram(MaskUtils.replaceAll(purchaseEdit.price.toString())) else MaskUtils.maskQuantity(MaskUtils.convertValueDoubleToString(purchaseEdit.price))

            registerTextFieldViewModel.onChangeIdCard(purchaseEdit.purchaseCardId)
            registerTextFieldViewModel.onChangeCategory(purchaseEdit.categoryOwnerId)
            registerTextFieldViewModel.onChangeLocale(purchaseEdit.locale)
            registerTextFieldViewModel.onChangeDateCurrent(purchaseEdit.date)
            registerTextFieldViewModel.onChangePrice(price)
            registerTextFieldViewModel.onChangeProduct(purchaseEdit.name)
            registerTextFieldViewModel.onChangeQuantOrKilo(purchaseEdit.quantiOrKilo)
            registerTextFieldViewModel.onChangeTypeProduct(purchaseEdit.typeProduct)
            if(purchaseEdit.discount > 0){
                registerTextFieldViewModel.onChangeDiscountCurrent(MaskUtils.maskQuantity(MaskUtils.convertValueDoubleToString(purchaseEdit.discount)))
                isCheck = true
            }
        }
    }

    registerTextFieldViewModel.countProduct.observe(lifecycleOwner) {
        countProduct = it
    }

    registerTextFieldViewModel.resetDate.observe(lifecycleOwner) {
        reset.value = it
    }

    categoryViewModel.searchCollectionResult.observe(lifecycleOwner) {
        registerTextFieldViewModel.onChangeCategoryCollection(it)
    }

   suspend fun updatePurchase(){

       withContext(coroutineScope.coroutineContext) {
           val purchase = Purchase(
               registerTextFieldViewModel.product.value!!,
               registerTextFieldViewModel.locale.value!!,
               registerTextFieldViewModel.idCard.value!!,
               registerTextFieldViewModel.quantOrKilo.value!!,
               registerTextFieldViewModel.typeProduct.value!!,
               registerTextFieldViewModel.dateCurrent.value!!,
               MaskUtils.convertValueStringToDouble(
                   MaskUtils.maskValue(registerTextFieldViewModel.price.value!!)
               ),
               registerTextFieldViewModel.category.value!!,
               registerTextFieldViewModel.email
           )

           purchase.id = purchaseEdit?.id ?: 0

           purchaseViewModel.updatePurchase(purchase)

       }
   }

    suspend fun savePurchases() {

        val purchaseSaveCoroutine = coroutineScope.async {
            purchaseInfoCollection.map { purchaseInfo ->
                purchaseViewModel.insertPurchase(purchaseInfo.purchaseCollection.map { it.purchase }
                    .toList())
            }
        }

        purchaseSaveCoroutine.await()
    }

    BackHandler {
        visibilityBackHandler = true
    }

    DialogBackCustom(visibilityBackHandler, {
        visibilityBackHandler = false
        navController?.popBackStack()
    }, {
        visibilityBackHandler = false
    }, "Sair", "Os dados adicionados serão perdidos!\nTem certeza que deseja sair?")

    Box {

        BottomSheetScaffold(
            sheetBackgroundColor = if(!isEditable!!){
                background_card
            }else {
                text_primary
            },
            sheetContent = {
                if(!isEditable!!){
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
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
                                text = "$countProduct".padStart(3, '0'),
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
                            context,
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
                                            purchaseEdit.purchase,
                                            index,
                                            indexInfo
                                        )

                                        if(purchaseEdit.purchase.discount > 0){
                                            isCheck = true
                                        }
                                    } else {
                                        registerTextFieldViewModel.removerPurchase(indexInfo, index)
                                    }
                                    coroutineScope.launch {
                                        scaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            })
                    }
                }

            },
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        IconButton(onClick = {
                            if (countProduct > 0 ) {
                                coroutineScope.launch {
                                    if(isEditable){
                                        updatePurchase()
                                    }else{
                                        savePurchases()
                                    }

                                    navController!!.popBackStack()
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_done_24),
                                contentDescription = null,
                                tint = primary
                            )
                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = { visibilityBackHandler = true }) {
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
            sheetPeekHeight = 78.dp,
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
                    Column(modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                            Checkbox(
                                colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                                checked = isCheck,
                                onCheckedChange = {
                                    isCheck = !isCheck

                                    if(!isCheck){
                                        registerTextFieldViewModel.onChangeDiscountCurrent("")
                                    }
                                }
                            )

                            Text(text = "Desconto", fontFamily = LatoBlack)
                        }

                        if(isCheck){
                            NumberInputComponent(maxChar = 13,
                                keyboardType = KeyboardType.Number,
                                value = registerTextFieldViewModel.discount.observeAsState().value,
                                reset = reset.value,
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .padding(end = 16.dp),
                                label = "Desconto",
                                error = registerTextFieldViewModel.discountError.observeAsState().value,
                                customOnClick = object :
                                    CustomTextFieldOnClick {
                                    override fun onChangeValue(newValue: String) {
                                        registerTextFieldViewModel.onChangeDiscountCurrent(newValue)
                                    }
                                })
                        }

                        Spacer(Modifier.height(3.dp))

                        Divider(
                            color = divider,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                    }

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
                        BoxChoiceValue(lifecycleOwner, registerTextFieldViewModel)
                    }

                    CategoryProduct(
                        registerTextFieldViewModel,
                        registerTextFieldViewModel.typeCategoryError.observeAsState().value
                    )

                    PurchaseAndPaymentComponent(
                        lifecycleOwner,
                        registerTextFieldViewModel,
                        registerTextFieldViewModel.idCardError.observeAsState().value
                    )

                    if(!isEditable){
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 186.dp, end = 16.dp, top = 16.dp),
                            onClick = {
                                if (registerTextFieldViewModel.checkFields() && (isCheck && registerTextFieldViewModel.discount.value?.isNotBlank() == true || !isCheck)) {
                                    registerTextFieldViewModel.addPurchase()
                                    registerTextFieldViewModel.onChangeResetDate()
                                    isCheck = false
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

    val scope = rememberCoroutineScope()

    val scrollState = rememberLazyListState()


    fun onClick(category: Category) {
        registerTextFieldViewModel.onChangeCategory(category.id!!)
        registerTextFieldViewModel.onChangeCategoryCurrent(category)
    }

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
        LazyRow(state = scrollState,
            modifier = Modifier.padding(start = 8.dp)) {
            categoryCollections.forEachIndexed { index, categoryScope ->
                if (categoryScope.id == categoryChoice) {
                    scope.launch { scrollState.animateScrollToItem(index) }
                }
            }

            items(categoryCollections) { category ->
                Card(modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable {
                        onClick(category)
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
                                    onClick(category)
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
    lifecycleOwner: LifecycleOwner,
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    error: Boolean? = false
) {
    val context = LocalContext.current
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
    val cardCreditCollection =
        creditCardViewModel.searchCollectionResult.observeAsState(initial = listOf()).value
    val reset by registerTextFieldViewModel.resetDate.observeAsState(initial = false)
    val isBlock = registerTextFieldViewModel.isBlock.observeAsState()
    val colorBackground = if (isBlock.value!!) text_primary.copy(alpha = 0.6f) else primary_dark

    LaunchedEffect(Unit) {
        creditCardViewModel.getAll()
    }

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
                    registerTextFieldViewModel = registerTextFieldViewModel,
                    reset = reset && !isBlock.value!!,
                    isEnableClick = isBlock.value!!,
                    context = context
                )
            }
        }
    }

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
fun BoxChoiceValue(
    lifecycleOwner: LifecycleOwner,
    registerTextFieldViewModel: RegisterTextFieldViewModel
) {
    var isMoney by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("") }
    var convertedValue = 0
    var focusRequester by remember { mutableStateOf(FocusRequester()) }

    registerTextFieldViewModel.resetDate.observe(lifecycleOwner) {
        if (it) {
            value = ""
        }
    }

    registerTextFieldViewModel.quantOrKilo.observe(lifecycleOwner) {
        if (it != null) {
            value = it
        }
    }

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
    val purchaseInfoCollection: MutableLiveData<MutableList<PurchaseInfo>> =
        MutableLiveData<MutableList<PurchaseInfo>>(mutableListOf())
    private val categoryCurrent: MutableLiveData<Category> = MutableLiveData(Category())
    val countProduct: MutableLiveData<Int> = MutableLiveData(0)
    val discount: MutableLiveData<String> = MutableLiveData("")

    //variavel de error
    val productError: MutableLiveData<Boolean> = MutableLiveData(false)
    val priceError: MutableLiveData<Boolean> = MutableLiveData(false)
    val localeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val idCardError: MutableLiveData<Boolean> = MutableLiveData(false)
    val typeCategoryError: MutableLiveData<Boolean> = MutableLiveData(false)
    val quantOrKiloError: MutableLiveData<Boolean> = MutableLiveData(false)
    val discountError: MutableLiveData<Boolean> = MutableLiveData(false)

    private val index: MutableLiveData<Int> = MutableLiveData(-1)
    private val indexInfo: MutableLiveData<Int> = MutableLiveData(-1)
    val categoryCollection = MutableLiveData<MutableList<Category>>(mutableListOf())

    val email = UserLoggedShared.getEmailUserCurrent()

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
        discount.value = purchase.discount.toString()
    }

    override fun checkFields(): Boolean {

        productError.value = product.value!!.isBlank()

        priceError.value = price.value!!.isBlank()

        discountError.value = discount.value!!.isBlank()

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

    private fun updatePurchaseInfoCollection() {
        val auxPurchaseCollection = mutableListOf<PurchaseInfo>()

        auxPurchaseCollection.addAll(purchaseInfoCollection.value!!)

        purchaseInfoCollection.value = auxPurchaseCollection
    }

    fun removerPurchase(indexInfo: Int, index: Int) {
        purchaseInfoCollection.value?.get(indexInfo)?.purchaseCollection?.removeAt(index)
        if (purchaseInfoCollection.value?.get(indexInfo)?.purchaseCollection?.size == 0) {
            purchaseInfoCollection.value?.removeAt(indexInfo)
            updatePurchaseInfoCollection()

            countProduct.value = purchaseInfoCollection.value?.sumOf { purchaseInfo ->
                purchaseInfo.purchaseCollection.count()
            }
        }
    }

    fun onChangeDiscountCurrent(discount: String){
        this.discount.value = discount
    }

    fun onChangeCategoryCurrent(category: Category) {
        this.categoryCurrent.value = category
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
        discount.value = ""

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

        var auxPurchaseCollection = mutableListOf<PurchaseInfo>()

        auxPurchaseCollection.addAll(purchaseInfoCollection.value!!)

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
            category.value!!,
            email,
            MaskUtils.convertValueStringToDouble(
                if(discount.value!!.isNotBlank()) {
                    discount.value!!
                }else{
                    "0.0"
                }
            )
        )

        val purcharAndCategory = PurchaseAndCategory(
            purchase,
            categoryCurrent.value!!
        )

        if (index.value != -1 && indexInfo.value != -1 && auxPurchaseCollection.size > 0) {
            auxPurchaseCollection[indexInfo.value!!].purchaseCollection[index.value!!] =
                purcharAndCategory

            if (auxPurchaseCollection[indexInfo.value!!].title != purcharAndCategory.purchase.locale) {
                auxPurchaseCollection[indexInfo.value!!].title = purcharAndCategory.purchase.locale
            }

        } else {
            if (auxPurchaseCollection.isNotEmpty()) {
                val indexCurrent =
                    auxPurchaseCollection.indexOfFirst { it.title == locale.value!! }
                if (indexCurrent != -1) {
                    auxPurchaseCollection[indexCurrent].purchaseCollection.add(purcharAndCategory)
                } else {
                    val newPurchaseCollection = auxPurchaseCollection
                    newPurchaseCollection.add(
                        PurchaseInfo(
                            purchase.locale,
                            mutableListOf(purcharAndCategory)
                        )
                    )
                    auxPurchaseCollection = newPurchaseCollection
                }

            } else {
                auxPurchaseCollection =
                    mutableListOf(PurchaseInfo(purchase.locale, mutableListOf(purcharAndCategory)))
            }
        }

        purchaseInfoCollection.value!!.removeAll(purchaseInfoCollection.value!!)
        purchaseInfoCollection.value = auxPurchaseCollection

        countProduct.value = auxPurchaseCollection.sumOf { purchaseInfo ->
            purchaseInfo.purchaseCollection.count()
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