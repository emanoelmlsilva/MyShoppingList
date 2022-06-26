package com.example.myshoppinglist.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshoppinglist.controller.NavController
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.screen.CreateCards
import com.example.myshoppinglist.screen.CreateUser
import com.example.myshoppinglist.screen.CreateUserViewModel
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyShoppingListTheme(darkTheme = false ) {
                val navController = rememberNavController()
                val context = LocalContext.current
                val userViewModel: UserViewModel = UserViewModel(context)

                val route = if(userViewModel.hasExistUser()) "createUser" else "createUser"

                Surface(color = MaterialTheme.colors.background) {
                    NavController(navHostController = navController, window, route)
                }
            }
        }
    }
}