package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.background_opacity
import com.example.myshoppinglist.ui.theme.primary_light
import com.example.myshoppinglist.ui.theme.secondary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WaitingProcessComponent(visibilityWaiting: Boolean, messageError: String, callback: Callback) {

    if (visibilityWaiting) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { callback.onClick() },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight().background(background_opacity),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = primary_light,
                            modifier = Modifier.size(90.dp),
                            strokeWidth = 1.dp
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    Text(
                        text = messageError,
                        color = secondary,
                        textAlign = TextAlign.Center,
                        fontFamily = LatoBlack,
                    )
                }
            })
    }
}