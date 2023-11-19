package com.example.myshoppinglist.components

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.animation.AnimationUtils
import kotlin.math.*

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun CarouselComponent(
    count: Int,
    visibleAnimation: Boolean = true,
    parentModifier: Modifier = Modifier.fillMaxWidth(),
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column(modifier = Modifier.fillMaxWidth()) {

        BaseAnimationComponent(
            visibleAnimation = visibleAnimation,
            contentBase = {
                BoxWithConstraints(
                    modifier = parentModifier
                ) {

                    VerticalPager(
                        count = count, state = pagerState,
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                    ) { page ->
                        Card(
                            backgroundColor = Color.Transparent,
                            elevation = 0.dp,
                            modifier = Modifier
                                .fillMaxHeight()
                                .graphicsLayer {
                                    val pageOffset =
                                        calculateCurrentOffsetForPage(page).absoluteValue

                                    AnimationUtils
                                        .lerp(
                                            1.15f,
                                            1f,
                                            1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                        .also { scale ->
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                }
                             .aspectRatio(1.45f)
                        ) {
                            content(
                                index = page,
                                modifier = Modifier
                                    .padding()
                                    .height(contentHeight)
                                    .offset {
                                        val pageOffset =
                                            this@VerticalPager.calculateCurrentOffsetForPage(page)
                                        // Then use it as a multiplier to apply an offset
                                        IntOffset(
                                            x = (40.dp * pageOffset).roundToPx(),
                                            y = 0,
                                        )
                                    }
                            )

                        }

                    }

                }
            })
    }

}