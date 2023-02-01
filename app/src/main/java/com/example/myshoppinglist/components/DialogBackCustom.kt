package com.example.myshoppinglist.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import com.example.myshoppinglist.ui.theme.*

@Composable
fun DialogBackCustom(visibility: Boolean, onConfirm: () -> Unit, onCancel: () -> Unit, title: String, text: String) {
    var visibilityDialog by remember { mutableStateOf(visibility) }

    LaunchedEffect(key1 = visibility) {
        visibilityDialog = visibility
    }

    if (visibilityDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(colors = ButtonDefaults.textButtonColors(contentColor = primary_dark), onClick = {
                    visibilityDialog = false
                    onConfirm()
                })
                { Text(text = "Sim") }
            },
            dismissButton = {
                TextButton(colors = ButtonDefaults.textButtonColors(contentColor = text_primary), onClick = {
                    visibilityDialog = false
                    onCancel()
                })
                { Text(text = "Não") }
            },
            title = { Text(text = title, fontFamily = LatoBlack, color = text_primary) },
            text = { Text(text = text, fontFamily = LatoRegular, color = text_primary) }
        )
    }
}