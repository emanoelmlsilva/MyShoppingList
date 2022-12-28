package com.example.myshoppinglist.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.*
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
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.*
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MaskUtils.convertValueDoubleToString
import kotlinx.coroutines.*
import java.util.*

val TAG = "ProductsManagerScreen"

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(navController: NavController?) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val productManagerFieldViewModel: ProductManagerFieldViewModel =
        ProductManagerFieldViewModel(context)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
    val purchaseInfoFilterCollection = remember { mutableStateListOf<PurchaseAndCategory>() }
    var valueSum by remember { mutableStateOf(0.0) }
    var quantityPurchases by remember { mutableStateOf("00") }
    var visibleAnimation by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        creditCardViewModel.getAll()
        productManagerFieldViewModel.mountObejctSearchDatabase(ObjectFilter())
    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner) { creditCardCollection ->
        productManagerFieldViewModel.onChangeCardCreditCollection(creditCardCollection.map { creditCard ->
            CreditCardDTO().fromCreditCardDTO(
                creditCard
            )
        })
    }

    productManagerFieldViewModel.purchaseViewModel.searchPurchaseAndCategory.observe(lifecycleOwner) {
        quantityPurchases =
            if (it.size > 100) it.size.toString() else if (it.size < 10) "00${it.size}" else "0${it.size}"
        purchaseInfoFilterCollection.removeAll(purchaseInfoFilterCollection)
        purchaseInfoFilterCollection.addAll(it)
    }

    productManagerFieldViewModel.purchaseViewModel.searchSumPriceResult.observe(lifecycleOwner) {
        valueSum = it
    }

    productManagerFieldViewModel.visibleAnimation.observe(lifecycleOwner) {
        visibleAnimation = it
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
            SearchProduct(context, visibleAnimation, lifecycleOwner, productManagerFieldViewModel)

            BoxShowPriceProduct(
                convertValueDoubleToString(valueSum),
                quantityPurchases
            )

            BoxPurchaseItens(context, purchaseInfoFilterCollection, productManagerFieldViewModel)

        }
    }
}

