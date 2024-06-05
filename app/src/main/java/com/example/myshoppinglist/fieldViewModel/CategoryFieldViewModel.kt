package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.services.controller.CategoryController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel(){

    private val TAG = "CategoryViewModel"
    private val categoryController = CategoryController.getData(context, lifecycleOwner)
    val categoryCollection = MutableLiveData(emptyList<Category>())
    val categorySize = MutableLiveData(0)

    fun updateCategoryCollection(){
        viewModelScope.launch(Dispatchers.Main) {
            categoryController.getAllDB().observeForever{
                categoryCollection.value = it
                categorySize.value = it.size
            }
        }
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

}

data class CategoryState(
    val categorySize: Int = 0,
    val categoryCollectionFlowUI: List<Category> = emptyList())
