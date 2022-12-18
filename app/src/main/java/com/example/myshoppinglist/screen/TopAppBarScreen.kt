package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun TopAppBarScreen(content: @Composable (() -> Unit?)?, onClickIcon: (() -> Unit)? = null, isScrollable: Boolean = false, modifier: Modifier = Modifier, color: Color = MaterialTheme.colors.background, contentColor: Color = contentColorFor(text_secondary), hasToolbar: Boolean = false, floatingActionButton:  @Composable () -> Unit = { }) {

    val baseModifier = if(!isScrollable) modifier else modifier.verticalScroll(
        rememberScrollState()
    )

    MyShoppingListTheme {
        Scaffold(
            topBar = {
                if(hasToolbar) {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = { if (onClickIcon != null) onClickIcon() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = secondary_dark
                                )
                            }
                        },
                        backgroundColor = text_secondary,
                        elevation = 0.dp
                    )
                }
            },
            modifier = Modifier.padding(bottom = if(floatingActionButton != {}) 65.dp else 0.dp),
            floatingActionButton = floatingActionButton,
            ) {
            content?.let { screen ->
                Surface(
                    color = color,
                    contentColor = contentColor,
                    modifier = baseModifier,
                    ) {screen()} }
        }
    }
}