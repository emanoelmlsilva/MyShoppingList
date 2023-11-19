package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.components.CarouselComponent
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.fieldViewModel.HomeFieldViewModel
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_secondary
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(navController: NavController?, homeFieldViewModel: HomeFieldViewModel) {
    var visibleAnimation by remember { mutableStateOf(true) }

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
                homeFieldViewModel.getIdAvatar(),
                homeFieldViewModel.getNickName(),
                visibleAnimation,
                object : Callback {
                    override fun onClick() {
                        homeFieldViewModel.onChangeVisibleValue()
                    }
                })

            CarouselComponent(
                list = homeFieldViewModel.getCreditCardCollection(),
                visibleAnimation = visibleAnimation,
                parentModifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f),
                contentHeight = 265.dp,
                navController = navController
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
                homeFieldViewModel.getPurchaseCollection(),
                object : VisibleCallback {
                    override fun onChangeVisible(visible: Boolean) {
                        if (visibleAnimation != visible) {
                            visibleAnimation = visible
                        }
                    }
                })
        }
    }

}