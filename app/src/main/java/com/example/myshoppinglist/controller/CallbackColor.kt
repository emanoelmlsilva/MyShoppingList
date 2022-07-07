package com.example.myshoppinglist.controller

import androidx.compose.ui.graphics.Color

abstract class CallbackColor: Callback{
    override fun onClick() {
        TODO("Not yet implemented")
    }
    abstract fun setColorCurrent(color: Color)

}