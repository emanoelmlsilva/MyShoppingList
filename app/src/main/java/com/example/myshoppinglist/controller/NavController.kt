package com.example.myshoppinglist.controller

import android.view.Window
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.ui.theme.primary_light
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NavController(navHostController: NavHostController, window: Window, routeInitial: String, callback: VisibleCallback){
    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable(Screen.CreateUser.name) {
            callback.onChangeVisible(false)
            CreateUserScreen(navHostController)
        }
        composable("${Screen.CreateCards.name}?hasToolbar={hasToolbar}", arguments = listOf(navArgument("hasToolbar") { type = NavType.BoolType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val hasToolbar = navBackStack.arguments?.getBoolean("hasToolbar")
            CreateCardScreen(navHostController, hasToolbar ?: false)
        }
        composable(Screen.Home.name){
            callback.onChangeVisible(true)
            window.statusBarColor = primary_light.hashCode()
            HomeScreen(navHostController)
        }
        composable(Screen.CreditCollection.name){
            callback.onChangeVisible(true)
            CreditCollectionScreen(navHostController)
        }
        composable("${Screen.RegisterPurchase.name}?idCardCurrent={idCardCurrent}", arguments = listOf(navArgument("idCardCurrent"){ type = NavType.LongType})){
            navBackStack ->
            callback.onChangeVisible(false)
            val idCardCurrent = navBackStack.arguments?.getLong("idCardCurrent")
            RegisterPurchaseScreen(navController = navHostController, idCardCurrent!!)
        }
        composable("${Screen.Spending.name}?idCard={idCard}", arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ){ navBackStack ->
            callback.onChangeVisible(false)
            val idCard = navBackStack.arguments?.getLong("idCard")
            if (idCard != null) {
                SpendingScreen(navHostController, idCard)
            }
        }
        composable(Screen.Products.name) {
            callback.onChangeVisible(true)
            ProductsScreen(navHostController)
        }
        composable(Screen.Finance.name) {
            callback.onChangeVisible(true)
            FinanceScreen(navHostController)
        }
    }
}