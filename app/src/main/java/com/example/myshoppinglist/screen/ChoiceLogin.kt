package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*

@Composable
fun ChoiceLogin(navController: NavController) {
    TopAppBarScreen(
        onClickIcon = { navController.popBackStack() }, content = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight() ,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.65f)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.my_shopping_list_logo),
                    contentDescription = null
                )
            }

            Column(  modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
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
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.8f)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxHeight(.7f),
                            verticalArrangement = Arrangement.SpaceEvenly,
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

                            Text(
                                text = "ou",
                                fontFamily = LatoRegular,
                                fontSize = 16.sp,
                                color = text_primary_light
                            )

                            TextButton(onClick = { navController.navigate(Screen.Register.name) }) {
                                Text(
                                    buildAnnotatedString {
                                        append("Não tem conta criada?")
                                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                            append(" Sign up")
                                        }
                                    },
                                    fontFamily = LatoRegular,
                                    fontSize = 16.sp,
                                    color = Color(0xFF05290A)
                                )
                            }
                        }
                    }
                }
            }

        }
    })
}