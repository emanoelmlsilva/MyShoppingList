package com.example.myshoppinglist.screen

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun TopAppBarScreen(content: @Composable (() -> Unit?)?, onClickIcon: () -> Unit){
    MyShoppingListTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { onClickIcon() }) {
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
        ) {
            content?.let { screen -> screen() }
        }
    }
}