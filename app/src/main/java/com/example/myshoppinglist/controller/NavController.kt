package com.example.myshoppinglist.controller

import android.content.Context
import android.view.Window
import android.view.WindowManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.screen.*
import com.example.myshoppinglist.utils.ConversionUtils
import com.example.myshoppinglist.utils.MountStructureCrediCard
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

    fun softInputMode(isKeyBoard: Boolean){
        window.setSoftInputMode(if(isKeyBoard) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable(Screen.CreateUser.name) {
            callback.onChangeVisible(false)
            CreateUserScreen(navHostController, isUpdate = false, hasToolbar = false)
        }
        composable("${Screen.CreateUser.name}?isUpdate={isUpdate}?hasToolbar={hasToolbar}",
            arguments = listOf(
                navArgument("isUpdate") { type = NavType.BoolType },
                navArgument("hasToolbar") { type = NavType.BoolType }
            )) { navBackStack ->
            callback.onChangeVisible(false)
            val isUpdate = navBackStack.arguments?.getBoolean("isUpdate")
            val hasToolbar = navBackStack.arguments?.getBoolean("hasToolbar")
            CreateUserScreen(navHostController, isUpdate ?: false, hasToolbar)
        }
        composable(
            "${Screen.CreateCards.name}?hasToolbar={hasToolbar}?holderName={holderName}?isUpdate={isUpdate}?creditCardDTO={creditCardDTO}",
            arguments = listOf(
                navArgument("hasToolbar") { type = NavType.BoolType },
                navArgument("holderName") { type = NavType.StringType },
                navArgument("creditCardDTO") { type = NavType.StringType },
                navArgument("isUpdate") { type = NavType.BoolType })
        ) { navBackStack ->
            callback.onChangeVisible(false)
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
            val context = LocalContext.current
            val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
            val homeFieldViewModel = HomeFieldViewModel(context, lifecycleOwner)
            val email = UserLoggedShared.getEmailUserCurrent()

            softInputMode(true)
            callback.onChangeVisible(true)

            val valueUser = homeFieldViewModel.getUser(email).observeAsState()

            val valueCreditCollection = homeFieldViewModel.getCreditCardController().getAllWithSumDB().observeAsState()

            val valuePurchaseCollection = homeFieldViewModel.getPurchaseController().getPurchasesAndCategoryWeekDB().observeAsState()

            if (valueUser.value != null && valueCreditCollection.value != null && valuePurchaseCollection.value != null) {

                if(valueUser.value!!.name.isNotEmpty() && valueUser.value!!.idAvatar != 0){
                    homeFieldViewModel.onChangeNickName(valueUser.value!!.nickName)
                    homeFieldViewModel.onChangeIdAvatar(valueUser.value!!.idAvatar)
               }

                if (valueCreditCollection.value!!.isNotEmpty()) {
                    homeFieldViewModel.onChangeCreditCardCollection(valueCreditCollection.value!!)
                }

                if(valuePurchaseCollection.value!!.isNotEmpty()){
                    homeFieldViewModel.onChangePurchaseCollection(valuePurchaseCollection.value!!)
                }

                HomeScreen(navHostController, homeFieldViewModel)
            }

        }
        composable(
            "${Screen.RegisterPurchase.name}?idCardCurrent={idCardCurrent}?isEditable={isEditable}?purchaseEdit={purchaseEdit}",
            arguments = listOf(navArgument("idCardCurrent") { type = NavType.LongType }, navArgument("isEditable") {type = NavType.BoolType}, navArgument("purchaseEdit") {type = NavType.StringType})
        ) { navBackStack ->
            callback.onChangeVisible(false)
            val idCardCurrent = navBackStack.arguments?.getLong("idCardCurrent")
            val isEditable = navBackStack.arguments?.getBoolean("isEditable")
            val purchaseJson = navBackStack.arguments?.getString("purchaseEdit").toString()
            var purchaseEdit: Purchase? = null

            if(purchaseJson.isNotBlank()){
                purchaseEdit = Purchase()

                purchaseEdit.toDTO(
                    ConversionUtils<PurchaseDTO>(PurchaseDTO::class.java).fromJson(purchaseJson)!!
                )
            }

            RegisterPurchaseScreen(navController = navHostController, idCardCurrent!!, isEditable, purchaseEdit)
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
            softInputMode(false)
            callback.onChangeVisible(true)
            ProductsManagerScreen(navHostController)
        }
        composable(Screen.Finance.name) {
            callback.onChangeVisible(true)
            FinanceScreen(navHostController)
        }
        composable(Screen.Categories.name) {
            softInputMode(true)
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
        composable(Screen.ChoiceLogin.name){
            ChoiceLogin(navHostController)
        }
        composable(Screen.Login.name){
            Login(navHostController)
        }
        composable(Screen.Register.name){
            Register(navHostController)
        }
    }
}