package com.example.myshoppinglist.database.viewModels

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    abstract fun checkFileds(): Boolean
}