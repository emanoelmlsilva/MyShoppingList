package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.BoxDropdownCardCredit
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalMaterialApi
@Composable
fun SpendingScreen(navController: NavHostController?) {
    val context = LocalContext.current
    val purchaseViewModel = PurchaseViewModel(context)
    val spendingTextFieldViewModel = SpendingTextFieldViewModel()
    val creditCardViewModel = CreditCardViewModel(context)
    val purchaseInfoCollection = remember { mutableStateListOf<PurchaseInfo>() }
    val price = remember { mutableStateOf<Double>(0.0)}
    val monthsCollection = remember { mutableStateListOf<String>() }
    val monthCurrent = remember {mutableStateOf<String>("")}
    val creditCardCollection = remember { mutableListOf<CreditCard>()}
    val currentCreditCard = remember { mutableStateOf<CreditCard?>(null)}

    LaunchedEffect(Unit){
        creditCardViewModel.getAll()
    }

    fun reset(){
        monthCurrent.value = ""
        price.value = 0.0
        purchaseInfoCollection.removeAll(purchaseInfoCollection)
        monthsCollection.removeAll(monthsCollection)
    }

    fun getInforPurchaseByMonth(month: String){
        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(month)

        purchaseViewModel.getPurchaseByMonth(currentCreditCard.value!!.id, "$monthAndYearNumber-")

        purchaseViewModel.sumPriceBMonth(currentCreditCard.value!!.id, "$monthAndYearNumber-")
    }

    creditCardViewModel.searchCollectionResult.observeForever {
        creditCardCollection.removeAll(creditCardCollection)
        creditCardCollection.addAll(it)
        currentCreditCard.value = it[0]

        purchaseViewModel.getMonthByIdCard(currentCreditCard.value!!.id)
    }

    purchaseViewModel.searchPriceResult.observeForever {
        price.value = it
    }

    spendingTextFieldViewModel.monthCurrent.observeForever {
        if(it.isNotBlank()) {
           getInforPurchaseByMonth(it)
        }
    }

    purchaseViewModel.searchResultMonths.observeForever { months ->

        monthsCollection.removeAll(monthsCollection)

        val convertedMonth = months.groupBy {
            val separaterDate = it.split("-")
            "${separaterDate.get(0)}-${separaterDate.get(1)}"
        }.map{ group -> FormatUtils().getMonth("${group.key}-01")}

        monthsCollection.addAll(convertedMonth)

        if(convertedMonth.isNotEmpty()){
            val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(convertedMonth.get(0))

            monthCurrent.value = monthAndYearNumber

            getInforPurchaseByMonth(convertedMonth.get(0))
        }

    }

    spendingTextFieldViewModel.purchaseInfoCollection.observeForever {
        purchaseInfoCollection.addAll(it)
    }

    purchaseViewModel.searchCollectionResults.observeForever { purchases ->

        val purchaseInfoFormattedCollection: MutableList<PurchaseInfo> = purchases.groupBy { it.date }.map{ group -> PurchaseInfo(group.key,
            group.value as MutableList<Purchase>
        )} as MutableList<PurchaseInfo>

        purchaseInfoCollection.removeAll(purchaseInfoCollection)

        spendingTextFieldViewModel.onChangePurchaseInfoCollection(purchaseInfoFormattedCollection)

    }

    TopAppBarScreen(content = {

        Column(modifier = Modifier.padding(16.dp)){
            Spacer(
                Modifier
                    .height(35.dp))

            BoxSpendingFromMonth(spendingTextFieldViewModel, monthsCollection, price.value, currentCreditCard.value, creditCardCollection, object :
                CallbackCreditCard {
                override fun onChangeValueCreditCard(creditCard: CreditCard) {
                    currentCreditCard.value = creditCard

                    purchaseViewModel.getMonthByIdCard(currentCreditCard.value!!.id)

                    reset()

                }
            })

            Spacer(
                Modifier
                    .height(35.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Card(modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape), backgroundColor = background_card, onClick = { navController!!.navigate("register_purchase")}){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_shopping_bag_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(ButtonDefaults.IconSize)
                            .padding(18.dp),
                    )
                }
                Text(text = "Comprar", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(
                Modifier
                    .height(15.dp))

            Divider(
                color = divider,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

            if(purchaseInfoCollection!!.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    purchaseInfoCollection!!.map{ purchaseInfo ->
                        item {
                            Text(text = FormatUtils().getNameDay(purchaseInfo.title), modifier = Modifier.padding(start = 8.dp, top = 24.dp), color = text_title_secondary)
                        }

                        items(purchaseInfo.purchaseCollection){ purchase ->
                            BoxPurchaseSpeding(purchase)
                        }
                    }
                }
            }
        }

    }, onClickIcon = {navController?.popBackStack()})
}

@Composable
fun BoxSpendingFromMonth(spendingField: SpendingTextFieldViewModel, months: List<String>, price: Double, currentCreditCard: CreditCard?,creditCards: List<CreditCard>, callbackCreditCard: CallbackCreditCard){
    val hasMonths = months.isNotEmpty()
    val monthCurrent = remember { mutableStateOf(if(hasMonths) months[0] else "") }
    spendingField.monthCurrent.observeForever {
        if(it.isNotBlank()) monthCurrent.value = it else if(hasMonths) monthCurrent.value = months[0]
    }

    Column(verticalArrangement = Arrangement.Center){
        Row(verticalAlignment = Alignment.Bottom){
            Text(text = "Gastos do mês", modifier = Modifier.padding(bottom = 16.dp), fontSize = 16.sp, color = text_title_secondary)
            CustomDropDownMonth(
                object : CustomTextFieldOnClick {
                    override fun onChangeValue(newValue: String) {
                        spendingField.onChangeMonth(newValue)
                    }
                }, months, monthCurrent.value)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(price))}", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            if(currentCreditCard != null) BoxDropdownCardCredit(creditCards, currentCreditCard, callbackCreditCard)
        }
    }
}

