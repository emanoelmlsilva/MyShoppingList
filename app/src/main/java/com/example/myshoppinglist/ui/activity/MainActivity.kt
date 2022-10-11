package com.example.myshoppinglist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myshoppinglist.controller.NavController
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyShoppingListTheme(darkTheme = false ) {
                val navController = rememberNavController()
                val context = LocalContext.current
                val userViewModel: UserViewModel = UserViewModel(context)

                val route = if(userViewModel.hasExistUser()) "home" else "createUser"

                Surface(color = MaterialTheme.colors.background) {
                    NavController(navHostController = navController, window, route)
                }
            }
        }
    }
}