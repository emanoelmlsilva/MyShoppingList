package com.example.myshoppinglist.screen

import android.os.Handler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
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
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(navController: NavHostController?) {
    val context = LocalContext.current
    val purchaseViewModel = PurchaseViewModel(context)
    val reset = remember { mutableStateOf(false)}
    val scaffoldState = rememberBottomSheetScaffoldState()
    val registerTextFieldViewModel: RegisterTextFieldViewModel = viewModel()
    val purchaseInfoCollection = remember { mutableStateListOf<PurchaseInfo>() }

    registerTextFieldViewModel.purchaseCollection.observeForever {
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        purchaseInfoCollection.addAll(it)
    }

    registerTextFieldViewModel.resetDate.observeForever {
        reset.value = it
    }

    Box {
        BottomSheetScaffold(
            sheetBackgroundColor = background_card,
            sheetContent = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.92f)
                        .background(background_card)
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
                            text = "${purchaseInfoCollection.size}".padStart(3, '0'), color = text_secondary ,modifier = Modifier
                                .drawBehind {
                                    drawCircle(
                                        color = text_primary,
                                        radius = this.size.minDimension
                                    )
                                }, fontSize = 12.sp
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
                    BoxProductRegisterComponent(purchaseInfoCollection)
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
            sheetPeekHeight = 228.dp,
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(.7f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{
                    TextInputComponent(
                        label = "Produto",
                        reset = reset.value,
                        isMandatory = true,
                        error =  registerTextFieldViewModel.productError.observeAsState().value,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                registerTextFieldViewModel.onChangeProduct(newValue)
                            }

                            override fun onChangeTypeProduct(newProduct: TypeProduct) {
                                TODO("Not yet implemented")
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

                    CategoryProduct(registerTextFieldViewModel, registerTextFieldViewModel.typeCategoryError.observeAsState().value)

                    PurchaseAndPaymentComponent(registerTextFieldViewModel, registerTextFieldViewModel.idCardError.observeAsState().value)

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp, top = 16.dp),
                        onClick = {
                            if(registerTextFieldViewModel.checkFileds()) {
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
                btnTextCancel = "CANCELAR",
                btnTextAccept = "SALVAR",
                onClickCancel = {},
                onClickAccept = {
                    purchaseInfoCollection.forEach { purchaseInfo ->
                        purchaseViewModel.insertPurchase(purchaseInfo.purchaseCollection)
                    }
                    navController!!.popBackStack()
                })
        }
    }
}

@Composable
fun CategoryProduct(registerTextFieldViewModel: RegisterTextFieldViewModel, error: Boolean? = false){
    var categoryCollections = listOf(TypeCategory.HYGIENE, TypeCategory.CLEARNING, TypeCategory.FOOD, TypeCategory.DRINKS, TypeCategory.OTHERS)
    var categoryChoice = registerTextFieldViewModel.category.observeAsState().value

    Column{
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = "Categorias", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 4.dp), fontWeight = FontWeight.Bold)
            if(error!!) Icon(painter = painterResource(id = R.drawable.ic_baseline_error_24), modifier = Modifier.size(16.dp), contentDescription = null, tint = message_error)
        }
        LazyRow(modifier = Modifier.padding(start = 8.dp)){
            items(categoryCollections){ category ->
                Card(modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable { registerTextFieldViewModel.onChangeCategory(category) }){
                    Row(modifier = Modifier
                        .background(if (category == categoryChoice) primary_dark else background_card)
                        .padding(horizontal = 6.dp, vertical = 3.dp), horizontalArrangement = Arrangement.Center){
                        Image(
                            painter = painterResource(id = category.idImage),
                            contentDescription = null,
                            Modifier
                                .size(20.dp)
                                .padding(top = 3.dp)
                        )
                        Text(text = category.category, modifier = Modifier.padding(start = 6.dp))
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun PurchaseAndPaymentComponent(registerTextFieldViewModel: RegisterTextFieldViewModel, error: Boolean? = false) {
    var expanded by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val creditCardViewModel = CreditCardViewModel(context)
    val cardCreditCollection = creditCardViewModel.searchCollectionResult.observeAsState(initial = listOf()).value
    val reset by registerTextFieldViewModel.resetDate.observeAsState(initial = false)
    creditCardViewModel.getAll()
    val cardColleciton = getNameCard(cardCreditCollection)
    val isBlock = registerTextFieldViewModel.isBlock.observeAsState()

    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        backgroundColor = text_secondary,
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
                Row {
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                    Text(
                        text = "Local da Compra & Pagamento",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                IconButton(
                    modifier = Modifier.then(Modifier.size(24.dp)),
                    onClick = { registerTextFieldViewModel.onChangeIsBlock(!isBlock.value!!) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_block_24),
                        contentDescription = null,
                        tint = if(isBlock.value!!) text_title_secondary else text_primary,
                    )
                }

            }
            if (expanded) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                TextInputComponent(
                    label = "Local",
                    reset = reset,
                    error = registerTextFieldViewModel.localeError.observeAsState().value,
                    customOnClick = object : CustomTextFieldOnClick {
                        override fun onChangeValue(newValue: String) {
                            registerTextFieldViewModel.onChangeLocale(newValue)
                        }

                    })

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Forma de Pagamento")
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_credit_card_24),
                                contentDescription = null,
                                tint = text_primary,
                            )
                    }
                    Divider(
                        color = text_primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                    CustomDropdownMenu(
                        cardColleciton,
                        error = error,
                        object : CustomTextFieldOnClick {
                            override fun onChangeValueLong(newValue: Long) {
                                registerTextFieldViewModel.onChangeIdCard(newValue)
                            }
                        }, reset)

                }
            }

        }
    }

}

