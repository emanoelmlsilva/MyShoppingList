package com.example.myshoppinglist.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.components.CreateHeaderFieldViewModel
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.components.SpendingComponent
import com.example.myshoppinglist.controller.Callback
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.*

@Composable
fun HomeScreen(navController: NavController?) {
    val context = LocalContext.current
    val homeFieldViewModel = HomeFieldViewModel()
    val userViewModel = UserViewModel(context)
    val isVisibleValue = homeFieldViewModel.isVisibleValue.observeAsState(initial = true)
    userViewModel.getUserCurrent()

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HeaderComponent(userViewModel, object: Callback{
                override fun onClick() {
                    homeFieldViewModel.onChangeVisibleValue()
                }
            })
            SpendingComponent(isVisibleValue.value)
            BoxCreditCard(object : Callback{
                override fun onClick() {
                    navController?.navigate("credit_collection")
                }
            })
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