package com.example.myshoppinglist.screen

import ResultData
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.components.WarningNoConnection
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.UserViewModelDB
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.UserService
import com.example.myshoppinglist.services.controller.LoadingDataController
import com.example.myshoppinglist.services.repository.LoginRepository
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.ui.viewModel.LoginViewModel
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Register(navController: NavController) {
    val LOG = "REGISTER"
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var emailError by remember {
        mutableStateOf(false)
    }
    var passwordError by remember {
        mutableStateOf(false)
    }
    var passwordConfirmError by remember {
        mutableStateOf(false)
    }
    var isError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    val loginViewModel =
        LoginViewModel(LoginRepository(UserService.getUserService()), UserViewModelDB(context))
    var errorConnection by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var exceptionError by remember { mutableStateOf(Exception()) }

    TopAppBarScreen(
        enableScroll = true,
        onClickIcon = { navController.popBackStack() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WarningNoConnection(errorConnection, exceptionError, object : Callback {
                    override fun onClick() {
                        errorConnection = false
                    }
                })

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 50.dp, bottom = 221.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.my_shopping_list_logo),
                            contentDescription = null
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
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier.fillMaxHeight(.65f),
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
                                keyboardActions = KeyboardActions(
                                    onDone = { keyboardController?.hide() }),
                                customOnClick = object : CustomTextFieldOnClick {
                                    override fun onChangeValue(newValue: String) {
                                        email = newValue
                                        if (isError) {
                                            isError = false
                                        }
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
                                keyboardActions = KeyboardActions(
                                    onDone = { keyboardController?.hide() }),
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

                            TextInputComponent(
                                label = "Confirmar senha",
                                value = passwordConfirm,
                                reset = false,
                                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                maxChar = 45,
                                isCountChar = true,
                                isMandatory = true,
                                keyboardActions = KeyboardActions(
                                    onDone = { keyboardController?.hide() }),
                                error = passwordConfirmError,
                                customOnClick = object : CustomTextFieldOnClick {
                                    override fun onChangeValue(newValue: String) {
                                        passwordConfirm = newValue
                                    }
                                },
                                trailingIcon = {
                                    val image = if (showConfirmPassword)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff


                                    IconButton(onClick = {
                                        showConfirmPassword = !showConfirmPassword
                                    }) {
                                        Icon(imageVector = image, null)
                                    }
                                })
                        }

                        Button(colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            onClick = {

                                if (!loading) {

                                    emailError = email.isBlank()
                                    passwordError = password.isBlank()
                                    passwordConfirmError = passwordConfirm.isBlank()

                                    if (emailError || passwordError || passwordConfirmError) {
                                        return@Button
                                    } else if (password != passwordConfirm) {
                                        passwordConfirmError = true
                                        return@Button
                                    }

                                    loading = true

                                    keyboardController?.hide()

                                    val user = UserDTO(email, password)

                                    loginViewModel.singUp(
                                        user,
                                        object : CallbackObject<UserDTO> {
                                            override fun onSuccess() {
                                                Log.d(LOG, "Sucesso ao fazer signUp")

                                                LoadingDataController.getData(context, lifecycleOwner).loadingDataCategories(email, object : Callback {
                                                    override fun onSuccess() {
                                                        UserLoggedShared.insertUserLogged(email)
                                                        UserInstanceImpl.getInstance(context)

                                                        navController.navigate("${Screen.CreateUser.name}?isUpdate=${false}?hasToolbar=${false}") {
                                                            popUpTo(0) { inclusive = false }
                                                        }
                                                    }

                                                    override fun onFailed(messageError: String) {
                                                        Log.d(TAG, "findAndSaveCategories - onFailed ")
                                                    }
                                                })

                                            }

                                            override fun onFailedException(exception: Exception) {

                                                exceptionError = exception

                                                when (exception) {
                                                    is ConnectException -> {
                                                        errorConnection = true
                                                        ResultData.NotConnectionService(UserDTO())
                                                    }
                                                    is SocketTimeoutException -> {
                                                        errorConnection = true
                                                        ResultData.NotConnectionService(UserDTO())
                                                    }

                                                }

                                                loading = false
                                            }
                                        })

                                }

                            }) {

                            if (loading) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 13.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(20.dp),
                                        color = background_card,
                                        strokeWidth = 2.dp
                                    )
                                }
                            } else {
                                Text(text = "CRIAR", fontFamily = LatoRegular, fontSize = 14.sp)
                            }


                        }

                        Text(
                            "ou",
                            fontFamily = LatoRegular,
                            fontSize = 10.sp,
                            color = text_title_secondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Button(
                            elevation = ButtonDefaults.elevation(0.dp),
                            modifier = Modifier.padding(bottom = 16.dp),
                            onClick = {
                                navController.navigate(Screen.Login.name)
                            },
                            border = BorderStroke(1.dp, text_primary),
                            colors = ButtonDefaults.buttonColors(backgroundColor = background_card)
                        ) {
                            Text(
                                text = "Login",
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