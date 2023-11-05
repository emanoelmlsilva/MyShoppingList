package com.example.myshoppinglist.callback

interface CallbackObject<T> : Callback{

    fun onSuccess(objects: T){}

    override fun onSuccess(){}

    override fun onFailed(messageError: String){}

    fun onFailedException(exception: Exception){}

}