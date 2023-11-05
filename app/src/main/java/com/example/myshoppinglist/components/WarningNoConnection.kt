package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun WarningNoConnection(visibility: Boolean, exception: Exception, callback: Callback) {

    fun getMessageException(): String {
        return when (exception) {
            is ConnectException -> {
                "Sem conexão. \nReconecte e tente novamente!"
            }
            is SocketTimeoutException -> {
                "Servidor indisponível. \nTente novamente mais tarde!"
            }
            else -> {
                ""
            }
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = visibility) {
        if (visibility) {
            scope.launch(context = Dispatchers.Main) {
                delay(2000L)
                callback.onClick()
            }
        }
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.no_connection)
    )

    BaseAnimationComponent(visibleAnimation = visibility, contentBase = {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = secondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.25f)
                    ) {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            LottieAnimation(
                                modifier = Modifier.size(size = 120.dp),
                                iterations = LottieConstants.IterateForever,
                                composition = composition,
                            )

                            Spacer(modifier = Modifier.height(height = 12.dp))

                            Text(
                                text = getMessageException(),
                                fontFamily = LatoRegular,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(height = 12.dp))
                        }
                    }
                }
            })
    })

}