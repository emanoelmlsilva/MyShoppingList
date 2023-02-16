package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.components.DialogBackCustom
import com.example.myshoppinglist.ui.theme.*

@Composable
fun SettingsScreen(navController: NavHostController, idAvatar: Int, nickName: String) {
    var visibilityBackHandler by remember{ mutableStateOf(false) }

    DialogBackCustom(visibilityBackHandler, {
        visibilityBackHandler = false
//        navController.navigate()
    }, {
        visibilityBackHandler = false
    }, "Sair", "Deseja sair do aplicativo?")


    TopAppBarScreen(
        hasToolbar = true,
        hasDoneButton = true,
        colorDoneButton = text_primary_light,
        iconDone = R.drawable.ic_baseline_logout_24,
        onClickIcon = { navController.popBackStack() },
        onClickIconDone = {visibilityBackHandler = true},
        content = {
            Column(modifier = Modifier.fillMaxSize()) {

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(start = 22.dp)) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                    ) {
                        Column(modifier = Modifier.padding(top = 34.dp, start = 24.dp)) {
                            Image(
                                painter = painterResource(id = idAvatar),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(135.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, text_primary, CircleShape)
                            )

                        }

                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_brightness_6_24),
                                contentDescription = null,
                                tint = text_primary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                        Text(
                            text = nickName,
                            Modifier.padding(start = 34.dp),
                            color = text_primary,
                            fontFamily = LatoBlack
                        )

                        IconButton(onClick = {}, modifier = Modifier.padding(start = 16.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = text_primary
                            )
                        }
                    }
                }

                Divider(
                    color = divider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        })
}