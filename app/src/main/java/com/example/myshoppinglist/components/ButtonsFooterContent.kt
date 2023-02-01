package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.secondary_light
import com.example.myshoppinglist.ui.theme.text_primary


@Composable
fun ButtonsFooterContent(isClickable: Boolean? = true, btnTextAccept: String, btnTextCancel: String? = null, iconAccept: ImageVector? = null, iconCancel: ImageVector? = null, onClickAccept: (() -> Unit)? = null, onClickCancel: (() -> Unit)? = null){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, if(btnTextCancel != null) 12.dp else 0.dp), horizontalArrangement = if(btnTextCancel != null) Arrangement.SpaceBetween else Arrangement.Center){
        if(btnTextCancel != null){
            OutlinedButton(
                modifier = Modifier
                    .padding(start = 6.dp, bottom = 8.dp, end = 6.dp)
                ,
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

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = if(isClickable!!) primary else secondary_light),
            modifier = Modifier
                .padding(start = 6.dp, bottom = 16.dp, end = 6.dp)
                .clickable(onClick = {
                }), onClick = {                     if(onClickAccept != null){
                onClickAccept()
            }
            }) {
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