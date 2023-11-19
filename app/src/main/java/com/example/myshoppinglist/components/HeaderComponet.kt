package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.text_primary

@ExperimentalAnimationApi
@Composable
fun HeaderComponent(navController: NavController, idAvatar: Int, nickName: String, visibleAnimation: Boolean, callBack: Callback) {

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, bottom = 3.dp, end = 18.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = if(idAvatar == 0) R.drawable.default_avatar else idAvatar),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .border(1.dp, text_primary, CircleShape)
                        )

                        Text(
                            text = "Ola, $nickName",
                            Modifier.padding(start = 12.dp),
                            color = text_primary
                        )
                    }
                    IconButton(onClick = { navController.navigate("${Screen.SettingsScreen.name}?idAvatar=${idAvatar}?nickName=${nickName}") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_miscellaneous_services_24),
                            contentDescription = "help",
                            tint = text_primary,
                        )
                    }
                }
            }
        }
    )
}