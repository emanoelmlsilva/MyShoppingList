package com.example.myshoppinglist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun BaseAnimationComponent(visibleAnimation: Boolean? = true, contentBase: @Composable () -> Unit) {

    AnimatedVisibility(
        visible = visibleAnimation!!,
        enter = slideInVertically(
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        ),
        exit = shrinkVertically(
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        ),
        content = {
            contentBase()
        })
}