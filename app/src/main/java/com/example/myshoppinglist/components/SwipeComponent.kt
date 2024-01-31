package com.example.myshoppinglist.components

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.CallbackSwipe
import com.example.myshoppinglist.ui.theme.message_error
import com.example.myshoppinglist.ui.theme.primary_dark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SwipeComponent(
    iconLeft: ImageVector = Icons.Outlined.Delete,
    iconRight: ImageVector = Icons.Outlined.Edit,
    colorBackground: Color = Color.Transparent,
    index: Int,
    onSwipe: (Int) -> Unit,
    onDragStart: (Int) -> Unit,
    onDragEnd: () -> Unit,
    callback: CallbackSwipe,
    dismissBackground: @Composable () -> Unit
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val swipeableState = rememberSwipeableState(initialValue = 0)

    val directionLeft = swipeableState.direction > -1
    val directionDefault = swipeableState.direction == 0F
    var isBeingManipulated by remember { mutableStateOf(false) }

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                if ((swipeableState.progress.from == 1 || swipeableState.progress.from == 2)) {
                    isBeingManipulated = false
                    scope.launch {
                        if (swipeableState.progress.from == 2) {
                            callback.onHandlerHighAction()
                        } else {
                            callback.onHandlerLeftAction()
                        }
                        swipeableState.snapTo(0)
                        onSwipe(index)
                        onDragEnd()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onDragStart(index)
                        isBeingManipulated = true
                    }
                }
                false
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(
                        0f to 0,
                        -dipToPx(context, 60f) to 1,
                        dipToPx(context, 60f) to 2
                    ),
                    orientation = androidx.compose.foundation.gestures.Orientation.Horizontal,
                )
                .background(if (directionDefault || !isBeingManipulated) colorBackground else if (directionLeft) primary_dark else message_error)
        ) {

            Icon(
                iconRight,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                contentDescription = "Archive"
            )

            Icon(
                iconLeft,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                contentDescription = "delete"
            )

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            if (isBeingManipulated) swipeableState.offset.value.roundToInt() else 0,
                            0
                        )
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                dismissBackground()
            }
        }
    }

}

private fun dipToPx(context: Context, dipValue: Float): Float {
    return dipValue * context.resources.displayMetrics.density
}