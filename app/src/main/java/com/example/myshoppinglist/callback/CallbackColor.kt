package com.example.myshoppinglist.callback

import androidx.compose.ui.graphics.Color

abstract class CallbackColor: Callback {
    override fun onClick() {
        TODO("Not yet implemented")
    }
    abstract fun setColorCurrent(color: Color)

}