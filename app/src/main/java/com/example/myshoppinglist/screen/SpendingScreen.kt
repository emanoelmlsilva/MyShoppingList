package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.*
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.PurchaseAndCategoryInfo
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.*
import com.example.myshoppinglist.database.dtos.PurchaseDTO as PurchaseDaoDTO

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SpendingScreen(navController: NavHostController?, idCard: Long) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val purchaseController: PurchaseController = PurchaseController.getData(context, lifecycleOwner)
    val creditCardController: CreditCardController =
        CreditCardController.getData(context, lifecycleOwner)
    val spendingTextFieldViewModel = SpendingTextFieldViewModel()
    val purchaseInfoCollection = remember { mutableStateListOf<PurchaseAndCategoryInfo>() }
    var price by remember { mutableStateOf(0.0) }
    val monthsCollection = remember { mutableStateListOf<String>() }
    val creditCardCollection = remember { mutableListOf<CreditCard>() }
    var currentCreditCard by remember { mutableStateOf<CreditCard?>(null) }
    var visibleAnimation by remember { mutableStateOf(true) }
    var idPurchaseEdit by remember { mutableStateOf(0L) }
    var visibilityDialog by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(false) }
    var purchaseCurrent by remember { mutableStateOf(Purchase()) }
    var visibilityBackHandler by remember { mutableStateOf(false) }
    val categoryCollection = remember { mutableListOf<Category>() }
    var resetMonth by remember { mutableStateOf(false) }

    var visibleWaiting by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    fun reset() {
        idPurchaseEdit = 0L
        price = 0.0
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        monthsCollection.removeAll(monthsCollection)
        messageError = MeasureTimeService.messageWaitService
    }

    val callback = object : CallbackObject<ItemListDTO> {
        override fun onSuccess() {

            if (visibleWaiting) {
                MeasureTimeService.resetMeasureTime(object : Callback {
                    override fun onChangeValue(newValue: Boolean) {
                        resetMonth = false
                        visibilityBackHandler = false
                        visibleWaiting = false
                        reset()
                        reload = true
                    }
                })
            } else {
                reset()
                reload = true
            }

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

    fun getInforPurchaseByMonth(month: String) {
        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(month)
        val idCard = currentCreditCard!!.myShoppingId

        purchaseController.getPurchaseByMonthDB(idCard, "$monthAndYearNumber-")
            .observe(lifecycleOwner) { purchaseAndCategoryCollection ->

                categoryCollection.addAll(purchaseAndCategoryCollection.map { it.category })

                val purchaseInfoFormattedCollection: MutableList<PurchaseAndCategoryInfo> =
                    purchaseAndCategoryCollection.groupBy { it.purchase.date }.map { group ->
                        PurchaseAndCategoryInfo(
                            group.key,
                            group.value.reversed().toMutableList()
                        )
                    } as MutableList<PurchaseAndCategoryInfo>

                purchaseInfoCollection.removeAll(purchaseInfoCollection)

                spendingTextFieldViewModel.onChangePurchaseInfoCollection(
                    purchaseInfoFormattedCollection
                )

            }

        price = purchaseController.sumPriceByMonthDB(idCard, "$monthAndYearNumber-")
    }

    LaunchedEffect(key1 = idCard, key2 = reload) {

        idPurchaseEdit = 0L

        if (idCard != 0L || reload) {
            creditCardController.findAllDB().observe(lifecycleOwner) {
                creditCardCollection.removeAll(creditCardCollection)
                creditCardCollection.addAll(it)
            }

            creditCardController.findCreditCardByIdDB(idCard).observe(lifecycleOwner) {
                currentCreditCard = it

            }
        }

        reload = false
    }

    spendingTextFieldViewModel.purchaseInfoCollection.observeForever {
        purchaseInfoCollection.addAll(it)
    }

    TopAppBarScreen(
        hasBackButton = true,
        hasDoneButton = false,
        hasToolbar = true,
        onClickIcon = { navController?.popBackStack() },
        content = {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                WaitingProcessComponent(visibleWaiting, messageError, callback)

                DialogBackCustom(
                    visibilityBackHandler,
                    {
                        MeasureTimeService.startMeasureTime(callback)
                        resetMonth = true
                        purchaseController.deletePurchase(
                            purchaseCurrent.idPurchaseApi,
                            purchaseCurrent.myShoppingId,
                            callback
                        )


                    },
                    {
                        visibilityBackHandler = false
                    },
                    "Deseja apagar compra do historico?",
                    context.getString(R.string.delete_message, purchaseCurrent.name)
                )

                BoxSpendingFromMonthComponent(
                    lifecycleOwner,
                    purchaseController,
                    price,
                    currentCreditCard,
                    creditCardCollection,
                    object :
                        CallbackCreditCard {
                        override fun onChangeValueCreditCard(creditCard: CreditCard) {
                            currentCreditCard = creditCard
                            reset()

                        }

                        override fun onChangeDataMonth(month: String) {
                            getInforPurchaseByMonth(month)
                        }

                        override fun onChangeValue(newValue: String) {
                            if (newValue.isNotBlank()) {
                                getInforPurchaseByMonth(newValue)
                            }
                        }
                    })

                Spacer(
                    Modifier
                        .height(35.dp)
                )

                BaseAnimationComponent(
                    visibleAnimation = visibleAnimation,
                    contentBase = {
                        Column {
                            Row {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Card(modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape),
                                        backgroundColor = background_card,
                                        onClick = { navController!!.navigate("${Screen.RegisterPurchase.name}?idCardCurrent=${currentCreditCard?.myShoppingId}?isEditable=${false}?purchaseEdit=${""}") }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_outline_shopping_bag_24),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(ButtonDefaults.IconSize)
                                                .padding(18.dp),
                                        )
                                    }
                                    Text(
                                        text = "Comprar",
                                        fontFamily = LatoBlack,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                Spacer(
                                    Modifier
                                        .width(20.dp)
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Card(modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape),
                                        backgroundColor = background_card,
                                        onClick = { navController?.navigate("${Screen.ListPurchase.name}?idCard=${currentCreditCard?.myShoppingId ?: idCard}") }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.list_view),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(ButtonDefaults.IconSize)
                                                .padding(18.dp),
                                        )
                                    }
                                    Text(
                                        text = "Lista Mercado",
                                        fontFamily = LatoBlack,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                            Spacer(
                                Modifier
                                    .height(15.dp)
                            )

                            Divider(
                                color = divider,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )

                            DialogTransferPurchaseComponent(
                                context,
                                visibilityDialog,
                                idCard,
                                purchaseCurrent,
                                object : CallbackOptions {
                                    override fun onTransfer(
                                        value: Boolean,
                                        purchase: Purchase,
                                        idCardCurrent: Long
                                    ) {
                                        visibilityDialog = false
                                        if (idCardCurrent != 0L && purchase.purchaseCardId != idCardCurrent) {
                                            MeasureTimeService.startMeasureTime(callback)

                                            val creditCard =
                                                creditCardCollection.find { it.myShoppingId == idCardCurrent }

                                            val category =
                                                categoryCollection.find { it.myShoppingId == purchase.categoryOwnerId }

                                            purchase.purchaseCardId = idCardCurrent

                                            val purchaseDTO = PurchaseDTO(
                                                purchase, category!!,
                                                creditCard!!
                                            )

                                            purchaseController.updatePurchase(
                                                purchaseDTO,
                                                callback
                                            )

                                        }

                                    }

                                    override fun onEditable(idCardCurrent: Long) {
                                        navController!!.navigate("${Screen.RegisterPurchase.name}?idCardCurrent=${idCardCurrent}?isEditable=${true}?purchaseEdit=${""}")
                                    }
                                })
                        }
                    })

                if (purchaseInfoCollection.isNotEmpty()) {
                    BaseLazyColumnScroll(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth(),
                        callback = object : VisibleCallback {
                            override fun onChangeVisible(visible: Boolean) {
                                if (visibleAnimation != visible) {
                                    visibleAnimation = visible
                                }
                            }
                        }
                    ) {
                        purchaseInfoCollection.map { purchaseInfo ->
                            item {
                                Text(
                                    text = FormatUtils().getNameDay(purchaseInfo.title)
                                        .capitalize(),
                                    modifier = Modifier.padding(start = 8.dp, top = 24.dp),
                                    color = text_title_secondary
                                )
                            }

                            items(purchaseInfo.purchaseCollection) { purchase ->
                                BoxPurchaseSpendingComponent(purchase, idPurchaseEdit, object : Callback {
                                    override fun onClick() {
                                        idPurchaseEdit =
                                            if (idPurchaseEdit == 0L || idPurchaseEdit != purchase.purchase.myShoppingId) {
                                                purchase.purchase.myShoppingId
                                            } else {
                                                0L
                                            }

                                        purchaseCurrent = purchase.purchase
                                    }
                                },
                                    object : CallbackOptions {
                                        override fun onTransfer(
                                            value: Boolean,
                                            purchase: Purchase,
                                            idCardCurrent: Long
                                        ) {
                                            visibilityDialog = value
                                        }

                                        override fun onDelete() {
                                            visibilityBackHandler = true
                                        }

                                        override fun onEditable(idCardCurrent: Long) {
                                            navController?.navigate(
                                                "${Screen.RegisterPurchase.name}?idCardCurrent=${idCardCurrent}?isEditable=${true}?purchaseEdit=${
                                                    ConversionUtils<PurchaseDaoDTO>(
                                                        PurchaseDaoDTO::class.java
                                                    ).toJson(PurchaseDaoDTO(purchaseCurrent))
                                                }"
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }
            }

        })
}

class SpendingTextFieldViewModel : BaseFieldViewModel() {

    val purchaseInfoCollection: MutableLiveData<MutableList<PurchaseAndCategoryInfo>> =
        MutableLiveData<MutableList<PurchaseAndCategoryInfo>>(
            mutableListOf()
        )

    fun onChangePurchaseInfoCollection(newPurchaseInfo: MutableList<PurchaseAndCategoryInfo>) {
        purchaseInfoCollection.value = newPurchaseInfo
    }

    override fun checkFields(): Boolean {
        return false
    }
}