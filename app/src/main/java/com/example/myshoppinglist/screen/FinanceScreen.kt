package com.example.myshoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun FinanceScreen(navController: NavController?) {
    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, content = {})
}