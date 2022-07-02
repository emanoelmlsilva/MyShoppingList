package com.example.myshoppinglist.controller

import android.view.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myshoppinglist.screen.CreateCardScreen
import com.example.myshoppinglist.screen.CreateUserScreen
import com.example.myshoppinglist.screen.CreateUserViewModel
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun NavController(navHostController: NavHostController, window: Window, routeInitial: String){
    NavHost(navController = navHostController, startDestination = routeInitial) {
        composable("createUser") {
            val createUserViewModel: CreateUserViewModel = viewModel()
            window.statusBarColor = secondary.hashCode()
            CreateUserScreen(navHostController, createUserViewModel)
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
                                IconButton(onClick = { }) {
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
                    CreateCardScreen(navHostController)
                }
            } else {

                CreateCardScreen(navHostController)
            }
        }
    }
}