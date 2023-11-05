package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action

class CategoryViewModelDB(context: Context, lifecycleOwner: LifecycleOwner) : ViewModel() {
    //repository
    private val repository: CategoryRepository

    private var mLifecycleOwner: LifecycleOwner

    private val TAG = "CategoryViewModel"

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val categoryDAO = myShopListDataBase.categoryDAO()
        val email = UserLoggedShared.getEmailUserCurrent()
        repository = CategoryRepository(categoryDAO)
        mLifecycleOwner = lifecycleOwner
    }

    fun insertCategories(categories: List<Category>, callback: Callback){
        val action = Action{
            repository.insertCategories(categories)
        }

        Completable.fromAction(action).subscribe({callback.onSuccess()}, {throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun insertCategory(category: Category, callback: Callback) {
        val action = Action {
            repository.insertCategory(category)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun insertCategory(category: Category) {
        insertCategory(category, object : Callback {
            override fun onCancel() {
                super.onCancel()
            }

            override fun onSuccess() {
                super.onSuccess()
            }
        })
    }

    fun updateCategory(category: Category) {
        updateCategory(category, object : Callback {
            override fun onCancel() {
                super.onCancel()
            }

            override fun onSuccess() {
                super.onSuccess()
            }
        })
    }

    fun updateCategory(category: Category, callback: Callback) {
        val action = Action {
            repository.updateCategory(category)
        }

        val completable = Completable.fromAction(action)

        completable.subscribe(
            { callback.onSuccess() },
            { throwable ->
                Log.d(TAG, "ERROR " + throwable.message)
                callback.onCancel()
            }
        )
    }

    fun getAll(): LiveData<List<Category>> {
        return repository.getAll()
    }

    fun getCategoryById(idCategory: Long): LiveData<Category> {
        return repository.getCategoryById(idCategory)
    }

    fun getCategoryByCategory(category: String) {
//        var emailUser = ""
//        userViewModel.searchResult.observe(mLifecycleOwner) {
//            emailUser = it.email
//            repository.getCategoryByCategory(emailUser, category)
//        }
    }
}