package com.example.myshoppinglist.callback

abstract class VisibleCallback : Callback {
    override fun onClick() {
        TODO("Not yet implemented")
    }
    abstract fun onChangeVisible(visible: Boolean)

}