@SuppressLint("CheckResult")
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchProduct(
    context: Context,
    visibleAnimation: Boolean,
    lifecycleOwner: LifecycleOwner,
    productManagerFieldViewModel: ProductManagerFieldViewModel
) {
    val product = remember { mutableStateOf("") }
    val listProductText = remember { mutableStateListOf<String>() }
    val reset = remember { mutableStateOf(false) }
    var enableDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf(ObjectFilter()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    fun removeFilter(productItem: String) {
        listProductText.remove(productItem)

        var formarValueMin: String? = null
        var formarValueMax: String? = null

        if (filter.priceMin != null && filter.priceMax != null) {
            formarValueMin = MaskUtils.maskValue(
                convertValueDoubleToString(
                    filter.priceMin!!.toDouble()
                )
            )
            formarValueMax = MaskUtils.maskValue(
                convertValueDoubleToString(
                    filter.priceMax!!.toDouble()
                )
            )
        }

        if (productItem == filter.text) {
            filter.text = ""
        } else if (productItem == filter.month) {
            filter.month = ""
        } else if (productItem == "$formarValueMin á $formarValueMax") {
            filter.priceMin = null
            filter.priceMax = null
        } else if (productItem.startsWith("%card%")) {
            filter.cardFilter = CardCreditFilter()
            filter.idCard = 0
        } else {
            var typeCategory =
                filter.categoryCollection.find { productItem.contains(it.category) }
            if (typeCategory != null) {
                filter.categoryCollection.remove(typeCategory)
            }
        }

        productManagerFieldViewModel.mountObejctSearchDatabase(filter)

    }

    productManagerFieldViewModel.product.observe(lifecycleOwner) {
        if (it.isNotBlank() && reset.value) {
            reset.value = false
        }
        product.value = it
    }

    LaunchedEffect(key1 = enableDialog) {
        if (enableDialog) {
            keyboardController!!.hide()
        }
    }

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {
            Card(
                backgroundColor = background_card_gray_light, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.13f),
                shape = RoundedCornerShape(8.dp)
            ) {
                AlertDialogFilter(
                    keyboardController!!,
                    enableDialog,
                    filter,
                    productManagerFieldViewModel,
                    lifecycleOwner,
                    object : CallbackFilter {
                        override fun onClick() {
                            enableDialog = false
                        }

                        override fun onChangeObjectFilter(value: ObjectFilter) {

                            listProductText.removeAll(listProductText)

                            value.categoryCollection.forEach { category ->
                                listProductText.add("%category%, ${category.id}, ${category.category}, ${category.idImage}, ${category.color}")
                            }

                            if (value.priceMin != null && value.priceMax != null) {
                                val formarValueMin =
                                    MaskUtils.maskValue(convertValueDoubleToString(value.priceMin!!.toDouble()))
                                val formarValueMax =
                                    MaskUtils.maskValue(convertValueDoubleToString(value.priceMax!!.toDouble()))

                                listProductText.add("$formarValueMin á $formarValueMax")
                            }

                            if (value.month.isNotBlank()) {
                                listProductText.add(value.month)
                            }

                            if (value.cardFilter.avatar > 0) {
                                listProductText.add("%card%, ${value.idCard}, ${value.cardFilter.avatar}, ${value.cardFilter.nickName}")
                            }

                            filter = value

                            productManagerFieldViewModel.mountObejctSearchDatabase(filter)
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
                            TextInputComponent(
                                focusRequester = focusRequester,
                                modifier = Modifier
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
                                        filter.text = product.value

                                        productManagerFieldViewModel.mountObejctSearchDatabase(
                                            filter
                                        )
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
                                    keyboardController.hide()
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.Main) {
                                            enableDialog = true
                                        }
                                    }
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

                            var label: String? = null
                            var icon: Int? = null
                            var category: Category? = null

                            if (productItem.startsWith("%card%")) {
                                var splitValueText = productItem.split(",")
                                label = splitValueText[3]
                                icon = splitValueText[2].trim().toInt()
                            }else if(productItem.startsWith("%category%")){
                                var splitValuesCategory = productItem.split(",")
                                val idSplit = splitValuesCategory[1].trim().toLong()
                                val categorySplit = splitValuesCategory[2].trim()
                                val idImageSplit = splitValuesCategory[3].trim()
                                val color = splitValuesCategory[4].trim().toFloat().toInt()

                                category = Category(categorySplit, idImageSplit, color)
                                category.id = idSplit
                            }
                            CustomerChip(
                                context = context,
                                category = category,
                                label = label ?: productItem,
                                iconId = icon,
                                callback = object : Callback {
                                    override fun onClick() {
                                        removeFilter(productItem)
                                    }
                                })
                        }
                    }
                }
            }
        })
}

fun mountItemPurchase(purchaseCollection: List<PurchaseAndCategory>): List<PurchaseInfo> {
    val purchaseInfoCollection = mutableListOf<PurchaseInfo>()

    if (purchaseCollection.isEmpty()) return purchaseInfoCollection

    purchaseCollection.forEach { purchaseAndCategory ->

        val purchase = purchaseAndCategory.purchase
        val category = purchaseAndCategory.category

        val purchaseFilterCollection =
            purchaseCollection.filter { item -> purchase.name == item.purchase.name }

        val hasNotItemPurchase =
            purchaseInfoCollection.firstOrNull { item -> item.title == purchase.name } == null

        if (hasNotItemPurchase) {

            if (purchaseFilterCollection.isNotEmpty()) {
                val purchaseMultCollection: MutableList<Purchase> =
                    purchaseFilterCollection.map { it.purchase } as MutableList<Purchase>

                val valueSum = purchaseMultCollection.sumOf { it.price * if(it.typeProduct == TypeProduct.QUANTITY) it.quantiOrKilo.toInt() else 1 }

                val purchaseInfo = PurchaseInfo(
                    purchase.name, category.idImage, valueSum, Color(category.color),
                    purchaseMultCollection
                )

                purchaseInfoCollection.add(purchaseInfo)
            }
        }

    }

    return purchaseInfoCollection
}

fun isExpanded(index: Int, visibilityCollection: MutableList<Int>): Boolean {
    return visibilityCollection.indexOf(index) != -1
}

