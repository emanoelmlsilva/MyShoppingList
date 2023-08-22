package com.example.myshoppinglist.screen

import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.*
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.model.PurchaseAndCategoryInfo
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.ConversionUtils
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

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
    var monthCurrent by remember { mutableStateOf("") }
    val creditCardCollection = remember { mutableListOf<CreditCard>() }
    var currentCreditCard by remember { mutableStateOf<CreditCard?>(null) }
    var visibleAnimation by remember { mutableStateOf(true) }
    var idPurchaseEdit by remember { mutableStateOf(0L) }
    var visibilityDialog by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(false) }
    var purchaseCurrent by remember { mutableStateOf(Purchase()) }
    var visibilityBackHandler by remember { mutableStateOf(false) }

    fun reset() {
        idPurchaseEdit = 0L
        monthCurrent = ""
        price = 0.0
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        monthsCollection.removeAll(monthsCollection)
    }

    fun getInforPurchaseByMonth(month: String) {
        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(month)
        val idCard = currentCreditCard!!.myShoppingId

        purchaseController.getPurchaseByMonthDB(idCard, "$monthAndYearNumber-")
            .observe(lifecycleOwner) { purchaseAndCategoryCollection ->

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

        creditCardController.findAllDB().observe(lifecycleOwner) {
            creditCardCollection.removeAll(it)
            creditCardCollection.addAll(it)
        }

        creditCardController.findCreditCardByIdDB(idCard).observe(lifecycleOwner) {
            currentCreditCard = it

        }

    }

    spendingTextFieldViewModel.monthCurrent.observeForever {
        if (it.isNotBlank()) {
            getInforPurchaseByMonth(it)
        }
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
                DialogBackCustom(
                    visibilityBackHandler,
                    {
                        visibilityBackHandler = false

                        purchaseController.deletePurchase(
                            purchaseCurrent.idPurchaseApi,
                            purchaseCurrent.myShoppingId,
                            object : Callback {
                                override fun onSuccess() {
                                    reset()

                                    reload = true
                                }

                                override fun onFailed(messageError: String) {
                                    super.onFailed(messageError)
                                }
                            })


                    },
                    {
                        visibilityBackHandler = false
                    },
                    "Deseja apagar compra do historico?",
                    context.getString(R.string.delete_message, purchaseCurrent.name)
                )

                BoxSpendingFromMonth(
                    lifecycleOwner,
                    purchaseController,
                    spendingTextFieldViewModel,
                    price,
                    currentCreditCard,
                    creditCardCollection,
                    object :
                        CallbackCreditCard {
                        override fun onChangeValueCreditCard(creditCard: CreditCard) {
                            currentCreditCard = creditCard

//                            monthIdCard(currentCreditCard!!.myShoppingId)

                            reset()

                        }

                        override fun onChangeDataMonth(month: String) {
                            getInforPurchaseByMonth(month)
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

                            DialogTransferPurchase(
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
                                            purchase.purchaseCardId = idCardCurrent
//                                            purchaseViewModel.updatePurchase(purchase)
                                            reset()
                                            reload = true
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
                                BoxPurchaseSpending(purchase, idPurchaseEdit, object : Callback {
                                    override fun onClick() {
                                        Log.d(
                                            "TESTANDO",
                                            "idPurchaseEdit ${idPurchaseEdit}, purchase.purchase.myShoppingId ${purchase.purchase.idPurchaseApi}, TESTE ${idPurchaseEdit == 0L || idPurchaseEdit != purchase.purchase.myShoppingId}"
                                        )
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
                                                    ConversionUtils<PurchaseDTO>(
                                                        PurchaseDTO::class.java
                                                    ).toJson(PurchaseDTO(purchaseCurrent))
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

@Composable
fun BoxSpendingFromMonth(
    lifecycleOwner: LifecycleOwner,
    purchaseController: PurchaseController,
    spendingField: SpendingTextFieldViewModel,
    price: Double,
    currentCreditCard: CreditCard?,
    creditCards: List<CreditCard>,
    callbackCreditCard: CallbackCreditCard
) {
    val monthList = remember { mutableStateListOf<String>() }

    var monthCurrent by remember { mutableStateOf("") }

    fun monthIdCard(idCard: Long) {
        purchaseController.getMonthByIdCardDB(idCard).observe(lifecycleOwner) { months ->
            monthList.removeAll(monthList)

            val convertedMonth = months.groupBy {
                val separaterDate = it.split("-")
                "${separaterDate[0]}-${separaterDate.get(1)}"
            }.map { group -> FormatUtils().getMonth("${group.key}-01") }

            monthList.addAll(convertedMonth)

            val monthCurrentDefault = FormatUtils().getMonthCurrent()

            val isNotExist =
                months.find { FormatUtils().getMonth("${it}-01") == monthCurrentDefault } == null

            if (isNotExist && (monthCurrent.isBlank() || FormatUtils().getNumberMonth(
                    monthCurrentDefault
                )!!.toInt() > FormatUtils().getNumberMonth(monthCurrent)!!.toInt())
            ) {
                monthCurrent = FormatUtils().getNumberMonth(monthCurrentDefault).toString()
                val yearCurrent = FormatUtils().getYearCurrent()

                val formatMonth = if (monthCurrent.toInt() > 9) monthCurrent else "0$monthCurrent"

                monthCurrent = "$yearCurrent-$formatMonth-01"

                monthList.add(0, monthCurrentDefault)

            } else if (convertedMonth.isNotEmpty()) {
                val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(convertedMonth[0])

                monthCurrent = "$monthAndYearNumber-01"
            }

            val convertMonthNumberCurrent = FormatUtils().getMonth(monthCurrent)

            callbackCreditCard.onChangeDataMonth(convertMonthNumberCurrent)

        }

    }

    LaunchedEffect(key1 = currentCreditCard) {
        if (currentCreditCard != null) monthIdCard(currentCreditCard!!.myShoppingId)
    }

    spendingField.monthCurrent.observeForever {
        val hasMonths = monthList.isNotEmpty()
        monthCurrent = if (hasMonths) monthList[0] else ""

        if (it.isNotBlank()) monthCurrent = it else if (hasMonths) monthCurrent =
            monthList[0]
    }

    Column(verticalArrangement = Arrangement.Center) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "Gastos do mês",
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 16.sp,
                fontFamily = LatoBlack,
                color = text_primary_light
            )
            if (monthList.isNotEmpty()) {
                CustomDropDownMonth(
                    object : CustomTextFieldOnClick {
                        override fun onChangeValue(newValue: String) {
                            spendingField.onChangeMonth(newValue)
                        }
                    }, monthList, monthCurrent
                )
            }

        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(price))}",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            if (currentCreditCard != null) BoxDropdownCardCredit(
                creditCards,
                currentCreditCard,
                callbackCreditCard
            )
        }
    }
}

@Composable
fun CustomDropDownMonth(
    callback: CustomTextFieldOnClick,
    monthCollection: List<String>,
    monthCurrent: String,
    reset: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 0.dp, backgroundColor = background_card, modifier = Modifier
            .padding(6.dp, 16.dp, 16.dp, 16.dp)
            .clickable(onClick = {
                if (monthCollection.isNotEmpty()) {
                    expanded = true
                }
            })
    ) {

        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "- ${if (monthCollection.isNotEmpty()) monthCurrent else ""}",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = text_title_secondary
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            monthCollection.forEach { month ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    callback.onChangeValue(month)
                }) {
                    Text(text = month)
                }
            }
        }
    }

}