fun getNameCard(creditCardColelction: List<CreditCard>): HashMap<String, Long> {
    val cardCreditFormated: HashMap<String, Long> = HashMap<String, Long> ()

    cardCreditFormated.put("Cartões" , -1)

    creditCardColelction.forEachIndexed { index, creditCard ->
            cardCreditFormated.put(
                creditCard.cardName,
                creditCard.id
            )
         }

    return cardCreditFormated.entries.sortedBy { it.value }.associate { it.toPair() } as HashMap<String, Long>

}

@Composable
fun CustomButton(callback: Callback, icon: Int) {
    Card(elevation = 2.dp, shape = RoundedCornerShape(6.dp), backgroundColor = background_card) {
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
fun BoxChoiceValue(registerTextFieldViewModel: RegisterTextFieldViewModel) {
    var isMoney by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("0") }
    var convertedValue = 0

    registerTextFieldViewModel.resetDate.observeForever {
        if(it){
            value = "1"
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
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(callback = object : Callback {
                override fun onClick() {
                    convertedValue = MaskUtils.replaceAll(value).toInt()
                    if (convertedValue > 0) {
                        convertedValue -= 1
                        value = convertedValue.toString()
                        registerTextFieldViewModel.onChangeQuantOrKilo(value)
                    }
                }
            }, icon = R.drawable.ic_baseline_remove_24)
            NumberInputComponent(
                maxChar = 11,
                hasIcon = true,
                value = value,
                isMandatory = false,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .fillMaxWidth(0.79f),
                label = if (isMoney) "Quantidade" else "Quilograma",
                customOnClick = customOnClick
            )
            CustomButton(callback = object : Callback {
                override fun onClick() {
                    convertedValue = MaskUtils.replaceAll(value).toInt()
                    convertedValue += 1
                    value = convertedValue.toString()
                    registerTextFieldViewModel.onChangeQuantOrKilo(value)
                }
            }, icon = R.drawable.ic_baseline_add_24)
        }
    }

}

class RegisterTextFieldViewModel: BaseFieldViewModel(){
    var product: MutableLiveData<String> = MutableLiveData("")
    var price: MutableLiveData<String> = MutableLiveData("")
    var quantOrKilo: MutableLiveData<String> = MutableLiveData("")
    var locale: MutableLiveData<String> = MutableLiveData("")
    var idCard: MutableLiveData<Long?> = MutableLiveData(-1)
    var purchaseCardId: MutableLiveData<Long> = MutableLiveData(0)
    var category: MutableLiveData<TypeCategory> = MutableLiveData(null)
    var isBlock: MutableLiveData<Boolean> = MutableLiveData(false)
    var resetDate: MutableLiveData<Boolean> = MutableLiveData(false)
    var typeProduct: MutableLiveData<TypeProduct> = MutableLiveData(TypeProduct.QUANTITY)
    val purchaseCollection: MutableLiveData<MutableList<PurchaseInfo>> = MutableLiveData<MutableList<PurchaseInfo>>(mutableListOf())

    //variavel de error
    val productError: MutableLiveData<Boolean> = MutableLiveData(false)
    val priceError: MutableLiveData<Boolean> = MutableLiveData(false)
    val localeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val idCardError: MutableLiveData<Boolean> = MutableLiveData(false)
    val typeCategoryError: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun checkFileds(): Boolean {

        productError.value = product.value!!.isBlank()

        priceError.value = price.value!!.isBlank()

        localeError.value = locale.value!!.isBlank()

        idCardError.value = idCard.value == -1L

        typeCategoryError.value = category.value == null

        if(product.value!!.isBlank()) return false

        if(price.value!!.isBlank()) return false

        if(quantOrKilo.value!!.isBlank()) return false

        if(locale.value!!.isBlank()) return false

        if(idCard.value == -1L) return false

        if(category.value == null) return false

        return true
    }

    fun onChangeResetDate(){
        resetDate.value = true

        product.value = ""
        price.value = ""
        quantOrKilo.value = ""
        locale.value = ""
        idCard.value = -1L
        category.value = null
        typeProduct.value = TypeProduct.QUANTITY

        Handler().postDelayed({ kotlin.run {
            resetDate.value = false
        } },200)
    }

    fun onChangeCategory(newCategoty: TypeCategory){
        category.value = newCategoty
        typeCategoryError.value = false

    }

    fun onChangeTypeProduct(newTypeProduct: TypeProduct){
        typeProduct.value = newTypeProduct
    }

    fun addPurchase(){

        val purchase = Purchase(product.value!!, locale.value!!, idCard.value!!, quantOrKilo.value!!, typeProduct.value!!, FormatUtils().getDateString(), MaskUtils.convertValueStringToDouble(
            price.value!!
        ),
            category.value!!
        )

        if(purchaseCollection.value != null && !purchaseCollection.value!!.isEmpty()){
            val indexCurrent = purchaseCollection.value!!.indexOfFirst { it.title.equals(locale.value!!) }
            if(indexCurrent != -1){
                purchaseCollection.value!!.get(indexCurrent).purchaseCollection.add(purchase)
            }else{
                var newPurchaseCollection = purchaseCollection.value
                newPurchaseCollection!!.add(PurchaseInfo(purchase.locale, mutableListOf(purchase)))
                purchaseCollection.value = newPurchaseCollection
            }

        }else{
            purchaseCollection.value = mutableListOf(PurchaseInfo(purchase.locale, mutableListOf(purchase)))
        }

    }

    fun onChangeProduct(newProduct: String){
        product.value = newProduct
        productError.value = newProduct.isBlank()
    }

    fun onChangePrice(newPrice : String){
        price.value = newPrice
    }

    fun onChangeQuantOrKilo(newQuantOrKilo : String){
        quantOrKilo.value = newQuantOrKilo
    }

    fun onChangeLocale(newLocale : String){
        locale.value = newLocale
        localeError.value = newLocale.isBlank()
    }

    fun onChangeIdCard(newIdCard : Long?){
        idCard.value = newIdCard
        idCardError.value = newIdCard == -1L
    }

    fun onChangeIsBlock(newIsBlock : Boolean){
        isBlock.value = newIsBlock
    }

}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PreviewRegisterPurchaseScreen() {
//    RegisterPurchaseScreen(null)
}