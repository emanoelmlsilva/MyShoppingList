package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.text_primary


@Composable
fun ButtonsFooterContent(btnTextAccept: String, btnTextCancel: String? = null, iconAccept: ImageVector? = null, iconCancel: ImageVector? = null, onClickAccept: () -> Unit, onClickCancel: (() -> Unit)? = null){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 12.dp), horizontalArrangement = if(btnTextCancel != null) Arrangement.SpaceBetween else Arrangement.Center){
        if(btnTextCancel != null){
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(0.4F)
                    .background(secondary)
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                onClick = {
                    if (onClickCancel != null) {
                        onClickCancel()
                    }
                }) {
                Text(btnTextCancel, color = text_primary)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                if (iconCancel != null) {
                    Icon(
                        iconCancel,
                        contentDescription = btnTextCancel,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
            onClick = { onClickAccept() }) {
            Text(btnTextAccept)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            if (iconAccept != null) {
                Icon(
                    iconAccept,
                    contentDescription = btnTextAccept,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }

        }
    }
}