package com.example.myshoppinglist.screen

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CallbackPurchase
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.enums.TypeState
import com.example.myshoppinglist.fieldViewModel.RegisterTextFieldViewModel
import com.example.myshoppinglist.model.LocationAndDate
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(
    navController: NavHostController?,
    idCardCurrent: Long,
    isEditable: Boolean? = false,
    purchaseEdit: Purchase? = null
) {
    val context = LocalContext.current
    val screenHeightDp = with(LocalDensity.current) {
        (LocalContext.current.resources.displayMetrics.heightPixels / density).dp
    }
    val density = LocalDensity.current.density
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    val categoryController = CategoryController.getData(context, lifecycleOwner)
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)

    val scaffoldState = rememberBottomSheetScaffoldState()

    val registerTextFieldViewModel: RegisterTextFieldViewModel = viewModel()

    val purchaseAndCategoryDTOCollection by
        registerTextFieldViewModel.purchaseAndCategoryDTOCollection.observeAsState(initial = mutableListOf())
    val categoryCollections by categoryController.getAllDB().observeAsState(initial = emptyList())

    var countProduct by remember { mutableStateOf(0) }
    var visibilityBackHandler by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(false) }
    var visibleWaiting by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }
    var visibilityLocationAndDate by remember { mutableStateOf(false) }
    var visibilityRemoveProduct by remember {mutableStateOf(false)}
    var productRemove by remember {mutableStateOf("")}

    LaunchedEffect(key1 = idCardCurrent) {
        creditCardController.findCreditCardByIdDB(idCardCurrent).observe(lifecycleOwner) {
            registerTextFieldViewModel.onChangeCreditCard(it)

        }
    }

    LaunchedEffect(key1 = purchaseEdit) {
        if (purchaseEdit != null) {
            countProduct = 1

            val price = if (purchaseEdit.typeProduct == TypeProduct.KILO) MaskUtils.maskKiloGram(
                MaskUtils.replaceAll(purchaseEdit.price.toString())
            ) else MaskUtils.maskQuantity(MaskUtils.convertValueDoubleToString(purchaseEdit.price))

            registerTextFieldViewModel.onChangeCategory(purchaseEdit.categoryOwnerId)
            registerTextFieldViewModel.onChangeLocale(purchaseEdit.locale)
            registerTextFieldViewModel.onChangeDateCurrent(purchaseEdit.date)
            registerTextFieldViewModel.onChangePrice(price)
            registerTextFieldViewModel.onChangeProduct(purchaseEdit.name)
            registerTextFieldViewModel.onChangeQuantOrKilo(purchaseEdit.quantiOrKilo)
            registerTextFieldViewModel.onChangeTypeProduct(purchaseEdit.typeProduct)
            if (purchaseEdit.discount > 0) {
                registerTextFieldViewModel.onChangeDiscountCurrent(
                    MaskUtils.maskQuantity(
                        MaskUtils.convertValueDoubleToString(
                            purchaseEdit.discount
                        )
                    )
                )
                isCheck = true
            }
        }
    }

    registerTextFieldViewModel.countProduct.observe(lifecycleOwner) {
        countProduct = it
    }

    fun updatePurchase(location: String, date: String, callback: Callback) {

        val purchase = Purchase(
            registerTextFieldViewModel.product.value!!,
            registerTextFieldViewModel.locale.value!!,
            registerTextFieldViewModel.creditCard.value!!.myShoppingId,
            registerTextFieldViewModel.quantOrKilo.value!!,
            registerTextFieldViewModel.typeProduct.value!!,
            registerTextFieldViewModel.dateCurrent.value!!,
            MaskUtils.convertValueStringToDouble(
                MaskUtils.maskValue(registerTextFieldViewModel.price.value!!)
            ),
            registerTextFieldViewModel.category.value!!,
            registerTextFieldViewModel.email
        )
        purchase.locale = location
        purchase.date = date
        purchase.myShoppingId = purchaseEdit?.myShoppingId ?: 0

        val category = categoryCollections.find {
            registerTextFieldViewModel.category.value == it.myShoppingId
        }
        purchaseController.updatePurchase(
            PurchaseDTO(
                purchase, category!!,
                registerTextFieldViewModel.creditCard.value!!
            ), callback
        )
    }

    fun savePurchases(location: String, date: String, callback: Callback) {

        val purchases = purchaseAndCategoryDTOCollection.map { purchaseAndCategoryDTO ->
                val purchaseDTO = PurchaseDTO(
                    purchaseAndCategoryDTO.purchaseDTO.toPurchase(purchaseAndCategoryDTO.categoryDTO.userDTO.email),
                    purchaseAndCategoryDTO.categoryDTO.toCategory(),
                    registerTextFieldViewModel.creditCard.value!!
                )
                purchaseDTO.locale = location
                purchaseDTO.date = date

                purchaseDTO
        }

        purchaseController.savePurchases(purchases, callback)
    }

    val callback = object : CallbackObject<ItemListDTO> {
        override fun onSuccess() {
            visibilityLocationAndDate = false

            navController!!.popBackStack()
            visibleWaiting = false
            messageError = MeasureTimeService.messageWaitService

        }

        override fun onCancel() {
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

    fun getHeightScreenDP(): Dp {
        val pixels = screenHeightDp.value.toDouble()
        return (pixels / density).dp
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
        DialogLocationAndDate(
            context,
            visibilityLocationAndDate,
            recoverLocation = purchaseEdit?.locale,
            recoverDate = purchaseEdit?.date,
            object : CallbackObject<LocationAndDate> {

                override fun onCancel() {
                    visibilityLocationAndDate = false
                }

                override fun onSuccess(locationAndDate: LocationAndDate) {

                    if (isEditable != null && isEditable) {
                        updatePurchase(locationAndDate.location, locationAndDate.date, callback)
                    } else {
                        savePurchases(locationAndDate.location, locationAndDate.date, callback)
                    }
                }
            })

        BottomSheetScaffold(
            sheetBackgroundColor = if (!isEditable!!) {
                background_card
            } else {
                text_primary
            },
            sheetPeekHeight = (if (!isEditable) getHeightScreenDP() else 0.dp),
            sheetContent = {
                if (!isEditable) {
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
                            purchaseAndCategoryDTOCollection,
                            object : CallbackPurchase() {
                                override fun onChangeIndex(
                                    index: Int,
                                    typeState: TypeState
                                ) {
                                    val purchaseUpdateData =
                                        purchaseAndCategoryDTOCollection[index]

                                    if (typeState == TypeState.EDIT) {

                                        registerTextFieldViewModel.updateData(
                                            purchaseUpdateData.purchaseDTO.toPurchase(
                                                registerTextFieldViewModel.email
                                            ),
                                            index
                                        )

                                        if (purchaseUpdateData.purchaseDTO.discount > 0) {
                                            isCheck = true
                                        }

                                        coroutineScope.launch {
                                            scaffoldState.bottomSheetState.collapse()
                                        }
                                    } else {
                                        registerTextFieldViewModel.index.value = index
                                        visibilityRemoveProduct = true
                                        productRemove = purchaseUpdateData.purchaseDTO.name
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
                            if (countProduct > 0) {

                                visibilityLocationAndDate = true
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.expand()
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
                        IconButton(onClick = {
                            keyboardController?.hide()
                            visibilityBackHandler = true
                        }) {
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
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    DialogBackCustom(visibilityRemoveProduct, {
                        coroutineScope.launch {
                            registerTextFieldViewModel.removerPurchase(registerTextFieldViewModel.index.value!!)

                            scaffoldState.bottomSheetState.collapse()
                            visibilityRemoveProduct = false
                            registerTextFieldViewModel.index.value = -1
                        }
                    }, {
                        registerTextFieldViewModel.index.value = -1
                        visibilityRemoveProduct = false
                    }, "Deletar", "Deseja remover o item $productRemove da lista?")

                    WaitingProcessComponent(visibleWaiting, messageError, callback)

                    TextInputComponent(
                        label = "Produto",
                        value = registerTextFieldViewModel.product.value!!,
                        maxChar = 45,
                        isCountChar = true,
                        isMandatory = true,
                        error = registerTextFieldViewModel.productError.value,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                registerTextFieldViewModel.onChangeProduct(newValue)
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                                checked = isCheck,
                                onCheckedChange = {
                                    isCheck = !isCheck

                                    if (!isCheck) {
                                        registerTextFieldViewModel.onChangeDiscountCurrent("")
                                    }
                                }
                            )

                            Text(text = "Desconto", fontFamily = LatoBlack)
                        }

                        if (isCheck) {
                            NumberInputComponent(maxChar = 13,
                                keyboardType = KeyboardType.Number,
                                value = registerTextFieldViewModel.discount.value,
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .padding(end = 16.dp),
                                label = "Desconto",
                                error = registerTextFieldViewModel.discountError.value,
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
                            value = registerTextFieldViewModel.price.value,
                            modifier = Modifier
                                .fillMaxWidth(0.45f)
                                .padding(end = 16.dp),
                            label = "Preço",
                            error = registerTextFieldViewModel.priceError.value,
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
                        if (isEditable && purchaseEdit != null) purchaseEdit.categoryOwnerId else 0,
                        categoryCollections,
                        registerTextFieldViewModel,
                        object : CallbackObject<Category> {
                            override fun onSuccess(category: Category) {
                                registerTextFieldViewModel.onChangeCategory(category.myShoppingId!!)
                                registerTextFieldViewModel.onChangeCategoryCurrent(category)
                                keyboardController?.hide()
                            }
                        }
                    )

                    if (!isEditable) {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 186.dp, end = 16.dp, top = 16.dp),
                            enabled = registerTextFieldViewModel.enableButtonAdd.observeAsState(
                                false
                            ).value,
                            onClick = {
                                if (registerTextFieldViewModel.checkFields() && ((isCheck && registerTextFieldViewModel.discount.value?.isNotBlank()!!) || !isCheck)) {
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
    idUpdate: Long = 0L,
    categoryCollections: List<Category>,
    registerTextFieldViewModel: RegisterTextFieldViewModel,
    callbackObject: CallbackObject<Category>
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val scrollState = rememberLazyListState()

    var idCategoryChoice by remember { mutableStateOf(idUpdate) }

    registerTextFieldViewModel.resetDate.observeForever {
        if (it) {
            idCategoryChoice = 0
            registerTextFieldViewModel.onFieldResetData()
        }
    }

    registerTextFieldViewModel.updateDate.observeForever {
        if (it && registerTextFieldViewModel.category.value != null) {
            idCategoryChoice = registerTextFieldViewModel.category.value!!
            registerTextFieldViewModel.onResetUpdateData()
        }
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Categorias",
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 4.dp),
                fontWeight = FontWeight.Bold
            )
            if (registerTextFieldViewModel.typeCategoryError.observeAsState().value!!) Icon(
                painter = painterResource(id = R.drawable.ic_baseline_error_24),
                modifier = Modifier.size(16.dp),
                contentDescription = null,
                tint = message_error
            )
        }
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            categoryCollections.forEachIndexed { index, categoryScope ->
                if (categoryScope.myShoppingId == idCategoryChoice) {
                    scope.launch { scrollState.animateScrollToItem(index) }
                }
            }

            items(categoryCollections) { category ->
                Card(modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable {
                        idCategoryChoice = category.myShoppingId
                        callbackObject.onSuccess(category)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .height(33.dp)
                            .background(if (category.myShoppingId == idCategoryChoice) background_card_light else background_card),
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
                                    idCategoryChoice = category.myShoppingId
                                    callbackObject.onSuccess(category)
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
    registerTextFieldViewModel: RegisterTextFieldViewModel
) {
    var isMoney by remember { mutableStateOf(true) }
    var convertedValue = 0
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val valueAmount by registerTextFieldViewModel.quantOrKilo.observeAsState("")

    val customOnClick = object : CustomTextFieldOnClick {
        override fun onChangeValue(newValue: String) {
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
                value = valueAmount,
                focusRequester = focusRequester,
                error = registerTextFieldViewModel.quantOrKiloError.value,
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
                                if (valueAmount.isBlank()) 0 else MaskUtils.replaceAll(valueAmount)
                                    .toInt()
                            convertedValue += 1
                            registerTextFieldViewModel.onChangeQuantOrKilo(convertedValue.toString())
                        }
                    },
                    icon = R.drawable.ic_baseline_add_24
                )

                CustomButton(callback = object : Callback {
                    override fun onClick() {
                        focusRequester.requestFocus()
                        convertedValue =
                            if (valueAmount.isBlank()) 0 else MaskUtils.replaceAll(valueAmount)
                                .toInt()
                        if (convertedValue > 0) {
                            convertedValue -= 1
                            registerTextFieldViewModel.onChangeQuantOrKilo(convertedValue.toString())
                        }
                    }
                }, icon = R.drawable.ic_baseline_remove_24)
            }

        }
    }
}