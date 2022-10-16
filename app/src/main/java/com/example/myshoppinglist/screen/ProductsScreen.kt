package com.example.myshoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProductsScreen(navController: NavController?) {

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, content = {})
}