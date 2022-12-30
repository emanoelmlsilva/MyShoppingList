package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
    listState: LazyListState = rememberLazyListState(),
    visibleAnimation: Boolean,
    callback: VisibleCallback?,
    modifier: Modifier? = null,
    content: LazyListScope.() -> Unit
) {
    val modifierDefault = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {

                if (listState.isScrollInProgress) {
                    val positionScroll = available.y
                    if (positionScroll <= 0) {
                        callback?.onChangeVisible(false)
                    } else if (positionScroll >= 30) {
                        callback?.onChangeVisible(true)
                    }
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = (modifier ?: modifierDefault).nestedScroll(nestedScrollConnection)
    ) {
        content()
    }
}