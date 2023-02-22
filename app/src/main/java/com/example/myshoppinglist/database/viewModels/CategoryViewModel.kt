package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import kotlinx.coroutines.CoroutineExceptionHandler

class CategoryViewModel(context: Context, lifecycleOwner: LifecycleOwner): ViewModel() {

    private val repository: CategoryRepository
    val searchCollectionResult: MutableLiveData<List<Category>>
    val searchResult: MutableLiveData<Category>
    private var userViewModel: UserViewModel
    private var mLifecycleOwner: LifecycleOwner

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
            val categoryDAO = myShopListDataBase.categoryDAO()
        userViewModel = UserViewModel(context)
        val email = UserLoggedShared.getEmailUserCurrent()
        userViewModel.findUserByName(email)
        repository = CategoryRepository(categoryDAO)
        searchCollectionResult = repository.searchCollectionResult
        searchResult = repository.searchResult
        mLifecycleOwner = lifecycleOwner
    }

    fun insertCategory(category: Category){
        repository.insertCateogry(category)
    }

    fun updateCategory(category: Category){
        repository.updateCategory(category)
    }

    fun getAll(){
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            emailUser = it.email
            repository.getAll(emailUser)
        }
    }

    fun getCategoryById(idCategory: Long){
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            emailUser = it.email
            repository.getCategoryById(emailUser, idCategory)
        }
    }

    fun getCategoryByCategory(category: String){
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            emailUser = it.email
            repository.getCategoryByCategory(emailUser, category)
        }
    }
}