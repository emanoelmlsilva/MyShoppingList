package com.example.myshoppinglist.callback

import androidx.compose.ui.graphics.Color

interface CallbackColor: Callback {
    override fun onClick() {
        TODO("Not yet implemented")
    }
     fun onChangeValue(newValue: Color){}
}