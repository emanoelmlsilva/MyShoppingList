package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.FormatUtils
import com.example.myshoppinglist.utils.MaskUtils
import java.util.*

@ExperimentalMaterialApi
@Composable
fun SpendingScreen(navController: NavHostController?) {
    val teste = listOf(PurchaseInfo(
        "24-07-2022",
        mutableListOf(
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-02-2022",
                "3453.45".toDouble(),
                TypeCategory.DRINKS
            ),
            Purchase(
                "Biscoito",
                "Lula Mercadinho",
                0,
                "1",
                TypeProduct.QUANTITY,
                "24-02-2022",
                "12.34".toDouble(),
                TypeCategory.DRINKS
            )
        )
    ),
        PurchaseInfo(
            "13-06-2022",
            mutableListOf(
                Purchase(
                    "Biscoito",
                    "Lula Mercadinho",
                    0,
                    "1",
                    TypeProduct.QUANTITY,
                    "13-06-2022",
                    "0.0".toDouble(),
                    TypeCategory.DRINKS
                ),
                Purchase(
                    "Biscoito",
                    "Lula Mercadinho",
                    0,
                    "1",
                    TypeProduct.QUANTITY,
                    "13-06-2022",
                    "0.0".toDouble(),
                    TypeCategory.DRINKS
                )
            )
        ))

    TopAppBarScreen(content = {

        Column(modifier = Modifier.padding(16.dp)){
            Spacer(
                Modifier
                    .height(35.dp))

            BoxSpendingFromMonth()

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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                teste.map{ purchaseInfo ->
                    item {
                        Text(text = FormatUtils().getNameDay(purchaseInfo.title), modifier = Modifier.padding(start = 8.dp, top = 24.dp), color = text_title_secondary)
                    }

                    items(purchaseInfo.purchaseCollection){ purchase ->
                        BoxPurchaseSpeding(purchase)
                    }
                }
            }
        }

    }, onClickIcon = {navController?.popBackStack()})
}

@Composable
fun BoxSpendingFromMonth(){
    Column(verticalArrangement = Arrangement.Center){
        Text(text = "Gastos do mês", fontSize = 16.sp, color = text_title_secondary)
        Row(verticalAlignment = Alignment.Bottom){
            Text(text = "R$ ${MaskUtils.maskValue("")}", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Text(text = "- Novembro", modifier = Modifier.padding(start = 24.dp, bottom = 12.dp, top = 4.dp), color = text_title_secondary)
        }
    }
}

@Composable
fun BoxPurchaseSpeding(purchase: Purchase){
    Column(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
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

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewSpendingScreen(){
    SpendingScreen(null)
}