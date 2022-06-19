package com.example.myshoppinglist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshoppinglist.screen.CreateCards
import com.example.myshoppinglist.screen.CreateUser
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
                NavHost(navController = navController, startDestination = "createUser") {
                    composable("createUser") {
                        window.statusBarColor = secondary.hashCode()
                        CreateUser(navController)
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
                                CreateCards(navController)
                            }
                        } else {

                            CreateCards(navController)
                        }
                    }

                }
            }
        }
    }
}