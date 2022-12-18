package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository

class CategoryViewModel(context: Context, lifecycleOwner: LifecycleOwner): ViewModel() {

    private val repository: CategoryRepository
    val searchCollectionResult: MutableLiveData<List<Category>>
    private var userViewModel: UserViewModel
    private var mLifecycleOwner: LifecycleOwner

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val categoryDAO = myShopListDataBase.categoryDAO()
        userViewModel = UserViewModel(context)
        userViewModel.getUserCurrent()
        repository = CategoryRepository(categoryDAO)
        searchCollectionResult = repository.searchCollectionResult
        mLifecycleOwner = lifecycleOwner
    }

    fun insertCategory(category: Category){
        repository.insertCateogry(category)
    }

    fun updateCategory(category: Category){
        repository.updateCategory(category)
    }

    fun getAll(){
        var nameUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            nameUser = it.name
            repository.getAll(nameUser)
        }
    }

}