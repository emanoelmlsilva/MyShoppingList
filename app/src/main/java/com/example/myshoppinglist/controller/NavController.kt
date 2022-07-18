package com.example.myshoppinglist.controller

import android.view.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.ui.theme.primary_dark
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NavController(navHostController: NavHostController, window: Window, routeInitial: String){
    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable("createUser") {
            window.statusBarColor = secondary.hashCode()
            CreateUserScreen(navHostController)
        }
        composable("createCards?hasToolbar={hasToolbar}", arguments = listOf(navArgument("hasToolbar") { defaultValue = "false" })
        ) { navBackStack ->
            val hasToolbar = navBackStack.arguments?.getString("hasToolbar").toBoolean()

            window.statusBarColor = secondary.hashCode()
            if (hasToolbar) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = {navHostController.popBackStack()}) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Menu Btn",
                                        tint = secondary_dark
                                    )
                                }
                            },
                            backgroundColor = text_secondary,
                            elevation = 0.dp
                        )
                    }
                ) {
                    CreateCardScreen(navHostController, TypeCard.CREDIT)
                }
            } else {
                CreateCardScreen(navHostController,TypeCard.MONEY)
            }
        }
        composable("home"){
            window.statusBarColor = primary_dark.hashCode()
            HomeScreen(navHostController)
        }
        composable("credit_collection"){
            CreditCollectionScreen(navHostController)
        }
        composable("register_purchase"){
            RegisterPurchaseScreen(navController = navHostController)
        }
    }
}