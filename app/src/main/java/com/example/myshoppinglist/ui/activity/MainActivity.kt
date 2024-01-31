package com.example.myshoppinglist.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.controller.NavController
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.shadow
import com.example.myshoppinglist.ui.theme.text_primary
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyShoppingListTheme(darkTheme = false) {
                val navController = rememberNavController()
                val route = intent.getStringExtra(ROUTE_INITIAL)
                var bottomBarState by rememberSaveable { (mutableStateOf(true)) }

                val screenBarCollection = listOf(
                    Screen.Home,
                    Screen.Categories,
                    Screen.ProductsManager
                )
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    bottomBar = {
                        if(bottomBarState){
                            BottomNavigation(
                                elevation = 0.dp,
                                backgroundColor = (shadow.copy(alpha = 0.9f))
                            ) {
                                screenBarCollection.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(painter = painterResource(id = screen.drawable),
                                                modifier = Modifier
                                                    .size(24.dp),
                                                contentDescription = null,
                                                tint = if (currentDestination?.hierarchy?.any { it.route == screen.name } == true) primary else text_primary)
                                        },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.name } == true,
                                        onClick = {
                                            navController.navigate(screen.name) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    if (route != null) {
                        NavController(
                            navHostController = navController,
                            window,
                            route,
                            object : VisibleCallback {
                                override fun onChangeVisible(visible: Boolean) {
                                    bottomBarState = visible
                                }
                            })
                    }
                }
            }
        }
    }
}