@Composable
fun CustomDropDownMonth(callback: CustomTextFieldOnClick, monthCollection: List<String>, monthCurrent: String){
    var expanded by remember {mutableStateOf(false)}

    Card(elevation = 0.dp, backgroundColor = background_card, modifier = Modifier
        .padding(6.dp, 16.dp, 16.dp, 16.dp)
        .clickable(onClick = { expanded = true })) {

        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "- ${if (monthCollection.isNotEmpty()) monthCurrent else ""}", modifier = Modifier.padding(start = 8.dp, end = 8.dp), color = text_title_secondary)
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
fun BoxPurchaseSpeding(purchase: Purchase){
    Column(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = purchase.category.imageCircle),
                contentDescription = null,
                Modifier
                    .size(46.dp)
                    .padding(top = 3.dp, end = 8.dp)
            )
            Column{
                Text(text = purchase.name, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(.7f))
                Text(text = purchase.locale, modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(top = 8.dp), fontSize = 12.sp, color = text_title_secondary)
            }
            Column{
                Text(text = "R$ ${MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(purchase.price))}", fontWeight = FontWeight.Bold)
                Text(text = "${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}",
                    color = text_title_secondary, modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(), fontSize = 14.sp, textAlign = TextAlign.Center)
            }

        }

        Divider(
            color = divider,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}

class SpendingTextFieldViewModel: BaseFieldViewModel(){

    val purchaseInfoCollection: MutableLiveData<MutableList<PurchaseInfo>> = MutableLiveData<MutableList<PurchaseInfo>>(
        mutableListOf())

    val monthCurrent: MutableLiveData<String> = MutableLiveData<String>("")

    fun onChangePurchaseInfoCollection(newPurchaseInfo: MutableList<PurchaseInfo>){
        purchaseInfoCollection.value = newPurchaseInfo
    }

    fun onChangeMonth(newMonth: String){
        monthCurrent.value = newMonth
    }

    override fun checkFileds(): Boolean {
        return false
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewSpendingScreen(){
    SpendingScreen(null)
}