@Composable
fun BoxPurchaseItens(
    context: Context,
    purchaseCollection: List<PurchaseAndCategory>,
    productManagerFieldViewModel: ProductManagerFieldViewModel
) {
    var purchaseInfoCollection = remember {
        mutableStateListOf<PurchaseInfo>()
    }

    val expandeds = remember { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = purchaseCollection.size) {
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        purchaseInfoCollection.addAll(mountItemPurchase(purchaseCollection))
    }


    fun expandableContainer(index: Int) {
        val auxExpandeds = expandeds.toMutableList()
        expandeds.removeAll(expandeds)
        expandeds.addAll(changeVisibility(index, auxExpandeds))
    }

    BaseLazyColumnScroll(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        visibleAnimation = true,
        callback = object : VisibleCallback() {
            override fun onChangeVisible(visible: Boolean) {
                productManagerFieldViewModel.onChangeVisibleAnimation(visible)
            }
        }
    ) {
        purchaseInfoCollection.mapIndexed { indexInfo, purchaseInfo ->
            item {
                Column(
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandableContainer(indexInfo) }
                            .padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { expandableContainer(indexInfo) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = if (com.example.myshoppinglist.components.isExpanded(
                                            indexInfo,
                                            expandeds
                                        )
                                    ) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = text_primary,
                                )
                            }
                            IconCategoryComponent(
                                modifier = Modifier.padding(start = 6.dp),
                                iconCategory = AssetsUtils.readIconBitmapById(
                                    context,
                                    purchaseInfo.avatar
                                )!!
                                    .asImageBitmap(),
                                colorIcon = purchaseInfo.color,
                                size = 30.dp,
                                enabledBackground = true
                            )

                            Text(
                                text = purchaseInfo.title.capitalize(),
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .padding(start = 8.dp),
                                fontFamily = LatoBold
                            )
                        }
                        Text(
                            text = "${if (purchaseInfo.purchaseCollection.size < 100) "0${purchaseInfo.purchaseCollection.size}" else purchaseInfo.purchaseCollection.size}",
                            fontFamily = LatoBlack
                        )
                    }
                }

            }

            if (isExpanded(
                    indexInfo,
                    expandeds
                )
            ) {

                itemsIndexed(purchaseInfo.purchaseCollection) { index, purchase ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(.9f)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            ) {
                                Text(
                                    fontFamily = LatoRegular,
                                    text = purchase.locale, modifier = Modifier
                                        .padding(start = 12.dp),
                                    textAlign = TextAlign.Start
                                )
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        fontFamily = LatoRegular,
                                        fontSize = 12.sp,
                                        text = "${if (purchase.typeProduct == TypeProduct.QUANTITY) "x" else ""} ${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}"
                                    )

                                    Text(
                                        fontFamily = LatoRegular,
                                        fontSize = 12.sp,
                                        text = "R$ ${
                                            MaskUtils.maskValue(
                                                convertValueDoubleToString(
                                                    purchase.price
                                                )
                                            )
                                        }",
                                        modifier = Modifier
                                            .padding(start = 12.dp),
                                    )

                                    Text(
                                        text = FormatUtils().getNameDay(purchase.date, false)
                                            .uppercase(),
                                        fontFamily = LatoBlack,
                                        fontSize = 12.sp,
                                        color = text_primary_light,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                    )

                                }

                            }
                            Divider(
                                color = divider_ligth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 6.dp,
                            end = 6.dp,
                            bottom = if (indexInfo == (purchaseInfoCollection.size - 1)) 56.dp else 8.dp
                        ),
                ) {
                    Text(text = "Total", fontFamily = LatoBlack, color = text_title_secondary)
                    Text(
                        text = "- R$ ${
                            MaskUtils.maskValue(
                                convertValueDoubleToString(
                                    purchaseInfo.value
                                )
                            )
                        }",
                        fontFamily = LatoBlack,
                        modifier = Modifier.padding(start = 8.dp, end = 6.dp),
                        color = primary_dark
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )

            }

        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AlertDialogFilter(
    keyboardController: SoftwareKeyboardController,
    enableDialog: Boolean,
    filter: ObjectFilter,
    productManagerFieldViewModel: ProductManagerFieldViewModel,
    lifecycleOwner: LifecycleOwner,
    callback: CallbackFilter
) {
    val context = LocalContext.current
    val priceMinDefault = 0F
    val priceMaxDefault = 100F
    val categoryChoices = remember { mutableStateListOf<Category>() }
    val categoryCollections = remember { mutableStateListOf<Category>() }
    var priceMin by remember { mutableStateOf(priceMinDefault) }
    var priceMax by remember { mutableStateOf(priceMaxDefault) }
    var month by remember { mutableStateOf("") }
    var idCardCredit by remember { mutableStateOf(0L) }
    var currentCardCreditFilter by remember { mutableStateOf(CardCreditFilter()) }
    var creditCardDTOCollection = remember { mutableListOf<CreditCardDTO>() }
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)

    LaunchedEffect(Unit) {
        keyboardController.hide()
        categoryViewModel.getAll()
    }

    categoryViewModel.searchCollectionResult.observe(lifecycleOwner) {
        categoryCollections.removeAll(categoryCollections)
        categoryCollections.addAll(it)
    }

    productManagerFieldViewModel.cardCreditCollection.observe(lifecycleOwner) {
        if (it.isNotEmpty()) {
            creditCardDTOCollection.removeAll(creditCardDTOCollection)
            creditCardDTOCollection.addAll(it)
        }
    }

    LaunchedEffect(key1 = filter.cardFilter.id) {
        idCardCredit = filter.cardFilter.id
        currentCardCreditFilter = filter.cardFilter
    }

    LaunchedEffect(key1 = filter.priceMin, key2 = filter.priceMax) {
        keyboardController.hide()
        priceMin = if (filter.priceMin == null) priceMinDefault else filter.priceMin!!
        priceMax = if (filter.priceMax == null) priceMaxDefault else filter.priceMax!!
    }

    LaunchedEffect(key1 = filter.categoryCollection) {
        categoryChoices.forEach {
            val hasCollection = filter.categoryCollection.find { category -> category == it }

            if (hasCollection == null) {
                categoryChoices.remove(it)
            }
        }
    }

    LaunchedEffect(key1 = filter.month) {
        month = filter.month
    }

    if (enableDialog) {
        Dialog(
            onDismissRequest = { },
            content = {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(
                            text = "Filtros",
                            fontFamily = LatoBold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(top = 25.dp)
                        )
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
                                    backgroundColor = primary_dark,
                                    shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Categorias:",
                                            fontFamily = LatoBlack,
                                            fontSize = 18.sp,
                                            color = text_primary_light,
                                            modifier = Modifier.padding(start = 12.dp)
                                        )
                                    }
                                }
                                LazyRow(
                                    modifier = Modifier
                                        .background(background_card_gray_light)
                                        .fillMaxHeight(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    items(categoryCollections) { category ->
                                        val isChoiceCurrent =
                                            categoryChoices.find { categoryChoice ->
                                                categoryChoice == category
                                            } != null
                                        Card(modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                val isChoiceOld =
                                                    categoryChoices.find { categoryChoice ->
                                                        categoryChoice == category
                                                    } != null
                                                if (isChoiceOld) {
                                                    categoryChoices.remove(category)
                                                } else {
                                                    categoryChoices.add(category)
                                                }
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isChoiceCurrent) background_card_light else background_card),
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconCategoryComponent(
                                                    modifier = Modifier.padding(
                                                        horizontal = 8.dp,
                                                        vertical = 4.dp
                                                    ),
                                                    iconCategory = AssetsUtils.readIconBitmapById(
                                                        context,
                                                        category.idImage
                                                    )!!
                                                        .asImageBitmap(),
                                                    colorIcon = Color(category.color),
                                                    size = 23.dp,
                                                    enableClick = true,
                                                    enabledBackground = true,
                                                    callback = object : Callback {
                                                        override fun onClick() {
                                                            val isChoiceOld =
                                                                categoryChoices.find { categoryChoice ->
                                                                    categoryChoice == category
                                                                } != null
                                                            if (isChoiceOld) {
                                                                categoryChoices.remove(category)
                                                            } else {
                                                                categoryChoices.add(category)
                                                            }
                                                        }
                                                    }
                                                )

                                                Text(
                                                    text = category.category,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(end = if (isChoiceCurrent) 8.dp else 18.dp)
                                                )

                                                if (isChoiceCurrent) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Done,
                                                        contentDescription = null,
                                                        tint = text_primary,
                                                        modifier = Modifier
                                                            .size(23.dp)
                                                            .padding(end = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ChoicePrice(
                            priceMin,
                            priceMax,
                            callbackMin = object : CustomTextFieldOnClick {
                                override fun onChangeValueFloat(newValue: Float) {
                                    priceMin = newValue
                                }
                            },
                            callbackMax = object : CustomTextFieldOnClick {
                                override fun onChangeValueFloat(newValue: Float) {
                                    priceMax = newValue
                                }
                            })

                        ChoiceCard(
                            filter.cardFilter.id,
                            creditCardDTOCollection,
                            object : CallbackCreditCard {
                                override fun onChangeFilterCreditCard(cardCreditFilter: CardCreditFilter) {
                                    idCardCredit = cardCreditFilter.id
                                    currentCardCreditFilter = cardCreditFilter
                                }
                            })

                        ChoiceData(idCardCredit, filter.month, lifecycleOwner, object : Callback {
                            override fun onChangeValue(newMonth: String) {
                                month = newMonth
                            }
                        })

                        Column {
                            ButtonsFooterContent(
                                btnTextCancel = "CANCELAR",
                                onClickCancel = { callback.onClick() },
                                btnTextAccept = "SALVAR",
                                onClickAccept = {

                                    val saveFilter = ObjectFilter(
                                        categoryCollection = categoryChoices,
                                        priceMin = priceMin,
                                        priceMax = priceMax,
                                        idCard = idCardCredit,
                                        month = month,
                                        cardFilter = currentCardCreditFilter
                                    )
                                    callback.onChangeObjectFilter(saveFilter)
                                    callback.onClick()
                                })
                        }

                    }
                }
            },
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ChoicePrice(
    priceMin: Float? = 0F,
    priceMax: Float? = 100F,
    maxStepDefault: Float = 100.0f,
    callbackMin: CustomTextFieldOnClick,
    callbackMax: CustomTextFieldOnClick
) {
    var maxStepCurrent by remember { mutableStateOf(maxStepDefault) }
    var sliderPosition by remember { mutableStateOf(priceMin!!..maxStepDefault) }
    var valueMin by remember { mutableStateOf(priceMin) }
    var valueMax by remember { mutableStateOf(if (priceMax!! > 0f) priceMax else maxStepDefault) }
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

    LaunchedEffect(key1 = priceMin, key2 = priceMax) {
        sliderPosition = priceMin!!..(if (priceMax!! > 0f) priceMax else maxStepDefault)
        valueMin = priceMin
        valueMax = if (priceMax > 0f) priceMax else maxStepDefault

    }

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

                    callbackMin.onChangeValueFloat(valueMin!!)
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
                Text(text = MaskUtils.maskValue(convertValueDoubleToString(valueMin!!.toDouble())))

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

                                    callbackMin.onChangeValueFloat(valueMin!!)
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
                                convertValueDoubleToString(
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
fun ChoiceCard(
    cardCurrent: Long,
    cardCreditCollection: List<CreditCardDTO>,
    callbackCard: CallbackCreditCard
) {
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
                        color = background_card_light,
                        callback = object : Callback {
                            override fun onClick() {
                                cardCreditChoice = cardCredit
                                callbackCard.onChangeValue(cardCreditChoice.idCard)
                                callbackCard.onChangeFilterCreditCard(
                                    CardCreditFilter(
                                        cardCreditChoice
                                    )
                                )
                            }
                        })
                }
            }

        }

    }

}

