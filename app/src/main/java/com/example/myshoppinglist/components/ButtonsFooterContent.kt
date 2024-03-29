package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.secondary_light
import com.example.myshoppinglist.ui.theme.text_primary


@Composable
fun ButtonsFooterContent(
    isClickable: Boolean? = true,
    btnTextAccept: String,
    btnTextCancel: String? = null,
    iconAccept: ImageVector? = null,
    iconCancel: ImageVector? = null,
    onClickAccept: (() -> Unit)? = null,
    onClickCancel: (() -> Unit)? = null,
    callback: Callback? = null,
    modifierButton: Modifier = Modifier.padding(start = 6.dp, bottom = 16.dp, end = 6.dp),
    modifier: Modifier = Modifier
        .fillMaxWidth().padding(0.dp, if (btnTextCancel != null) 12.dp else 0.dp),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (btnTextCancel != null) Arrangement.SpaceBetween else Arrangement.Center
    ) {
        if (btnTextCancel != null) {
            OutlinedButton(
                modifier = modifierButton,
//                    .padding(start = 6.dp, bottom = 8.dp, end = 6.dp),
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
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = if (isClickable!!) primary else secondary_light),
            modifier = modifierButton
                .clickable(onClick = {
                }), onClick = {
                if (onClickAccept != null) {
                    onClickAccept()
                    callback?.onClick()

                }
            }) {
            Text(btnTextAccept)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            if (iconAccept != null) {
                Icon(
                    iconAccept,
                    contentDescription = btnTextAccept,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}