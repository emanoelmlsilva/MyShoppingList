package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme

@Composable
fun CreateUser(navController: NavController) {
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Greeting("Android")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyShoppingListTheme {
        Greeting("Android")
    }
}