package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.VisibleCallback

@Composable
fun BaseLazyColumnScroll(
    visibleAnimation: Boolean, callback: VisibleCallback?, modifier: Modifier? = null, content: LazyListScope.() -> Unit
) {
    val listState = rememberLazyListState()
    val modifierDefault = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    val limitedIndexScroll = 2
    val limitedVisibleOffset = 5
    val indexInitial = 0

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (listState.isScrollInProgress && listState.firstVisibleItemIndex < limitedIndexScroll) {
                    if (listState.firstVisibleItemIndex == indexInitial && listState.firstVisibleItemScrollOffset <= limitedVisibleOffset) {
                        callback?.onChangeVisible(true)
                    } else if (visibleAnimation && listState.firstVisibleItemScrollOffset >= limitedVisibleOffset) {
                        callback?.onChangeVisible(false)
                    }
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (listState.firstVisibleItemIndex > indexInitial && visibleAnimation) {
            callback?.onChangeVisible(false)
        }
    }

    LazyColumn(
        state = listState,
        modifier = (modifier?:modifierDefault).nestedScroll(nestedScrollConnection)
    ) {
        content()
    }
}