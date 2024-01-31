package com.example.myshoppinglist.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.enums.FilterFabState
import com.example.myshoppinglist.ui.theme.primary_dark
import com.example.myshoppinglist.ui.theme.secondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun FilterMultipleFabMenuButton(
    filterFabState: FilterFabState,
    callbackFabButton: CallbackObject<FilterFabState>,
    floatingActionCollection: List<@Composable (animatableFloatingActtion: Animatable<Float, AnimationVector1D>, modifier: Modifier) -> Unit>
) {

    val transitionState = remember {
        MutableTransitionState(filterFabState).apply {
            targetState = FilterFabState.DEFAULT
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "transition")

    val iconRotationDegree by transition.animateFloat({
        tween(durationMillis = 150, easing = FastOutSlowInEasing)
    }, label = "rotation") {
        if (filterFabState == FilterFabState.EXPANDED) 230f else 0f
    }

    Column(horizontalAlignment = Alignment.End) {

        FilterFabMenu(filterFabState, floatingActionCollection)

        Spacer(
            Modifier
                .height(13.dp)
        )

        FloatingActionButton(
            modifier = Modifier.rotate(iconRotationDegree),
            onClick = {
                if (filterFabState == FilterFabState.EXPANDED) {
                    callbackFabButton.onSuccess(FilterFabState.COLLAPSED)
                } else if (filterFabState == FilterFabState.COLLAPSED || filterFabState == FilterFabState.DEFAULT) {
                    callbackFabButton.onSuccess(FilterFabState.EXPANDED)
                }

            },
            backgroundColor = secondary
        ) {
            Icon(Icons.Filled.Add, null, tint = primary_dark)
        }


    }

}

@Composable
fun FilterFabMenu(
    filterFabState: FilterFabState,
    floatingActionCollection: List<@Composable (animatableFloatingActtion: Animatable<Float, AnimationVector1D>, modifier: Modifier) -> Unit>
) {

    val animatableActionCollection = floatingActionCollection.map { remember { Animatable(initialValue = 0f) } }
    val scope = rememberCoroutineScope()
    val DURATION: Int = 30
    val TIME_MILLIS = 200L

    fun upAnimatable(
        animatable: Animatable<Float, AnimationVector1D>
    ) {
        scope.launch(Dispatchers.Unconfined) {

            for (i in 0..10 step 2) {
                animatable.animateTo(
                    (i / 10.0).toFloat(),
                    tween(easing = FastOutSlowInEasing, durationMillis = DURATION)
                )
            }

            animatable.animateTo(1f, tween(easing = FastOutSlowInEasing, durationMillis = 0))

        }

    }

    fun dowAnimatable(
        animatable: Animatable<Float, AnimationVector1D>
    ) {
        scope.launch(Dispatchers.Unconfined) {

            for (i in 10 downTo 0 step 2) {
                animatable.animateTo(
                    (i / 10.0).toFloat(),
                    tween(easing = FastOutSlowInEasing, durationMillis = DURATION)
                )
            }

            animatable.animateTo(0f, tween(easing = FastOutSlowInEasing, durationMillis = DURATION))

        }

    }

    LaunchedEffect(filterFabState) {
        val animatableFloatingMenu =  if (filterFabState == FilterFabState.EXPANDED) animatableActionCollection.reversed() else animatableActionCollection
        animatableFloatingMenu.forEach { animatable ->
            if (filterFabState == FilterFabState.EXPANDED) {
                upAnimatable(animatable)
                delay(TIME_MILLIS)
            } else if (filterFabState == FilterFabState.COLLAPSED) {
                dowAnimatable(animatable)
                delay(TIME_MILLIS)
            }

        }

    }

    floatingActionCollection.mapIndexed{ index, floatingActionButton ->

        Spacer(
            Modifier
                .height(13.dp)
        )

        floatingActionButton(animatableActionCollection[index], modifier = Modifier.scale(animatableActionCollection[index].value))
    }

}