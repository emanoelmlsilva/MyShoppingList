@file:OptIn(ExperimentalFoundationApi::class)

package com.example.myshoppinglist.callback

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState

interface CallbackPagerState : Callback {

    fun nextPager(pagerState: PagerState)

    fun prevPager(pagerState: PagerState)

}