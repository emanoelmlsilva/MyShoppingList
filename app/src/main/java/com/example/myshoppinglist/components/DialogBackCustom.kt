package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.ui.theme.*

@Composable
fun DialogBackCustom(visibility: Boolean, onConfirm: () -> Unit, onCancel: () -> Unit, title: String, text: String) {
    var visibilityDialog by remember { mutableStateOf(visibility) }

    LaunchedEffect(key1 = visibility) {
        visibilityDialog = visibility
    }

    if (visibilityDialog) {
        AlertDialog(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
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
            title = { Text(text = title, fontFamily = LatoBlack, color = text_primary, modifier = Modifier.padding(6.dp), fontSize = 14.sp)},
            text = { Text(text = text, fontFamily = LatoRegular, color = text_primary, modifier = Modifier.padding(start = 16.dp), fontSize = 13.sp) }
        )
    }
}