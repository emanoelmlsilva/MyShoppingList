package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myshoppinglist.ui.theme.primary_light

@Composable
fun LoadingComposable(visibilityDialog: Boolean) {

    if (visibilityDialog) {
        Dialog(
            onDismissRequest = { },
            content = {
                CircularProgressIndicator(
                    color = primary_light,
                    modifier = Modifier.size(90.dp),
                    strokeWidth = 1.dp
                )
            })
    }
}