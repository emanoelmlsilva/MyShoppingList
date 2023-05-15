@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.myshoppinglist.screen

import android.util.Log
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
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.entities.relations.UserWithCreditCard
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.CreditCardService
import com.example.myshoppinglist.services.MyShoppingListService
import com.example.myshoppinglist.services.UserService
import com.example.myshoppinglist.services.controller.LoadingDataController
import com.example.myshoppinglist.ui.theme.*
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController: NavController) {
    val LOG = "LOGIN"
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
    val userService = UserService.getUserService()
    var visibleLoading by remember { mutableStateOf(false) }

    userViewModel.searchResult.observe(lifecycleOwner) {

        navController.navigate(Screen.Home.name) {
            popUpTo(Screen.Home.name) { inclusive = false }
        }

    }

    LaunchedEffect(Unit) {
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

                    if (errorLogin) {
                        Text(
                            text = "Email ou Senha incorreto!",
                            fontFamily = LatoRegular,
                            fontSize = 14.sp,
                            color = message_error
                        )
                    }

                    Button(colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        onClick = {

                            emailError = email.isBlank()
                            passwordError = password.isBlank()

                            if (emailError || passwordError) {
                                errorLogin = true
                                return@Button
                            }

                            visibleLoading = true

                            userService.findUser(email, password).enqueue(object :
                                Callback<User> {
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    Log.d(
                                        LOG,
                                        "success = $response , user ${response.body().toString()}"
                                    )

                                    if(response.isSuccessful){
                                        val user = response.body()

                                        errorLogin = false

                                        UserInstanceImpl.reset()
                                        UserInstanceImpl.getInstance(context, email)
                                        UserLoggedShared.insertUserLogged(email)

                                        userViewModel.insertUser(user!!, CoroutineExceptionHandler { _, exception -> })

                                        LoadingDataController.getData(context, lifecycleOwner).loadingData(user, object : com.example.myshoppinglist.callback.Callback{
                                            override fun onSucess() {
                                                userViewModel.findUserByName(email)
                                                visibleLoading = false
                                            }

                                            override fun onCancel() {
                                                visibleLoading = false
                                                errorLogin = true
                                            }
                                        })

                                    }else{
                                        visibleLoading = false
                                        errorLogin = true
                                    }

                                }

                                override fun onFailure(call: Call<User>?, t: Throwable?) {
                                }
                            })
                        }) {

                        if(!visibleLoading){
                            Text(text = "ENTRAR", fontFamily = LatoRegular, fontSize = 14.sp)
                        }else{
                            Column(modifier = Modifier.fillMaxWidth(.17f), horizontalAlignment = Alignment.CenterHorizontally){
                                CircularProgressIndicator(
                                    color = text_secondary,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 1.dp
                                )
                            }
                        }
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