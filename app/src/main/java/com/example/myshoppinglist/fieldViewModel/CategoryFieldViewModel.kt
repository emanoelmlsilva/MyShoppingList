package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.services.controller.CategoryController

class CategoryFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel(){

    private val categoryController = CategoryController.getData(context, lifecycleOwner)
    private val categoryCollection: MutableLiveData<MutableList<Category>> =
        MutableLiveData(mutableListOf())
    private val categorySize = MutableLiveData(0)

    fun getCategoryController(): CategoryController{
        return categoryController
    }

    fun getCategoryCollection(): List<Category>{
        return categoryCollection.value!!
    }

    fun onChangeCategoryCollection(newCategoryCollection: List<Category>){
        categoryCollection.value!!.clear()
        categoryCollection.value!!.addAll(newCategoryCollection)
        categorySize.value = newCategoryCollection.size
    }

    fun getCategorySize(): Int{
        return categorySize.value!!
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

}