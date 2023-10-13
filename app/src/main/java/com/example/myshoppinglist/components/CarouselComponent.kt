package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CarouselComponent(
    count: Int,
    visibleAnimation: Boolean = true,
    parentModifier: Modifier = Modifier.fillMaxWidth(),
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {

    val listState = rememberLazyListState(count / 2)

    LaunchedEffect(key1 = count) {
        listState.animateScrollToItem(count)
    }

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {
            BoxWithConstraints(
                modifier = parentModifier
            ) {
                val halfRowWidth = constraints.maxWidth / 2

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(),
                    verticalArrangement = Arrangement.spacedBy(-contentHeight / 3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = count,
                        itemContent = { globalIndex ->

                            val scale by remember {
                                derivedStateOf {
                                    val currentItem =
                                        listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == globalIndex }
                                            ?: return@derivedStateOf 0.85f

                                    (1f - minOf(
                                        1f,
                                        Math.abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth)
                                            .toFloat() / halfRowWidth
                                    ) * 0.3f)
                                }
                            }

                            content(
                                index = globalIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(contentHeight)
                                    .scale(scale)
                                    .zIndex(scale)
                            )
                        }
                    )
                }
            }
        })
}
