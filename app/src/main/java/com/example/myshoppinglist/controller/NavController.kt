package com.example.myshoppinglist.controller

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.fieldViewModel.*
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.utils.ConversionUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay

@SuppressLint("FlowOperatorInvokedInComposition", "StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.N)
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

    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    val homeFieldViewModel = HomeFieldViewModel(context, lifecycleOwner)
    val categoryFieldViewModel = CategoryFieldViewModel(context, lifecycleOwner)
    val productManagerFieldViewModel = ProductManagerFieldViewModel(context, lifecycleOwner)
    val registerCategoryFieldViewModel = RegisterCategoryFieldViewModel(context, lifecycleOwner)
    val listItemFieldViewModel = ListItemFieldViewModel(context, lifecycleOwner)
    val marketItemFieldViewModel = MarketItemFieldViewModel(context, lifecycleOwner)

    fun softInputMode(isKeyBoard: Boolean) {
        window.setSoftInputMode(if (isKeyBoard) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    LaunchedEffect(key1 = navHostController.currentDestination) {
        callback.onChangeVisible(Screen.enableScreenBottomBarState(navHostController.currentDestination!!.route!!))
    }

    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable(Screen.CreateUser.name) { navBackStack ->

            var arguments = navHostController.previousBackStackEntry?.arguments

            if(arguments == null){
                arguments = navBackStack.arguments
            }

            val isUpdate = arguments?.getBoolean("isUpdate") ?: true
            val hasToolbar = arguments?.getBoolean("hasToolbar") ?: true

            CreateUserScreen(navHostController, isUpdate, hasToolbar)

        }
        composable(
            "${Screen.CreateCards.name}?hasToolbar={hasToolbar}?holderName={holderName}?isUpdate={isUpdate}?creditCardDTO={creditCardDTO}",
            arguments = listOf(
                navArgument("hasToolbar") { type = NavType.BoolType },
                navArgument("holderName") { type = NavType.StringType },
                navArgument("creditCardDTO") { type = NavType.StringType },
                navArgument("isUpdate") { type = NavType.BoolType })
        ) { navBackStack ->
            val hasToolbar = navBackStack.arguments?.getBoolean("hasToolbar")
            val holderName = navBackStack.arguments?.getString("holderName")
            val creditCardDTO = navBackStack.arguments?.getString("creditCardDTO")
            val isUpdate = navBackStack.arguments?.getBoolean("isUpdate")

            CreateCardScreen(
                navHostController, hasToolbar ?: false,
                isUpdate!!, holderName!!, creditCardDTO!!
            )
        }
        composable(Screen.Home.name) {

            val creditCardCollection = homeFieldViewModel.creditCardCollection.value
            val purchaseCollection = homeFieldViewModel.purchaseCollection.value

            if (creditCardCollection != null) {
                if (creditCardCollection.isEmpty()) {
                    homeFieldViewModel.updateCreditCards()
                }
            }

            if (purchaseCollection != null) {
                if (purchaseCollection.isEmpty()) {
                    homeFieldViewModel.updatePurchases()
                }
            }

            HomeScreen(
                navHostController, homeFieldViewModel
            )

            softInputMode(true)
        }
        composable(
            "${Screen.RegisterPurchase.name}?idCardCurrent={idCardCurrent}?isEditable={isEditable}?purchaseEdit={purchaseEdit}",
            arguments = listOf(
                navArgument("idCardCurrent") { type = NavType.LongType },
                navArgument("isEditable") { type = NavType.BoolType },
                navArgument("purchaseEdit") { type = NavType.StringType })
        ) { navBackStack ->
            softInputMode(false)

            val idCardCurrent = navBackStack.arguments?.getLong("idCardCurrent")
            val isEditable = navBackStack.arguments?.getBoolean("isEditable")
            val purchaseJson = navBackStack.arguments?.getString("purchaseEdit").toString()
            var purchaseEdit: Purchase? = null

            if (purchaseJson.isNotBlank()) {
                purchaseEdit = Purchase()

                purchaseEdit.toDTO(
                    ConversionUtils<PurchaseDTO>(PurchaseDTO::class.java).fromJson(purchaseJson)!!
                )
            }

            RegisterPurchaseScreen(
                navController = navHostController,
                idCardCurrent!!,
                isEditable,
                purchaseEdit
            )
        }
        composable(
            "${Screen.Spending.name}?idCard={idCard}",
            arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ) { navBackStack ->
            val idCard = navBackStack.arguments?.getLong("idCard")
            if (idCard != null) {
                SpendingScreen(navHostController, idCard)
            }
        }
        composable(Screen.ProductsManager.name) {
            softInputMode(false)

            productManagerFieldViewModel.searchPurchases(ObjectFilter())
            ProductsManagerScreen(productManagerFieldViewModel)
        }
        composable(Screen.Finance.name) {
            FinanceScreen(navHostController)
        }
        composable(Screen.Categories.name) {
            softInputMode(true)

            val categoryCollection = categoryFieldViewModel.categoryCollection.value

            if (categoryCollection != null && categoryCollection.isEmpty()) {
                categoryFieldViewModel.updateCategoryCollection()
            }

            CategoriesScreen(navHostController, categoryFieldViewModel)

        }
        composable(
            "${Screen.RegisterCategory.name}?idCategory={idCategory}",
            arguments = listOf(navArgument("idCategory") { type = NavType.LongType })
        ) { navBackStack ->
            val idCategory = navBackStack.arguments?.getLong("idCategory")
            val category = registerCategoryFieldViewModel.nameCategory.value

            if (idCategory != null && idCategory != 0L && category != null && category.isEmpty()) {
                LaunchedEffect(Unit) {
                    registerCategoryFieldViewModel.updateCategory(idCategory)
                    delay(500)
                }
            }

            RegisterCategoryScreen(
                navHostController,
                registerCategoryFieldViewModel,
                idCategory!!
            )

        }
        composable(
            Screen.MakingMarketScreen.name
        ) { navBackStack ->

            val arguments = navBackStack.arguments

            val idCard = arguments?.getLong("idCard") ?: 0L

            MakingMarketScreen(navHostController, idCard, marketItemFieldViewModel)
        }
        composable(
            "${Screen.ListPurchase.name}?idCard={idCard}",
            arguments = listOf(navArgument("idCard") { type = NavType.LongType })
        ) { navBackStack ->

            val idCard = navBackStack.arguments?.getLong("idCard")

            ListItemPurchaseScreen(navHostController, idCard!!, listItemFieldViewModel)

        }
        composable("${Screen.SettingsScreen.name}?idAvatar={idAvatar}?nickName={nickName}",
            arguments = listOf(
                navArgument("idAvatar") { type = NavType.IntType },
                navArgument("nickName") { type = NavType.StringType }
            )) { navBackStack ->

            val idAvatar = navBackStack.arguments?.getInt("idAvatar")
            val nickName = navBackStack.arguments?.getString("nickName")

            SettingsScreen(navHostController, idAvatar!!, nickName!!)

        }
        composable(Screen.ChoiceLogin.name) {
            ChoiceLogin(navHostController)
        }
        composable(Screen.Login.name) {
            Login(navHostController)
        }
        composable(Screen.Register.name) {
            Register(navHostController)
        }
    }
}