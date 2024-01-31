package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository
import com.example.myshoppinglist.services.controller.CategoryController
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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