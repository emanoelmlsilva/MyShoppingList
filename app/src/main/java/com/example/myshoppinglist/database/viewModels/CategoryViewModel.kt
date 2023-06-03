package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action

class CategoryViewModel(context: Context, lifecycleOwner: LifecycleOwner) : ViewModel() {
    //repository
    private val repository: CategoryRepository

    val searchCollectionResult: MutableLiveData<List<Category>>
    val searchResult: MutableLiveData<Category>

    // user
    private var userViewModel: UserViewModel

    private var mLifecycleOwner: LifecycleOwner

    private val TAG = "CategoryViewModel"

    init {
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

    fun insertCategory(category: Category, callback: Callback) {
        val action = Action {
            repository.insertCateogry(category)
        }

        Completable.fromAction(action).subscribe({ callback.onSucess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun insertCategory(category: Category) {
        insertCategory(category, object : Callback{
            override fun onCancel() {
                super.onCancel()
            }

            override fun onSucess() {
                super.onSucess()
            }
        })
    }

    fun updateCategory(category: Category, callback: Callback) {
        val action = Action {
            repository.updateCategory(category)
        }

        val completable = Completable.fromAction(action)

        completable.subscribe(
            { callback.onSucess() },
            { throwable ->
                Log.d(TAG, "ERROR " + throwable.message)
                callback.onCancel()
            }
        )
    }

    fun getAll() {
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner) {
            emailUser = it.email
            repository.getAll(emailUser)
        }
    }

    fun getCategoryById(idCategory: Long) {
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner) {
            emailUser = it.email
            repository.getCategoryById(emailUser, idCategory)
        }
    }

    fun getCategoryByCategory(category: String) {
        var emailUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner) {
            emailUser = it.email
            repository.getCategoryByCategory(emailUser, category)
        }
    }
}