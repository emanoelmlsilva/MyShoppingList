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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var errorLogin by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = UserViewModel(context)

    userViewModel.searchResult.observe(lifecycleOwner){
        isSuccess = it != null && it.email.isNotBlank()

        if (isSuccess) {
            errorLogin = false
            UserInstanceImpl.reset()
            UserInstanceImpl.getInstance(context, email)
            UserLoggedShared.insertUserLogged(email)
            navController.navigate(Screen.Home.name){
                popUpTo(Screen.Home.name) { inclusive = false }
            }
        }else{
            errorLogin = true
        }
    }

    LaunchedEffect(Unit){
        UserLoggedShared.getInstance(context)
    }

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
                            value = email,
                            reset = false,
                            maxChar = 45,
                            isCountChar = true,
                            isMandatory = true,
                            error = emailError,
                            customOnClick = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {
                                    email = newValue
                                }
                            })

                        TextInputComponent(
                            label = "Senha",
                            value = password,
                            reset = false,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            maxChar = 45,
                            isCountChar = true,
                            isMandatory = true,
                            error = passwordError,
                            customOnClick = object : CustomTextFieldOnClick {
                                override fun onChangeValue(newValue: String) {
                                    password = newValue
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

                    if(errorLogin){
                        Text(text = "Email ou Senha incorreto!", fontFamily = LatoRegular, fontSize = 14.sp, color = message_error)
                    }

                    Button(colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        onClick = {
                            emailError = email.isBlank()
                            passwordError = password.isBlank()

                            if(emailError || passwordError){
                                return@Button
                            }

                            userViewModel.findUserByName(email)
                        }) {
                        Text(text = "ENTRAR", fontFamily = LatoRegular, fontSize = 14.sp)
                    }

                    TextButton(modifier = Modifier.padding(bottom = 26.dp), onClick = {
                        navController.navigate(
                            Screen.Register.name
                        )
                    }) {
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