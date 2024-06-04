package com.example.myshoppinglist.components

import android.app.Activity
import android.view.View
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
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
import com.example.myshoppinglist.enums.StatusSaveData
import com.example.myshoppinglist.ui.theme.*

private fun customStatusBar(color: Int = secondary.hashCode(), view: View) {
    val activity = view.context as Activity
    activity.window.statusBarColor = color
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun StatusSaveDataComponent(
    isFlowDelete: Boolean = false,
    visibility: Boolean,
    status: StatusSaveData,
    statusMain: Int = R.raw.save
) {
    val view = LocalView.current

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = statusMain)
    )

    val statusAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = if (status.icon == StatusSaveData.ERROR_DELETE.icon) StatusSaveData.ERROR.icon else status.icon)
    )

    DisposableEffect(view) {
        onDispose {
            customStatusBar(view = view)
        }
    }

    SideEffect {
        customStatusBar(
            (if ((status.icon != StatusSaveData.DELETE.icon && status.icon != StatusSaveData.ERROR_DELETE.icon) && !isFlowDelete) primary_dark else message_error).hashCode(),
            view
        )
    }

    BaseAnimationComponent(visibleAnimation = visibility, contentBase = {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(if ((status.icon != StatusSaveData.DELETE.icon && status.icon != StatusSaveData.ERROR_DELETE.icon) && !isFlowDelete) primary_dark else message_error),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(size = 200.dp),
                        iterations = LottieConstants.IterateForever,
                        composition = composition,
                    )

                    Spacer(Modifier.size(35.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.3f)
                            .padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        LottieAnimation(
                            modifier = Modifier.width(status.width),
                            iterations = LottieConstants.IterateForever,
                            composition = statusAnimation,
                        )
                        Text(
                            modifier = Modifier.padding(
                                start = if (status.icon != StatusSaveData.WAITING.icon && status.icon != StatusSaveData.DELETE.icon) 25.dp else 0.dp,
                                end = 30.dp
                            ),
                            text = status.message,
                            fontFamily = LatoRegular,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = text_secondary
                        )
                    }
                }
            })
    })
}