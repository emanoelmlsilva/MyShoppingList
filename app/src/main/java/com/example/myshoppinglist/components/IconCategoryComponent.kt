package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.ui.theme.card_blue
import com.example.myshoppinglist.ui.theme.text_primary

@Composable
fun IconCategoryComponent(
    modifier: Modifier = Modifier,
    iconCategory: ImageBitmap,
    enableClick: Boolean = false,
    enabledBackground: Boolean = false,
    colorIcon: Color = text_primary,
    size: Dp = 46.dp,
    callback: Callback? = null
) {

    val backgroundModifier = if (enabledBackground) Modifier
        .background(color = colorIcon.copy(alpha = 0.13f)) else Modifier

    if (enabledBackground) {
        Surface(
            elevation = 1.dp,
            shape = CircleShape,
            modifier = modifier
                .size(size)
        ) {

            IconComponent(size, backgroundModifier, enableClick, callback, iconCategory, colorIcon)

        }
    }
    else {
        IconComponent(size, backgroundModifier, enableClick, callback, iconCategory, colorIcon)
    }
}

@Composable
fun IconComponent(
    size: Dp,
    backgroundModifier: Modifier = Modifier,
    enableClick: Boolean,
    callback: Callback?,
    iconCategory: ImageBitmap,
    colorIcon: Color
) {
    Box(
        backgroundModifier,
        contentAlignment = Alignment.Center
    ) {
        IconButton(enabled = enableClick, onClick = {
            callback?.onClick()
        }) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(size / 2),
                bitmap = iconCategory,
                contentDescription = null,
                tint = colorIcon
            )
        }
    }
}