@ExperimentalMaterialApi
@Composable
fun ChoiceData(
    idCard: Long,
    dataCurrent: String,
    lifecycleOwner: LifecycleOwner,
    callback: Callback
) {
    val context = LocalContext.current
    val purchaseViewModel = PurchaseViewModel(context)
    var expanded by remember { mutableStateOf(false) }
    var splitDataCurrent = if (dataCurrent.isNotBlank()) dataCurrent.split("-") else listOf()
    var yearCurrent by remember { mutableStateOf(if (splitDataCurrent.isNotEmpty()) splitDataCurrent[0] else "") }
    var monthCurrent by remember { mutableStateOf(if (splitDataCurrent.isNotEmpty()) splitDataCurrent[1] else "") }
    val dataCollection = remember { mutableStateListOf<String>() }
    val dateMonthAndYear = remember { mutableStateMapOf<String, MutableList<String>>() }

    fun splitMonthOfYead(): HashMap<String, MutableList<String>> {
        val mapDate = HashMap<String, MutableList<String>>()
        dataCollection.forEach {
            val splitDate = it.split("-")
            val year = splitDate[0]
            val month = splitDate[1]

            if (mapDate[year].isNullOrEmpty()) {
                mapDate[year] = mutableListOf(month)
            } else {
                mapDate[year]?.add(month)
            }

        }

        return mapDate
    }

    LaunchedEffect(key1 = idCard) {
        purchaseViewModel.getMonthDistinctByIdCard(idCard)
    }

    purchaseViewModel.searchResultMonths.observe(lifecycleOwner) { it ->
        dataCollection.removeAll(dataCollection)
        dataCollection.addAll(it)

        dateMonthAndYear.entries.removeAll(dateMonthAndYear.entries)

        dateMonthAndYear.putAll(splitMonthOfYead())
        yearCurrent = ""
        monthCurrent = ""

        if (it.isNotEmpty()) {
            yearCurrent = dateMonthAndYear.keys.toList()[0]
            monthCurrent = if (splitDataCurrent.isNotEmpty()) splitDataCurrent[1] else ""
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
                items(dateMonthAndYear.values.toList()) { months ->
                    months.forEach { month ->
                        val isChoiceCurrent = monthCurrent == month
                        CustomerChip(
                            paddingVertical = 0.dp,
                            label = FormatUtils().getNameMonth(month)
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            isEnabled = true,
                            isBackgroundCircle = true,
                            isChoice = isChoiceCurrent,
                            color = background_card_light,
                            callback = object : Callback {
                                override fun onClick() {
                                    if (monthCurrent == month) {
                                        monthCurrent = ""
                                    } else {
                                        monthCurrent = month

                                        callback.onChangeValue("$yearCurrent-$monthCurrent")
                                    }

                                }
                            })
                    }
                }
            }

        }

    }
}

