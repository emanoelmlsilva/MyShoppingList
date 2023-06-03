package com.example.myshoppinglist.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.ui.uiState.CategoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModelTeste : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())

    val uiState : StateFlow<CategoryUiState> = _uiState.asStateFlow()

    var idImage by mutableStateOf("")
        private set

    var color by mutableStateOf(0)
        private set

    fun onChangeImage(newIdImage: String){
        idImage = newIdImage
    }

    fun onChangeColor(newColor: Int){
        color = newColor
    }

}