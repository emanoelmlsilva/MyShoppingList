package com.example.myshoppinglist.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.ui.theme.*


@Composable
fun ButtonCancel(
    modifierButton: Modifier,
    btnTextCancel: String? = null,
    rightBtnCancel: Boolean? = true,
    isEnabled: Boolean? = true,
    iconCancel: ImageVector? = null,
    onClickCancel: (() -> Unit)? = null
) {
    if (btnTextCancel != null) {
        OutlinedButton(
            modifier = modifierButton,
            enabled = isEnabled!!,
            onClick = {
                if (onClickCancel != null) {
                    onClickCancel()
                }
            }) {
            if (!rightBtnCancel!! && (iconCancel != null)) {
                Icon(
                    iconCancel,
                    tint = text_primary,
                    contentDescription = btnTextCancel,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            Text(btnTextCancel, color = text_primary)
            if (rightBtnCancel && (iconCancel != null)) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    iconCancel,
                    tint = text_primary,
                    contentDescription = btnTextCancel,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ButtonsFooterContent(
    isClickable: Boolean? = true,
    btnTextAccept: String,
    btnTextCancel: String? = null,
    iconAccept: ImageVector? = null,
    iconCancel: ImageVector? = null,
    onClickAccept: (() -> Unit)? = null,
    onClickCancel: (() -> Unit)? = null,
    rightBtnCancel: Boolean? = true,
    isEnabled: Boolean? = true,
    callback: Callback? = null,
    modifierButton: Modifier = Modifier.padding(start = 6.dp, bottom = 16.dp, end = 6.dp),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp, if (btnTextCancel != null) 12.dp else 0.dp),
) {


    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Row(
            modifier = modifier,
            horizontalArrangement = if (btnTextCancel != null) Arrangement.SpaceBetween else Arrangement.Center
        ) {

            ButtonCancel(
                modifierButton,
                btnTextCancel,
                rightBtnCancel,
                isEnabled,
                iconCancel,
                onClickCancel
            )

            Button(colors = ButtonDefaults.buttonColors(backgroundColor = if (isClickable!!) primary else secondary_light),
                modifier = modifierButton
                    ,onClick = {
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
}