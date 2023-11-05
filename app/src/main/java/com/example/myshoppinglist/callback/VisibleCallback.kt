package com.example.myshoppinglist.callback

interface VisibleCallback : Callback {
    override fun onClick() {
        TODO("Not yet implemented")
    }
    fun onChangeVisible(visible: Boolean){}
}