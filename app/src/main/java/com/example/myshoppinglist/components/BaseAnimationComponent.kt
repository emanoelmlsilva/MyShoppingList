package com.example.myshoppinglist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@ExperimentalAnimationApi
@Composable
fun BaseAnimationComponent(visibleAnimation: Boolean? = true, contentBase: @Composable () -> Unit) {
    val initialValue = 1f
    val animatedProgress = remember { Animatable(initialValue) }
    val durationMillis = 150

    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(
            0.5f,
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = durationMillis
            )
        )
    }

    AnimatedVisibility(
        visible = visibleAnimation!!,
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = 300, easing = FastOutLinearInEasing
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = 300, easing = FastOutLinearInEasing
            )
        ),
        content = {
            contentBase()
        })
}