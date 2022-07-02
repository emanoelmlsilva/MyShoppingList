package com.example.myshoppinglist.database.viewModels

import androidx.lifecycle.ViewModel

abstract class BaseFieldViewModel: ViewModel() {
    abstract fun checkFileds(): Boolean
}