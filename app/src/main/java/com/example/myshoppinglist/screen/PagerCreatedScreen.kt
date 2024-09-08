@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)

package com.example.myshoppinglist.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.CallbackPagerState
import com.example.myshoppinglist.components.PageVisibility
import com.example.myshoppinglist.database.dtos.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PagerCreatedScreen(navController: NavController?) {
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    var userDTO by remember { mutableStateOf(UserDTO()) }

    val callbackPagerState = object : CallbackPagerState{
        override fun nextPager(pagerState: PagerState) {
            coroutineScope.launch {
                pagerState.scrollToPage(1)
            }
        }

        override fun prevPager(pagerState: PagerState) {
            coroutineScope.launch {
                pagerState.scrollToPage(0)
            }

        }
    }

    val onboardPages = listOf<@Composable () -> Unit>(
        {
            CreateUserScreen(
                navController = navController,
                isUpdate = false,
                hasToolbar = false,
                buttonsFooter = true,
                nextPager = {
                    userDTO = it
                    callbackPagerState.nextPager(pagerState)
                }
            )
        },
        {
            CreateCardScreen(
                navController = navController,
                hasToolbar = false,
                isUpdate = false,
                holderNameUser = userDTO.name,
                creditCardDTOJson = "",
                buttonsFooter = true,
                prevPager = {
                    callbackPagerState.prevPager(pagerState)},
                userDTOCurrent = userDTO
            )
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                PageVisibility(pagerState.currentPage == (onboardPages.size - 1))
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                userScrollEnabled = false
            ) { page ->
                onboardPages[page]()
            }
        }
    }
}