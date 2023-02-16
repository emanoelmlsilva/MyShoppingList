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
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.ItemListAndCategoryDTO
import com.example.myshoppinglist.database.dtos.ItemListDTO
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.ui.theme.primary_light
import com.example.myshoppinglist.utils.ConversionUtils
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NavController(
    navHostController: NavHostController,
    window: Window,
    routeInitial: String,
    callback: VisibleCallback
) {
    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable(Screen.CreateUser.name) {
            callback.onChangeVisible(false)
            CreateUserScreen(navHostController)
        }
        composable(
            "${Screen.CreateCards.name}?hasToolbar={hasToolbar}?nameUser={nameUser}",
            arguments = listOf(
                navArgument("hasToolbar") { type = NavType.BoolType },
                navArgument("nameUser") { type = NavType.StringType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val hasToolbar = navBackStack.arguments?.getBoolean("hasToolbar")
            val nameUser = navBackStack.arguments?.getString("nameUser")
            CreateCardScreen(navHostController, hasToolbar ?: false, nameUser!!)
        }
        composable(Screen.Home.name) {
            callback.onChangeVisible(true)
            HomeScreen(navHostController)
        }
        composable(
            "${Screen.RegisterPurchase.name}?idCardCurrent={idCardCurrent}",
            arguments = listOf(navArgument("idCardCurrent") { type = NavType.LongType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val idCardCurrent = navBackStack.arguments?.getLong("idCardCurrent")
            RegisterPurchaseScreen(navController = navHostController, idCardCurrent!!)
        }
        composable(
            "${Screen.Spending.name}?idCard={idCard}",
            arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val idCard = navBackStack.arguments?.getLong("idCard")
            if (idCard != null) {
                SpendingScreen(navHostController, idCard)
            }
        }
        composable(Screen.ProductsManager.name) {
            callback.onChangeVisible(true)
            ProductsManagerScreen(navHostController)
        }
        composable(Screen.Finance.name) {
            callback.onChangeVisible(true)
            FinanceScreen(navHostController)
        }
        composable(Screen.Categories.name) {
            callback.onChangeVisible(true)
            CategoriesScreen(navHostController)
        }
        composable(
            "${Screen.RegisterCategory.name}?idCategory={idCategory}",
            arguments = listOf(navArgument("idCategory") { type = NavType.LongType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val idCategory = navBackStack.arguments?.getLong("idCategory")
            RegisterCategoryScreen(navHostController, idCategory)
        }
        composable(
            "${Screen.ListPurchase.name}?idCard={idCard}",
            arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val idCard = navBackStack.arguments?.getLong("idCard")
            ListItemPurchaseScreen(navHostController, idCard!!)
        }
        composable("${Screen.MakingMarketScreen.name}?idCard={idCard}?itemListCollection={itemListCollection}",
            arguments = listOf(
                navArgument("idCard") { type = NavType.LongType },
                navArgument("itemListCollection") {
                    type = NavType.StringType
                }
            )) { navBackStack ->
            callback.onChangeVisible(false)
            val idCard = navBackStack.arguments?.getLong("idCard")
            val itemListCollection = navBackStack.arguments?.getString("itemListCollection")

            MakingMarketScreen(navHostController, idCard!!, itemListCollection!!)
        }
        composable("${Screen.SettingsScreen.name}?idAvatar={idAvatar}?nickName={nickName}",
            arguments = listOf(
                navArgument("idAvatar") { type = NavType.IntType },
                navArgument("nickName") { type = NavType.StringType }
            )) { navBackStack ->
            callback.onChangeVisible(false)

            val idAvatar = navBackStack.arguments?.getInt("idAvatar")
            val nickName = navBackStack.arguments?.getString("nickName")

            SettingsScreen(navHostController, idAvatar!!, nickName!!)
        }
    }
}