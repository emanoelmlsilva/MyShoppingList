package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*

@Composable
fun ChoiceLogin(navController: NavController) {
    TopAppBarScreen(onClickIcon = { navController.popBackStack() }, content = {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.55f)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.my_shopping_list_logo),
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .paint(
                        painterResource(id = R.drawable.background_shape),
                        contentScale = ContentScale.FillBounds
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    Column(
                        modifier = Modifier.fillMaxHeight(.35f),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(colors = ButtonDefaults.buttonColors(backgroundColor = background_card),
                            modifier = Modifier
                                .fillMaxWidth(.75f)
                                .padding(vertical = 4.dp),
                            onClick = {
                                navController.navigate(Screen.Login.name) {
                                    popUpTo(0) { inclusive = false }
                                }
                            }) {
                            Text(text = "LOGIN", fontFamily = LatoRegular, fontSize = 14.sp)
                        }

                        TextButton(onClick = { navController.navigate(Screen.Register.name)}) {
                            Text(
                                text = "Não tem conta criada? Sign up",
                                fontFamily = LatoRegular,
                                fontSize = 16.sp,
                                color = Color(0xFF05290A)
                            )
                        }
                    }
                }
            }
        }
    })
}