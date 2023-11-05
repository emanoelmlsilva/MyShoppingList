package com.example.myshoppinglist.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.components.rememberImeState
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun TopAppBarScreen(
    content: @Composable (() -> Unit?)?,
    iconDone: Int? = R.drawable.ic_baseline_done_24,
    hasDoneButton: Boolean = false,
    colorDoneButton: Color? = primary,
    contentHeader: @Composable (() -> Unit?)? = {},
    onClickIcon: (() -> Unit)? = null,
    onClickIconDone: (() -> Unit)? = null,
    enableScroll: Boolean = false,
    modifier: Modifier = Modifier,
    hasBackButton: Boolean = true,
    color: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(text_secondary),
    hasToolbar: Boolean = false,
    floatingActionButton: @Composable () -> Unit = { },
    paddingFloatingButton: Dp = 0.dp
) {

    val imeState = rememberImeState()
    val scrollState: ScrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(1300))
    }

    val baseModifier = if (enableScroll) modifier.verticalScroll(scrollState) else modifier

    MyShoppingListTheme {
        Scaffold(
            topBar = {
                if (hasToolbar) {
                    TopAppBar(
                        title = {},
                        actions = {
                            if(hasDoneButton){
                                IconButton(onClick = { if (onClickIconDone != null) onClickIconDone()  }) {
                                    Icon(
                                        painter = painterResource(iconDone!!),
                                        contentDescription = null,
                                        tint = colorDoneButton!!
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { if (onClickIcon != null) onClickIcon() }) {
                                Icon(
                                    painter = painterResource(id = if(hasBackButton) R.drawable.chevron_left else R.drawable.close),
                                    contentDescription = null,
                                    tint = secondary_dark,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },
                        backgroundColor = text_secondary,
                        elevation = 0.dp
                    )
                }

                if(contentHeader != null) {
                    contentHeader()
                }
            },
            modifier = Modifier.padding(bottom = paddingFloatingButton),
            floatingActionButton = floatingActionButton,
        ) {
            content?.let { screen ->
                Surface(
                    color = color,
                    contentColor = contentColor,
                    modifier = baseModifier,
                    ) {screen()
                }
            }
        }
    }
}