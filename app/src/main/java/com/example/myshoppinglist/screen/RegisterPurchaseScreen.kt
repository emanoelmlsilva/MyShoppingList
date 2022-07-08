package com.example.myshoppinglist.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.components.CustomOnClick
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.ui.theme.*

@ExperimentalComposeUiApi
@Composable
fun RegisterPurchaseScreen(navController: NavHostController?){
    Scaffold(       topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = {navController?.popBackStack()}) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Menu Btn",
                        tint = secondary_dark
                    )
                }
            },
            backgroundColor = text_secondary,
            elevation = 0.dp
        )
    }){
        Column(modifier = Modifier.padding(16.dp)) {
            TextInputComponent(label = "Produto", customOnClick =  object: CustomOnClick{
                override fun onChangeValeu(newValue: String) {
                    Log.d("TESTE", "RECUPERANDO VALOR ${newValue}")
                }
            })
            Row(modifier = Modifier
                .fillMaxWidth().padding(top = 16.dp) , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                TextInputComponent(maxChar = 11, keyboardType = KeyboardType.Number, modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(end = 16.dp),label = "Preço", customOnClick =  object: CustomOnClick{
                    override fun onChangeValeu(newValue: String) {
                        Log.d("TESTE", "RECUPERANDO VALOR ${newValue}")
                    }
                })
                BoxChoiceValue()
            }
        }
    }
}

@Composable
fun CustomButton(callback: Callback, icon: Int){
    Card(elevation = 2.dp, shape = RoundedCornerShape(6.dp), backgroundColor = background_card){
    IconButton(modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { callback.onClick()},
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
fun BoxChoiceValue(){
    var isMoney by remember { mutableStateOf(true) }

    Card(elevation = 2.dp, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(top = 6.dp)){
        Row(modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            CustomButton(callback = object : Callback {
                override fun onClick() {

                }
            }, icon = R.drawable.ic_baseline_remove_24)
            TextInputComponent(maxChar = 11, icon = if(!isMoney) R.drawable.ic_baseline_1k_24 else R.drawable.ic_baseline_123_24
                ,isMandatory = false, modifier = Modifier.padding(vertical = 1.dp).fillMaxWidth(0.79f)
                ,label = if(isMoney) "Quantidade" else "Quilo", customOnClick =  object: CustomOnClick{
                override fun onChangeValeu(newValue: String) {
                    Log.d("TESTE", "RECUPERANDO VALOR ${newValue}")
                }
                override fun onClick() {
                    isMoney = !isMoney
                }
            })
            CustomButton(callback = object : Callback {
                override fun onClick() {

                }
            }, icon = R.drawable.ic_baseline_add_24)
        }
    }

}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PreviewRegisterPurchaseScreen(){
    RegisterPurchaseScreen(null)
}