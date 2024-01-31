package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.components.CarouselComponent
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.fieldViewModel.HomeFieldViewModel
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_secondary
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    navController: NavController?,
    homeFieldViewModel: HomeFieldViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val userDTO by homeFieldViewModel.getUser().observeAsState(UserDTO())
    val purchaseCollection by homeFieldViewModel.purchaseCollection.observeAsState(emptyList())
    val creditCardCollection by homeFieldViewModel.creditCardCollection.observeAsState(emptyList())

    LaunchedEffect(Unit){
        keyboardController?.hide()
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            HeaderComponent(
                navController!!,
                userDTO.idAvatar,
                userDTO.nickName,
                true,
                object : Callback {
                    override fun onClick() {
                    }
                })

            CarouselComponent(
                list = creditCardCollection,
                parentModifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f),
                contentHeight = 265.dp,
                navController = navController!!
            )

            Spacer(Modifier.size(32.dp))

            Text(
                text = "Histórico Semanal",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                fontFamily = LatoBold,
                fontSize = 24.sp
            )

            Spacer(Modifier.size(24.dp))

            BoxPurchaseHistoryComponent(
                purchaseCollection
            )
        }
    }

}