class ProductManagerFieldViewModel(context: Context) : BaseFieldViewModel() {
    val purchaseViewModel = PurchaseViewModel(context)
    val product: MutableLiveData<String> = MutableLiveData("")
    var cardCreditCollection: MutableLiveData<List<CreditCardDTO>> =
        MutableLiveData(mutableListOf())
    var visibleAnimation: MutableLiveData<Boolean> = MutableLiveData(true)

    fun onChangeVisibleAnimation(newVisibleAnimation: Boolean) {
        this.visibleAnimation.value = newVisibleAnimation
    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
    }

    fun onChangeCardCreditCollection(newCardCreditCollection: List<CreditCardDTO>) {
        cardCreditCollection.value = newCardCreditCollection
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

    fun mountObejctSearchDatabase(objectFilter: ObjectFilter) {

        var nameFields: String = ""
        var collectionSeach: MutableList<Any> = mutableListOf()

        if (objectFilter.text.isNotBlank()) {
            nameFields += "name LIKE '%' || ? || '%'"
            collectionSeach.add(objectFilter.text)
        }

        if (objectFilter.priceMin != null && objectFilter.priceMin!! >= 0) {
            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price >= ?"
            collectionSeach.add(objectFilter.priceMin!!)
        }

        if (objectFilter.priceMax != null && objectFilter.priceMax!! >= 0) {
            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price <= ?"
            collectionSeach.add(objectFilter.priceMax!!)
        }

        if (objectFilter.idCard > 0) {
            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }
            nameFields += "purchaseCardId =?"
            collectionSeach.add(objectFilter.idCard)
        }

        if (objectFilter.month.isNotBlank()) {
            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }
            nameFields += "date LIKE '%' || ? || '%'"
            collectionSeach.add(objectFilter.month)
        }

        if (objectFilter.categoryCollection.size > 0) {

            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }

            objectFilter.categoryCollection.forEachIndexed { index, category ->
                nameFields += "categoryOwnerId = ?"
                collectionSeach.add(category.id)

                if (objectFilter.categoryCollection.size > 1 && index < objectFilter.categoryCollection.size - 1) {
                    nameFields += " AND "
                }
            }
        }

        if (objectFilter.cardFilter.nickName.isNotBlank()) {
            if (collectionSeach.size > 0) {
                nameFields += " AND "
            }

            nameFields += "purchaseCardId = ?"
            collectionSeach.add(objectFilter.cardFilter.id)
        }

        purchaseViewModel.getPurchasesOfSearch(
            collectionSeach, nameFields
        )

        purchaseViewModel.getPurchasesSumOfSearch(collectionSeach, nameFields)
    }

}