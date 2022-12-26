package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.MyShoppingListTheme
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun TopAppBarScreen(
    content: @Composable (() -> Unit?)?,
    onClickIcon: (() -> Unit)? = null,
    onClickIconDone: (() -> Unit)? = null,
    isScrollable: Boolean = false,
    modifier: Modifier = Modifier,
    hasBackButton: Boolean = true,
    color: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(text_secondary),
    hasToolbar: Boolean = false,
    floatingActionButton: @Composable () -> Unit = { }
) {

    val baseModifier = if (!isScrollable) modifier else modifier.verticalScroll(
        rememberScrollState()
    )

    MyShoppingListTheme {
        Scaffold(
            topBar = {
                if (hasToolbar) {
                    TopAppBar(
                        title = {},
                        actions = {
                            if(!hasBackButton){
                                IconButton(onClick = { if (onClickIconDone != null) onClickIconDone()  }) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = null,
                                        tint = primary
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { if (onClickIcon != null) onClickIcon() }) {
                                Icon(
                                    imageVector = if(hasBackButton) Icons.Filled.ArrowBack else Icons.Filled.Close ,
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