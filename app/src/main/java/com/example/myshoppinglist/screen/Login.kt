@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.myshoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController: NavController) {
    var showPassword by remember { mutableStateOf(false) }

    TopAppBarScreen(onClickIcon = { navController.popBackStack() }, content = {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(.5f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.my_shopping_list_logo_small),
                        contentDescription = null
                    )

                    Text(
                        text = "Bem vindo de volta!",
                        fontFamily = LatoBlack,
                        fontSize = 32.sp,
                        color = text_primary_light
                    )
                }
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
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(
                        modifier = Modifier.fillMaxHeight(.50f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextInputComponent(
                            label = "Email",
                            value = "",
                            reset = false,
                            maxChar = 45,
                            isCountChar = true,
                            isMandatory = true,
                            error = false,
                            customOnClick = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {

                                }
                            })

                        TextInputComponent(
                            label = "Password",
                            value = "",
                            reset = false,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            maxChar = 45,
                            isCountChar = true,
                            isMandatory = true,
                            error = false,
                            customOnClick = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {

                                }
                            },
                            trailingIcon = {
                                val image = if (showPassword)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff


                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(imageVector = image, null)
                                }
                            })
                    }

                    Button(colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        onClick = { }) {
                        Text(text = "ENTRAR", fontFamily = LatoRegular, fontSize = 14.sp)
                    }

                    TextButton(modifier = Modifier.padding(bottom = 26.dp), onClick = { }) {
                        Text(
                            text = "Não tem conta criada? Sign up",
                            fontFamily = LatoRegular,
                            fontSize = 16.sp,
                            color = Color(0xFF05290A),
                        )
                    }
                }
            }
        }
    })
}