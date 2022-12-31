package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
    Box(
        modifier = modifier.size(size)
    ) {
        IconButton(enabled = enableClick, onClick = {
            callback?.onClick()
        }) {

            if (enabledBackground) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(size),
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = null,
                    tint = colorIcon.copy(alpha = 0.13f),
                )
            }
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