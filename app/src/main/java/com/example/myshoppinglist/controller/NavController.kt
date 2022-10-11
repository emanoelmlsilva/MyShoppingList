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
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.ui.theme.primary_light
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NavController(navHostController: NavHostController, window: Window, routeInitial: String){
    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable("createUser") {
            CreateUserScreen(navHostController)
        }
        composable("createCards?hasToolbar={hasToolbar}", arguments = listOf(navArgument("hasToolbar") { type = NavType.BoolType })
        ) { navBackStack ->
            val hasToolbar = navBackStack.arguments?.getBoolean("hasToolbar")
            CreateCardScreen(navHostController, hasToolbar ?: false)
        }
        composable("home"){
            window.statusBarColor = primary_light.hashCode()
            HomeScreen(navHostController)
        }
        composable("credit_collection"){
            CreditCollectionScreen(navHostController)
        }
        composable("register_purchase?idCardCurrent={idCardCurrent}", arguments = listOf(navArgument("idCardCurrent"){ type = NavType.LongType})){
            navBackStack ->
            val idCardCurrent = navBackStack.arguments?.getLong("idCardCurrent")
            RegisterPurchaseScreen(navController = navHostController, idCardCurrent!!)
        }
        composable("spending?idCard={idCard}", arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ){ navBackStack ->
            val idCard = navBackStack.arguments?.getLong("idCard")
            if (idCard != null) {
                SpendingScreen(navHostController, idCard)
            }
        }
    }
}