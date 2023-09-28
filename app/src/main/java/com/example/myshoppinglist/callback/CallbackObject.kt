package com.example.myshoppinglist.callback

interface CallbackObject<T> {

    fun onSuccess(objects: T){}

    fun onSuccess(){}

    fun onFailed(messageError: String){}

    fun onFailedException(exception: Exception){}

}