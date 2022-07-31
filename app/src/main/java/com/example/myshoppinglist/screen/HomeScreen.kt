package com.example.myshoppinglist.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.components.SpendingComponent
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils

@Composable
fun HomeScreen(navController: NavController?) {
    val context = LocalContext.current
    val homeFieldViewModel = HomeFieldViewModel()
    val purchaseViewModel = PurchaseViewModel(context)
    val userViewModel = UserViewModel(context)
    val creditCardViewModel = CreditCardViewModel(context)
    val isVisibleValue by homeFieldViewModel.isVisibleValue.observeAsState(initial = true)
    val purchaseCollection = remember { mutableStateListOf<Purchase>() }
    val price = remember { mutableStateOf<Double>(0.0)}
    val currentCreditCard = remember { mutableStateOf<CreditCard?>(null)}
    val creditCardCollection = remember { mutableListOf<CreditCard>()}

    fun recoverDataPurchase(){
        purchaseViewModel.getPurchasesWeek(currentCreditCard.value!!.id)

        purchaseViewModel.sumPriceBMonth(currentCreditCard.value!!.id, "${FormatUtils().getMonthAndYear()}-")
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        userViewModel.getUserCurrent()

        LaunchedEffect(Unit){
            creditCardViewModel.getAll()
        }

        purchaseViewModel.searchPriceResult.observeForever {
            price.value =  it
        }

        purchaseViewModel.searchCollectionResults.observeForever {  purchases ->
            purchaseCollection.removeAll(purchaseCollection)
            purchaseCollection.addAll(purchases)

        }

        creditCardViewModel.searchCollectionResult.observeForever {
            creditCardCollection.removeAll(creditCardCollection)
            creditCardCollection.addAll(it)
            currentCreditCard.value = it[0]

            recoverDataPurchase()
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HeaderComponent(userViewModel, object: Callback {
                override fun onClick() {
                    homeFieldViewModel.onChangeVisibleValue()
                }
            })
            SpendingComponent(price.value, isVisibleValue, object : Callback{
                override fun onClick() {
                    navController?.navigate("spending")
                }
            }, currentCreditCard.value,creditCardCollection, object :
                CallbackCreditCard {
                override fun onChangeValueCreditCard(creditCard: CreditCard) {
                    currentCreditCard.value = creditCard
                    recoverDataPurchase()
                }
            })
            BoxCreditCard(object : Callback {
                override fun onClick() {
                    navController?.navigate("credit_collection")
                }
            })

            Spacer(Modifier.size(32.dp))

            Divider(
                color = secondary_light,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

            Spacer(Modifier.size(22.dp))

            Text(text = "Histórico", modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp), fontWeight = FontWeight.Bold, fontSize = 24.sp)

            Spacer(Modifier.size(24.dp))

            BoxPurchaseHistoryComponent(purchaseCollection)
        }
    }

}

@Composable
fun BoxCreditCard(callBack: Callback){
    Card(
        backgroundColor = background_card,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(.93f)
            .clickable { callBack.onClick() },
        elevation = 2.dp, shape = RoundedCornerShape(8.dp),
    ){
        Row(modifier = Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically){
            Icon(modifier = Modifier
                .padding(start = 16.dp)
                .size(20.dp), painter = painterResource(id =  R.drawable.ic_baseline_credit_card_24), contentDescription = "icon de cartão",)
            Text(text = "Meus Cartões", modifier = Modifier.padding(start = 52.dp))
        }
    }
}

class HomeFieldViewModel: BaseFieldViewModel(){
    var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)

    fun onChangeVisibleValue(){
        isVisibleValue.value = !isVisibleValue.value!!
    }
    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = null)
}