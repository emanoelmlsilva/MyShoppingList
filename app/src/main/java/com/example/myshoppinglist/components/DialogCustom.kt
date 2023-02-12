package com.example.myshoppinglist.components

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myshoppinglist.ui.theme.background_card
import com.example.myshoppinglist.ui.theme.background_card_gray_light

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogCustom(
    visibilityDialog: Boolean,
    content: @Composable() (() -> Unit)
) {
    val context = LocalContext.current
    val windowManager =
        remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    val metrics = DisplayMetrics().apply {
        windowManager.defaultDisplay.getRealMetrics(this)
    }
    val (width, height) = with(LocalDensity.current) {
        Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
    }

    if (visibilityDialog) {

        Card(
            backgroundColor = background_card_gray_light, modifier = Modifier
                .fillMaxWidth()
        ) {

            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { },
                content = {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Card(
                            backgroundColor = background_card,
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredSize(width, (height / 1.8f)),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            ),
                        ) {
                            content()
                        }
                    }

                })
        }
    }
}