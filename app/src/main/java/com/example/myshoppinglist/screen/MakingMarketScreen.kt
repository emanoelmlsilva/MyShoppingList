package com.example.myshoppinglist.screen

import DialogRecoveryItemList
import DialogRegisterItemList
import android.content.Context
import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.*
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
import com.example.myshoppinglist.enums.FilterFabState
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.fieldViewModel.MarketItemFieldViewModel
import com.example.myshoppinglist.fieldViewModel.RegisterTextFieldViewModel
import com.example.myshoppinglist.model.LocationAndDate
import com.example.myshoppinglist.services.controller.ItemListController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Composable
fun MakingMarketScreen(
    navController: NavHostController,
    idCard: Long,
    marketItemFieldViewModel: MarketItemFieldViewModel,
) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)


    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val marketItemCollection by marketItemFieldViewModel.marketItemCollection.observeAsState(
        emptyList()
    )
    val itemListController = ItemListController.getData(context, lifecycleOwner)
    val visibleLoading by marketItemFieldViewModel.visibleLoading.observeAsState(false)

    val purchaseController = PurchaseController.getData(context, lifecycleOwner)

    var filterFabState by rememberSaveable() {
        mutableStateOf(FilterFabState.DEFAULT)
    }

    var checkAll by remember { mutableStateOf(false) }

    var visibility by remember {
        mutableStateOf(false)
    }
    var enabledDialog by remember {
        mutableStateOf(false)
    }
    var valueTotal by remember {
        mutableStateOf(0F)
    }
    var visibilityBackHandler by remember {
        mutableStateOf(false)
    }
    var updateItemList by remember {
        mutableStateOf(false)
    }
    var visibilityShowDialog by remember {
        mutableStateOf(false)
    }
    var enabledDialogList by remember {
        mutableStateOf(false)
    }
    var visibilityActions by remember {
        mutableStateOf(true)
    }

    var visibleWaiting by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    val callback = object : CallbackObject<ItemListDTO> {
        override fun onSuccess() {
            navController.popBackStack()
        }

        override fun onSuccess(objects: ItemListDTO) {
            messageError = MeasureTimeService.messageWaitService
            visibleWaiting = false
            marketItemFieldViewModel.startLoading()
            marketItemFieldViewModel.updateJoinMarketItemCollection(listOf(objects))
            enabledDialog = false
        }

        override fun onCancel() {
            visibility = false
            enabledDialog = false
        }

        override fun onFailed(messageError: String) {
            super.onFailed(messageError)
        }

        override fun onClick() {
            visibleWaiting = true
        }

        override fun onChangeValue(newValue: Boolean) {
            visibleWaiting = newValue
        }

        override fun onChangeValue(newValue: String) {
            messageError = newValue
        }
    }

    fun savePurchases(locale: String, date: String, callback: Callback) {
        val email = UserLoggedShared.getEmailUserCurrent()

        val purchases =
            marketItemCollection.filter { itemCheckCollection.indexOf(it.itemListDTO.myShoppingId) != -1 }
                .map {
                    val itemList = it.itemListDTO
                    val category = it.itemListDTO.categoryDTO

                    val purchase = Purchase(
                        itemList.item,
                        locale,
                        itemList.creditCardDTO.id,
                        it.amount,
                        it.type,
                        date,
                        MaskUtils.convertValueStringToDouble(
                            it.price.toString()
                        ),
                        category.myShoppingId,
                        email,
                        MaskUtils.convertValueStringToDouble(it.discount.toString())
                    )
                    PurchaseDTO(
                        purchase,
                        category.toCategory(),
                        itemList.creditCardDTO.toCreditCard()
                    )
                }

        purchaseController.savePurchases(purchases, callback)
    }

    BackHandler {
        visibilityBackHandler = true
    }

    LaunchedEffect(Unit) {
        if (marketItemFieldViewModel.marketItemCollection.value!!.isEmpty()) {
            val arguments = navController.previousBackStackEntry
                ?.arguments


            val recoverMarketItem =
                arguments?.getParcelableArrayList<ItemListDTO>("itemListChoiceCollection")!!

            marketItemFieldViewModel.updateItemListCollection(recoverMarketItem)
        }
    }

    val callbackFabButton = object : CallbackObject<FilterFabState> {
        override fun onSuccess(newFilterFabState: FilterFabState) {
            filterFabState = newFilterFabState
        }

        override fun onClick() {
            super.onClick()
        }
    }

    LaunchedEffect(key1 = marketItemCollection.size) {

        marketItemFieldViewModel.updateVisibleLoading(marketItemCollection.isEmpty())
    }

    TopAppBarScreen(
        hasDoneButton = true,
        contentHeader = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    backgroundColor = primary_dark,
                    shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
                ) {
                    Text(
                        "R$ ${
                            MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(valueTotal.toDouble()))
                        }",
                        fontFamily = LatoBlack,
                        fontSize = 24.sp,
                        color = text_secondary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        hasBackButton = false,
        hasToolbar = true,
        onClickIcon = { visibilityBackHandler = true },
        onClickIconDone = {
            if (itemCheckCollection.isNotEmpty()) {
                visibilityShowDialog = true
            } else {
                CustomToastComponent(context, "Selecione algum item da lista de mercado")
            }
        },
        paddingFloatingButton = 12.dp,
        floatingActionButton = {
            if (visibilityActions) {

                val floatingFabMenuCollection =

                    listOf<@Composable (animatable: Animatable<Float, AnimationVector1D>, modifier: Modifier) -> Unit>(
                        { animatable, modifier ->
                            ExtendedFloatingActionButton(
                                modifier = modifier,
                                icon = {
                                    Icon(
                                        Icons.Filled.Add,
                                        null,
                                        tint = primary_dark
                                    )
                                },
                                backgroundColor = text_secondary,
                                onClick = {
                                    filterFabState = FilterFabState.COLLAPSED
                                    enabledDialog = true
                                },
                                text = { Text("novo item".capitalize(), color = text_primary) }
                            )
                        },

                        { animatable, modifier ->
                            ExtendedFloatingActionButton(
                                modifier = modifier,
                                backgroundColor = text_secondary,
                                onClick = {
                                    filterFabState = FilterFabState.COLLAPSED
                                    enabledDialogList = true
                                },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(id = R.drawable.ic_baseline_playlist_add_24),
                                        null,
                                        tint = primary_dark
                                    )
                                },
                                text = { Text("adicionar".capitalize(), color = text_primary) })
                        })

                FilterMultipleFabMenuButton(
                    filterFabState,
                    callbackFabButton,
                    floatingFabMenuCollection
                )

            }

        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

                LoadingComposable(visibleLoading)

                WaitingProcessComponent(visibleWaiting, messageError, callback)

                DialogRecoveryItemList(
                    context,
                    lifecycleOwner,
                    idCard,
                    enabledDialogList,
                    marketItemCollection!!.map { it.itemListDTO },
                    object :
                        CallbackItemList {
                        override fun onInsert(itemListDTO: ItemListDTO) {

                        }

                        override fun onChangeValue(newValue: Boolean) {
                            enabledDialogList = newValue
                        }

                        override fun onUpdate(itemList: ItemListDTO) {
//                            val marketItem =
//                                MarketItem(0F, 0F, "0", TypeProduct.QUANTITY, false, itemList)
//                            marketItemCollection.add(marketItem)
                        }

                        override fun onUpdate(itemList: List<ItemListDTO>) {
                            marketItemFieldViewModel.startLoading()
                            marketItemFieldViewModel.updateJoinMarketItemCollection(itemList)
//                            itemListCollection.forEach { itemList ->
//                                val marketItem =
//                                    MarketItem(0F, 0F, "0", TypeProduct.QUANTITY, false, itemList)
//                                marketItemCollection.add(marketItem)
//                            }
                        }
                    })

                DialogShowPurchase(context,
                    visibilityShowDialog,
                    MaskUtils.convertValueDoubleToString(valueTotal.toDouble()),
                    marketItemCollection,
                    object : Callback {
                        override fun onSuccess() {
                            visibility = true
                        }

                        override fun onCancel() {
                            visibilityShowDialog = false
                        }
                    })

                DialogBackCustom(visibilityBackHandler, {
                    visibilityBackHandler = false
                    navController.popBackStack()
                }, {
                    visibilityBackHandler = false
                }, "Sair", "Os dados adicionados serão perdidos!\nTem certeza que deseja sair?")

                DialogLocationAndDate(context = context, visibility = visibility, callback = object : CallbackObject<LocationAndDate>{

                    override fun onCancel() {
                        visibility = false
                    }

                    override fun onSuccess(locationAndDate: LocationAndDate) {
                        savePurchases(locationAndDate.location, locationAndDate.date, callback)
                    }
                })

                DialogRegisterItemList(
                    context,
                    lifecycleOwner,
                    enabledDialog,
                    null,
                    object : CallbackItemList {
                        override fun onUpdate(itemList: ItemListDTO) {

                        }

                        override fun onInsert(itemList: ItemListDTO) {

                            if (itemList.creditCardDTO.idCard == 0L) {
                                itemList.creditCardDTO.idCard = idCard
                            }
                            itemListController.saveItemList(itemList, callback)
                        }

                        override fun onClick() {
                            enabledDialog = false
                        }

                        override fun onChangeValue(newValue: Boolean) {
                            callback.onChangeValue(newValue)
                        }

                        override fun onChangeValue(newValue: String) {
                            callback.onChangeValue(newValue)
                        }
                    })

                if (marketItemCollection.isNotEmpty() && !visibleLoading) {
                    BaseLazyColumnScroll(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = {
                            itemsIndexed(marketItemCollection) { index, marketItem ->
                                val itemListDTO = marketItem.itemListDTO
                                val itemListCurrent = marketItem.itemListDTO
                                val category = marketItem.itemListDTO.categoryDTO
                                val idItemAndCategory = itemListDTO.myShoppingId

                                val isCheck =
                                    itemCheckCollection.indexOf(itemListCurrent.myShoppingId) != -1
                                Column(modifier = Modifier.padding(bottom = if (index == (marketItemCollection.size - 1)) 110.dp else 0.dp)) {

                                    SlidingItemListComponent(
                                        context,
                                        itemListDTO,
                                        isCheck,
                                        true,
                                        isRemoved = itemListCurrent.isRemoved,
                                        hasOptionEdit = false,
                                        idItem = itemListCurrent.myShoppingId,
                                        category = category.toCategoryApi(),
                                        product = itemListCurrent.item,
                                        price = marketItem.price,
                                        discount = marketItem.discount,
                                        isCheckDiscount = marketItem.isCheckDiscount,
                                        type = marketItem.type,
                                        amountOrKilo = marketItem.amount,
                                        callback = object : CallbackItemList {
                                            override fun onUpdate(itemList: ItemListDTO) {

                                            }

                                            override fun onChangeValue(idCard: Long) {
                                                val isChecked =
                                                    itemCheckCollection.indexOf(idCard) != -1

                                                if (isChecked) {
                                                    valueTotal -= (marketItem.price * if (marketItem.type == TypeProduct.KILO) 1 else marketItem.amount.toInt())
                                                    itemCheckCollection.remove(idCard)
                                                    checkAll = false
                                                } else if (checkFieldProduct(marketItem)) {
                                                    valueTotal += (marketItem.price * if (marketItem.type == TypeProduct.KILO) 1 else marketItem.amount.toInt())
                                                    itemCheckCollection.add(idCard)
                                                    checkAll =
                                                        itemCheckCollection.size == marketItemCollection.size

                                                }
                                            }
                                        },
                                        callbackPrice = object : CustomTextFieldOnClick {
                                            override fun onChangeValue(newValue: String) {
                                                marketItemFieldViewModel.updatePrice(
                                                    idItemAndCategory,
                                                    MaskUtils.convertValueStringToDouble(newValue)
                                                        .toFloat()
                                                )
                                            }

                                        }, callbackQuantOrKilo = object : CustomTextFieldOnClick {
                                            override fun onChangeValue(newValue: String) {
                                                marketItemFieldViewModel.updateAmount(
                                                    idItemAndCategory,
                                                    newValue
                                                )
                                            }

                                            override fun onChangeTypeProduct(newProduct: TypeProduct) {
                                                marketItemFieldViewModel.updateTypeProduct(
                                                    idItemAndCategory,
                                                    newProduct
                                                )
                                            }

                                        },
                                        callbackDiscount = object : CustomTextFieldOnClick {
                                            override fun onChangeValue(newValue: String) {
                                                marketItemFieldViewModel.updateDiscount(
                                                    idItemAndCategory,
                                                    MaskUtils.convertValueStringToDouble(newValue)
                                                        .toFloat()
                                                )
                                            }

                                            override fun onChangeValue(newValue: Boolean) {
                                                marketItemFieldViewModel.updateCheckDiscount(
                                                    idItemAndCategory,
                                                    newValue
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        })
                }

            }
        })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogShowPurchase(
    context: Context,
    visibility: Boolean,
    valueTotal: String,
    marketItemCollection: List<MarketItem>,
    callback: Callback
) {

    val valuesMarketCollection = marketItemCollection.filter { it.price.compareTo(0) > 0 }.toList()

    if (visibility) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { },
            content = {
                Surface(
                    color = MaterialTheme.colors.background,
                    contentColor = contentColorFor(text_secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(background_card),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(
                            Modifier
                                .height(30.dp)
                        )

                        Text(
                            "Comprar Items R$ ${MaskUtils.maskValue(valueTotal)}",
                            fontFamily = LatoBlack,
                            color = primary_dark,
                            fontSize = 18.sp
                        )

                        Spacer(
                            Modifier
                                .height(30.dp)
                        )

                        Divider(
                            color = divider,
                            modifier = Modifier
                                .height(1.dp)
                        )

                        BaseLazyColumnScroll(verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.9f)
                                .background(background_card),
                            content = {
                                items(valuesMarketCollection) { marketItem ->
                                    val category = marketItem.itemListDTO.categoryDTO
                                    val itemList = marketItem.itemListDTO

                                    Column() {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            IconCategoryComponent(
                                                modifier = Modifier.padding(start = 6.dp),
                                                iconCategory = AssetsUtils.readIconBitmapById(
                                                    context,
                                                    category.idImage
                                                )!!
                                                    .asImageBitmap(),
                                                colorIcon = Color(category.color),
                                                size = 40.dp,
                                                enabledBackground = true
                                            )
                                            Column(horizontalAlignment = Alignment.End) {
                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        fontFamily = LatoRegular,
                                                        text = itemList.item, modifier = Modifier
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
                                                            text = "${if (marketItem.type == TypeProduct.QUANTITY) "x" else ""} ${marketItem.amount} ${if (marketItem.type == TypeProduct.QUANTITY) "UN" else "Kg"}"
                                                        )
                                                        Text(
                                                            fontFamily = LatoBold,
                                                            color = text_primary_light,
                                                            text = "R$ ${
                                                                MaskUtils.maskValue(
                                                                    MaskUtils.convertValueDoubleToString(marketItem.price.toDouble())
                                                                )
                                                            }",
                                                            modifier = Modifier
                                                                .padding(start = 12.dp),
                                                        )
                                                    }
                                                }

                                                if (marketItem.discount > 0) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(top = 4.dp)
                                                    ) {
                                                        Text(
                                                            fontFamily = LatoRegular,
                                                            text = "desconto", modifier = Modifier,
                                                            textAlign = TextAlign.Start,
                                                            fontSize = 12.sp,
                                                        )
                                                        Text(
                                                            modifier = Modifier.padding(start = 10.dp),
                                                            fontFamily = LatoRegular,
                                                            fontSize = 12.sp,
                                                            text = "R$ -${
                                                                MaskUtils.maskValue(
                                                                    MaskUtils.convertValueDoubleToString(marketItem.discount.toDouble())
                                                                )
                                                            }"
                                                        )
                                                        Text(
                                                            modifier = Modifier.padding(start = 10.dp),
                                                            fontFamily = LatoBold,
                                                            color = text_primary_light,
                                                            text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString((marketItem.price - marketItem.discount).toDouble()))}",
                                                        )

                                                    }
                                                }
                                            }
                                        }

                                        Divider(
                                            color = divider,
                                            modifier = Modifier
                                                .height(1.dp)
                                        )
                                    }
                                }
                            })

                        ButtonsFooterContent(
                            modifier = Modifier.fillMaxWidth(.9f),
                            btnTextAccept = "Continuar",
                            onClickAccept = {
                                callback.onSuccess()
                            },
                            btnTextCancel = "Cancelar",
                            onClickCancel = {
                                callback.onCancel()
                            })
                    }

                }
            })
    }
}


@Parcelize
class MarketItem(
    var price: Float,
    var discount: Float,
    var amount: String,
    var type: TypeProduct,
    var isCheckDiscount: Boolean,
    var itemListDTO: ItemListDTO
) : Parcelable {
    override fun toString(): String {
        return "MarketItem(price=$price, amount='$amount', type=$type, itemListAndCategory=$itemListDTO)"
    }
}

fun checkFieldProduct(marketItem: MarketItem): Boolean {
    return marketItem.amount.isNotBlank() && marketItem.amount != "0" && marketItem.price > 0 && (!marketItem.isCheckDiscount || marketItem.discount > 0F)
}