@Composable
fun BoxPurchaseSpending(
    purchaseAndCategory: PurchaseAndCategory,
    idPurchaseEdit: Long,
    callback: Callback,
    callbackOptions: CallbackOptions
) {
    val purchase = purchaseAndCategory.purchase ?: Purchase()
    val category = purchaseAndCategory.category ?: Category()
    val context = LocalContext.current
    val options = idPurchaseEdit != 0L && purchase.myShoppingId == idPurchaseEdit

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { callback.onClick() })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconCategoryComponent(
                iconCategory = AssetsUtils.readIconBitmapById(context, category.idImage)!!
                    .asImageBitmap(),
                size = 46.dp,
                colorIcon = Color(category.color),
                enabledBackground = true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp, top = if (purchase.discount > 0) {
                            24.dp
                        } else {
                            0.dp
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth(.25f)
                ) {
                    Text(
                        text = purchase.name.capitalize(),
                        fontFamily = LatoBlack,
                        color = text_primary_light,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = purchase.locale,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        fontSize = 12.sp,
                        color = text_title_secondary,
                        textAlign = TextAlign.Start
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "R$ ${
                            MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    purchase.price
                                )
                            )
                        }",
                        fontFamily = LatoBlack,
                        color = text_primary_light,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                    if (purchase.discount > 0) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth(.85f)
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                fontFamily = LatoRegular,
                                text = "desconto", modifier = Modifier,
                                textAlign = TextAlign.Start,
                                fontSize = 12.sp,
                            )
                            Text(
                                fontFamily = LatoRegular,
                                fontSize = 12.sp,
                                text = "R$ -${
                                    MaskUtils.maskValue(
                                        MaskUtils.convertValueDoubleToString(
                                            purchase.discount
                                        )
                                    )
                                }"
                            )
                            Text(
                                fontFamily = LatoBlack,
                                color = text_primary_light,
                                text = "R$ ${
                                    MaskUtils.maskValue(
                                        MaskUtils.convertValueDoubleToString(
                                            purchase.price - purchase.discount
                                        )
                                    )
                                }",
                            )

                        }
                    }
                    Text(
                        text = "${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                        color = text_title_secondary, modifier = Modifier
                            .padding(top = 8.dp), fontSize = 14.sp, textAlign = TextAlign.End
                    )
                }
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Options(
                options,
                purchase.myShoppingId,
                purchase.purchaseCardId,
                purchase.purchaseUserId,
                callbackOptions
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

@Composable
fun Options(
    options: Boolean,
    idPurchase: Long,
    idCard: Long,
    idUser: String,
    callback: CallbackOptions
) {
    if (options) {
        Card(
            modifier = Modifier.padding(bottom = 14.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = background_divider
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                IconButton(modifier = Modifier
                    .padding(end = 4.dp),
                    onClick = {
                        callback.onEditable(0L)
                    })
                {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = text_primary,
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .width(1.dp)
                        .height(45.dp)
                )

                IconButton(modifier = Modifier
                    .padding(horizontal = 4.dp), onClick = {
                    callback.onDelete()
                })
                {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = text_primary,
                    )
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .width(1.dp)
                        .height(45.dp)
                )

                IconButton(modifier = Modifier
                    .padding(start = 4.dp), onClick = {
                    callback.onTransfer(true, Purchase(), 0L)
                })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.transfer),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = text_primary
                    )
                }
            }
        }
    }
}

