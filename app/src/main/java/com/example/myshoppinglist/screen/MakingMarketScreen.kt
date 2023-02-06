package com.example.myshoppinglist.screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.ItemListViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.CardUtils.getNameCard
import com.example.myshoppinglist.utils.ConversionUtils
import com.example.myshoppinglist.utils.MaskUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MarketItem(
    var price: Float,
    var amount: String,
    var type: TypeProduct,
    var itemListAndCategory: ItemListAndCategory
) {
    override fun toString(): String {
        return "MarketItem(price=$price, amount='$amount', type=$type, itemListAndCategory=$itemListAndCategory)"
    }
}

@Composable
fun MakingMarketScreen(
    navController: NavHostController,
    idCard: Long,
    itemListJson: String
) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var visibleAnimation by remember { mutableStateOf(true) }
    var checkAll by remember { mutableStateOf(false) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val marketItemCollection = remember { mutableStateListOf<MarketItem>() }
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)
    var visibility by remember {
        mutableStateOf(false)
    }
    var valueTotal by remember {
        mutableStateOf(0F)
    }
    var visibilityBackHandler by remember {
        mutableStateOf(false)
    }

    BackHandler {
        visibilityBackHandler = true
    }

    LaunchedEffect(key1 = idCard, key2 = itemListJson) {
        itemListViewModel.getAll(idCard)
        ConversionUtils.fromJson(itemListJson)!!.forEach { itemList ->
            val marketItem =
                MarketItem(0F, "0", TypeProduct.QUANTITY, itemList.toItemListAndCategory())
            marketItemCollection.add(marketItem)
        }
    }

    fun findMarketItem(idItemAndCategory: Long): MarketItem? {
        return marketItemCollection.find { it.itemListAndCategory.itemList.id == idItemAndCategory }
    }

    fun updatePrice(idItemAndCategory: Long, price: Float) {
        val marketItem = findMarketItem(idItemAndCategory)
        val index = marketItemCollection.indexOf(marketItem)
        val auxMarketItemCollection = ArrayList(marketItemCollection)

        if (marketItem != null) {
            marketItem.price = price
            auxMarketItemCollection[index] = marketItem

            marketItemCollection.removeAll(marketItemCollection)
            marketItemCollection.addAll(auxMarketItemCollection)
        }
    }

    fun updateTypeProduct(idItemAndCategory: Long, typeProduct: TypeProduct) {
        val marketItem = findMarketItem(idItemAndCategory)
        val index = marketItemCollection.indexOf(marketItem)
        val auxMarketItemCollection = ArrayList(marketItemCollection)

        if (marketItem != null) {
            marketItem.type = typeProduct
            auxMarketItemCollection[index] = marketItem

            marketItemCollection.removeAll(marketItemCollection)
            marketItemCollection.addAll(auxMarketItemCollection)
        }
    }

    fun updateAmount(idItemAndCategory: Long, amount: String) {
        val marketItem = findMarketItem(idItemAndCategory)
        val index = marketItemCollection.indexOf(marketItem)
        val auxMarketItemCollection = ArrayList(marketItemCollection)

        if (marketItem != null) {
            marketItem.amount = amount
            auxMarketItemCollection[index] = marketItem

            marketItemCollection.removeAll(marketItemCollection)
            marketItemCollection.addAll(auxMarketItemCollection)
        }

    }

    fun checkFieldProduct(marketItem: MarketItem): Boolean {
        return marketItem.amount.isNotBlank() && marketItem.amount != "0" && marketItem.price > 0
    }

    TopAppBarScreen(
        hasBackButton = true,
        hasToolbar = true,
        onClickIcon = { visibilityBackHandler = true },
        paddingFloatingButton = 12.dp,
        floatingActionButton = {
            if (itemCheckCollection.isNotEmpty() && visibleAnimation) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 52.dp)
                ) {
                    ButtonsFooterContent(
                        btnTextAccept = "CONCLUIR COMPRAS R$ ${
                            MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    valueTotal.toDouble()
                                )
                            )
                        }",
                        onClickAccept = {
                            visibility = true
                        })
                }
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

                DialogBackCustom(visibilityBackHandler, {
                    visibilityBackHandler = false
                    navController.popBackStack()
                }, {
                    visibilityBackHandler = false
                }, "Sair", "Os dados adicionados serão perdidos!\nTem certeza que deseja sair?")

                DialogSaveProduct(
                    visibility,
                    context,
                    lifecycleOwner,
                    navController,
                    marketItemCollection,
                    itemCheckCollection,
                    object : Callback {
                        override fun onChangeValue(newValue: Boolean) {
                            visibility = newValue
                        }
                    })

                BaseLazyColumnScroll(modifier = Modifier
                    .fillMaxWidth(), callback = object : VisibleCallback() {
                    override fun onChangeVisible(visible: Boolean) {
                        visibleAnimation = visible
                    }
                }, content = {
                    itemsIndexed(marketItemCollection) { index, marketItem ->
                        val itemListAndCategory = marketItem.itemListAndCategory
                        val itemListCurrent = marketItem.itemListAndCategory.itemList
                        val category = marketItem.itemListAndCategory.category
                        val idItemAndCategory = itemListAndCategory.itemList.id

                        val isCheck = itemCheckCollection.indexOf(itemListCurrent.id) != -1
                        SlidingItemListComponent(
                            context,
                            itemListAndCategory,
                            isCheck,
                            true,
                            isRemoved = itemListCurrent.isRemoved,
                            sizeCheckCollection = true,
                            idItem = itemListCurrent.id,
                            category = category,
                            product = itemListCurrent.item,
                            price = marketItem.price,
                            quantOrKilo = marketItem.amount,
                            type = marketItem.type,
                            backgroundColor = if (index % 2 == 0) background_card_gray_light else background_card_light,
                            callback = object : CallbackItemList {
                                override fun onChangeValue(idCard: Long) {
                                    val isChecked = itemCheckCollection.indexOf(idCard) != -1

                                    if (isChecked) {
                                        valueTotal -= (marketItem.price * if(marketItem.type == TypeProduct.KILO) 1 else marketItem.amount.toInt())
                                        itemCheckCollection.remove(idCard)
                                        checkAll = false
                                    } else if (checkFieldProduct(marketItem)) {
                                        valueTotal += (marketItem.price * if(marketItem.type == TypeProduct.KILO) 1 else marketItem.amount.toInt())
                                        itemCheckCollection.add(idCard)
                                        checkAll =
                                            itemCheckCollection.size == marketItemCollection.size

                                    }

                                }
                            },
                            callbackPrice = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {
                                    updatePrice(
                                        idItemAndCategory,
                                        MaskUtils.convertValueStringToDouble(newValue).toFloat()
                                    )

                                }

                            }, callbackQuantOrKilo = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {
                                    updateAmount(idItemAndCategory, newValue)
                                }

                                override fun onChangeTypeProduct(newProduct: TypeProduct) {
                                    updateTypeProduct(idItemAndCategory, newProduct)
                                }

                            }
                        )
                    }
                })
            }
        })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogSaveProduct(
    visibility: Boolean,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    marketItemCollection: List<MarketItem>,
    itemCheckCollection: List<Long>,
    callback: Callback
) {
    val registerTextFieldViewModel: RegisterTextFieldViewModel = viewModel()
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
    val cardCreditCollection = remember {
        mutableStateListOf<CreditCard>()
    }
    var visibilityDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val purchaseViewModel = PurchaseViewModel(context)
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)

    LaunchedEffect(Unit) {
        creditCardViewModel.getAll()
    }

    LaunchedEffect(key1 = visibility) {
        visibilityDialog = visibility
    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner) {
        cardCreditCollection.removeAll(cardCreditCollection)
        cardCreditCollection.addAll(it)

        if (it.isNotEmpty()) {
            val idInitial = it[0].id
            registerTextFieldViewModel.idCard.value = idInitial
        }
    }

    fun checkFields(): Boolean {
        if (registerTextFieldViewModel.locale.value!!.isNotBlank()) {
            return true
        }

        registerTextFieldViewModel.localeError.value = true

        return false
    }

    suspend fun savePurchases() {

        val purchaserSaveCoroutine = coroutineScope.async {
            val purchaseCollection = marketItemCollection.filter { itemCheckCollection.indexOf(it.itemListAndCategory.itemList.id) != -1 } .map {
                val itemList = it.itemListAndCategory.itemList
                val category = it.itemListAndCategory.category

                val purchase = Purchase(
                    itemList.item,
                    registerTextFieldViewModel.locale.value!!,
                    itemList.creditCardOwnerIdItem,
                    it.amount,
                    it.type,
                    registerTextFieldViewModel.dateCurrent.value!!,
                    MaskUtils.convertValueStringToDouble(
                        it.price.toString()
                    ),
                    category.id
                )
                purchase
            }

            purchaseViewModel.insertPurchase(purchaseCollection)

            val itemRemovedCollection = marketItemCollection.filter { itemCheckCollection.indexOf(it.itemListAndCategory.itemList.id) != -1 && it.itemListAndCategory.itemList.isRemoved }.map{it.itemListAndCategory.itemList}

            itemRemovedCollection.forEach {  itemListViewModel.deleteItemList(it) }
        }

        purchaserSaveCoroutine.await()

        visibilityDialog = false
        navController.popBackStack()
    }

    if (visibilityDialog) {
        Card(
            backgroundColor = background_card_gray_light, modifier = Modifier
                .fillMaxWidth()
        ) {
            Dialog(
                onDismissRequest = { },
                content = {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                    ) {
                        Card(
                            backgroundColor = background_card, modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Spacer(
                                        Modifier
                                            .height(25.dp)
                                    )

                                    Text(
                                        "Salvar produtos",
                                        fontFamily = LatoRegular,
                                        color = primary_dark,
                                        fontSize = 18.sp
                                    )

                                    Spacer(
                                        Modifier
                                            .height(25.dp)
                                    )

                                    Divider(
                                        color = text_primary,
                                        modifier = Modifier
                                            .height(1.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .background(text_title_secondary)
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Warning,
                                        contentDescription = null,
                                        tint = text_secondary,
                                        modifier = Modifier.padding(start = 10.dp),
                                    )

                                    Text(
                                        "Os produtos salvos serão adicionados\naltomaticamente nas compras.",
                                        fontFamily = LatoRegular,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(start = 10.dp),
                                    )

                                }

                                Column(Modifier.padding(horizontal = 6.dp)) {
                                    Spacer(
                                        Modifier
                                            .height(30.dp)
                                    )

                                    Text(
                                        "Pagamento",
                                        fontFamily = LatoRegular,
                                        fontSize = 14.sp,
                                    )

                                    Spacer(
                                        Modifier
                                            .height(10.dp)
                                    )

                                    Divider(
                                        color = divider,
                                        modifier = Modifier
                                            .height(1.dp)
                                    )

                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        TextInputComponent(
                                            textColor = text_primary,
                                            label = "Local",
                                            value = registerTextFieldViewModel.locale.observeAsState().value!!,
                                            reset = false,
                                            modifier = Modifier.fillMaxWidth(.58f),
                                            maxChar = 30,
                                            isCountChar = true,
                                            error = registerTextFieldViewModel.localeError.observeAsState().value,
                                            customOnClick = object : CustomTextFieldOnClick {
                                                override fun onChangeValue(newValue: String) {
                                                    registerTextFieldViewModel.onChangeLocale(
                                                        newValue
                                                    )
                                                }

                                            })
                                        DatePickerCustom(
                                            registerTextFieldViewModel,
                                            backgroundColor = background_text_field,
                                            reset = false,
                                            isEnableClick = false,
                                            context = context
                                        )
                                    }
                                    CustomDropdownMenu(
                                        backgroundColor = background_text_field,
                                        idCardEditable = registerTextFieldViewModel.idCard.observeAsState().value,
                                        valueCollection = getNameCard(cardCreditCollection),
                                        error = false,
                                        isEnableClick = true,
                                        callback = object : CustomTextFieldOnClick {
                                            override fun onChangeValueLong(newValue: Long) {
                                                registerTextFieldViewModel.onChangeIdCard(newValue)
                                            }
                                        }, reset = false
                                    )

                                    Spacer(
                                        Modifier
                                            .height(35.dp)
                                    )

                                    ButtonsFooterContent(
                                        isClickable = true,
                                        btnTextCancel = "Cancelar",
                                        btnTextAccept = "SALVAR",
                                        onClickCancel = {
                                            visibilityDialog = false
                                            callback.onChangeValue(visibilityDialog)
                                        },
                                        onClickAccept = {
                                            if (checkFields()) {
                                                coroutineScope.launch {
                                                    savePurchases()
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    }
                })
        }
    }
}