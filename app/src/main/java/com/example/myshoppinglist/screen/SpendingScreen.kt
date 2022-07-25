package com.example.myshoppinglist.screen

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalMaterialApi
@Composable
fun SpendingScreen(navController: NavHostController?) {
    val context = LocalContext.current
    val purchaseViewModel = PurchaseViewModel(context)
    val spendingTextFieldViewModel = SpendingTextFieldViewModel()
    var purchaseInfoCollection = remember { mutableStateListOf<PurchaseInfo>() }//spendingTextFieldViewModel.purchaseInfoCollection.observeAsState()//

    val monthsCollection = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit){
        purchaseViewModel.getMonthByIdCard(1)
    }


    spendingTextFieldViewModel.monthCurrent.observeForever {
        if(it.isNotBlank()) {
            val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(it)
            purchaseViewModel.getPurchaseByMonth(1, "-$monthAndYearNumber")
        }
    }

    purchaseViewModel.searchResultMonths.observeForever { months ->
        val convertedMonth = months.groupBy {
            val separaterDate = it.split("-")
            "${separaterDate.get(1)}-${separaterDate.get(2)}"
        }.map{ group -> FormatUtils().getMonth("01-${group.key}")}

        monthsCollection.addAll(convertedMonth)
        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(convertedMonth.get(0))

        purchaseViewModel.getPurchaseByMonth(1, "-$monthAndYearNumber")
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

            if(monthsCollection.size > 0) BoxSpendingFromMonth(spendingTextFieldViewModel, monthsCollection)

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
fun BoxSpendingFromMonth(spendingField: SpendingTextFieldViewModel, months: List<String>){
    val monthCurrent = remember { mutableStateOf(months.get(0)) }
    spendingField.monthCurrent.observeForever {
        if(it.isNotBlank()) monthCurrent.value = it
    }

    Column(verticalArrangement = Arrangement.Center){
        Text(text = "Gastos do mês", fontSize = 16.sp, color = text_title_secondary)
        Row(verticalAlignment = Alignment.Bottom){
            Text(text = "R$ ${MaskUtils.maskValue("")}", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            CustomDropDownMonth(
                object : CustomTextFieldOnClick {
                    override fun onChangeValue(newValue: String) {
                        spendingField.onChangeMonth(newValue)
                    }
                }, months, monthCurrent.value)
        }
    }
}

@Composable
fun CustomDropDownMonth(callback: CustomTextFieldOnClick, monthCollection: List<String>, monthCurrent: String){
    var expanded by remember {mutableStateOf(false)}

    Card(elevation = 0.dp, backgroundColor = text_secondary, modifier = Modifier
        .padding(0.dp, 16.dp, 16.dp, 16.dp)
        .clickable(onClick = { expanded = true })) {

        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "- $monthCurrent", modifier = Modifier.padding(start = 16.dp), color = text_title_secondary)
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
            Text(text = purchase.name, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(.7f))
            Text(text = "R$ ${MaskUtils.maskValue(purchase.price.toString())}", fontWeight = FontWeight.Bold)
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
        TODO("Not yet implemented")
    }

}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewSpendingScreen(){
    SpendingScreen(null)
}