@Composable
fun DialogTransferPurchase(
    context: Context,
    visibilityDialog: Boolean,
    idCard: Long,
    purchase: Purchase,
    callbackOptions: CallbackOptions
) {
    val creditCardDTOCollection = remember { mutableListOf<CreditCardDTODB>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModel = CreditCardViewModelDB(context, lifecycleOwner)
    var idCardCurrent by remember { mutableStateOf(idCard) }

    LaunchedEffect(Unit) {
        creditCardViewModel.getAll()
    }

    fun reset() {
        idCardCurrent = idCard
    }

//    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner) {
//        if (it.isNotEmpty()) {
//            creditCardDTOCollection.removeAll(creditCardDTOCollection)
//            creditCardDTOCollection.addAll(it.map { creditCard ->
//                CreditCardDTODB().fromCreditCardDTO(
//                    creditCard
//                )
//            })
//        }
//    }

    DialogCustom(visibilityDialog = visibilityDialog, percentHeight = 2f) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.1f)
                .background(text_secondary),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    Modifier
                        .height(25.dp)
                )

                Text(
                    "Transferir Compra",
                    fontFamily = LatoBlack,
                    color = primary_dark,
                    fontSize = 18.sp
                )

                Spacer(
                    Modifier
                        .height(20.dp)
                )

                Divider(
                    color = secondary_dark,
                    modifier = Modifier
                        .height(1.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.8f)
            ) {
                Text(
                    text = "Escolha para qual cartão desejá transferir a compra.",
                    fontSize = 14.sp,
                    fontFamily = LatoRegular,
                    modifier = Modifier.padding(top = 18.dp)
                )

                ChoiceCardComponent(
                    idCardCurrent,
                    creditCardDTOCollection,
                    object : CallbackCreditCard {
                        override fun onChangeFilterCreditCard(cardCreditFilter: CardCreditFilter) {
                            idCardCurrent = cardCreditFilter.id
                        }
                    },
                    Modifier
                        .fillMaxHeight(.3f)
                        .fillMaxWidth(.8f)
                        .padding(start = 16.dp)
                )

                ButtonsFooterContent(
                    isClickable = true,
                    btnTextCancel = "CANCELAR",
                    btnTextAccept = "SALVAR",
                    onClickCancel = {
                        callbackOptions.onTransfer(false, Purchase(), 0L)
                        reset()
                    },
                    onClickAccept = {
                        callbackOptions.onTransfer(false, purchase, idCardCurrent)
                        reset()
                    })
            }
        }
    }
}

class SpendingTextFieldViewModel : BaseFieldViewModel() {

    val purchaseInfoCollection: MutableLiveData<MutableList<PurchaseAndCategoryInfo>> =
        MutableLiveData<MutableList<PurchaseAndCategoryInfo>>(
            mutableListOf()
        )

    val monthCurrent: MutableLiveData<String> = MutableLiveData<String>("")

    fun onChangePurchaseInfoCollection(newPurchaseInfo: MutableList<PurchaseAndCategoryInfo>) {
        purchaseInfoCollection.value = newPurchaseInfo
    }

    fun onChangeMonth(newMonth: String) {
        monthCurrent.value = newMonth
    }

    override fun checkFields(): Boolean {
        return false
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewSpendingScreen() {
    SpendingScreen(null, 0L)
}