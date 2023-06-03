package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.repositories.CategoryRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.ui.uiState.CategoryUiState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(context: Context, lifecycleOwner: LifecycleOwner) : ViewModel() {
    //repository
    private val repository: CategoryRepository

    val searchCollectionResult: MutableLiveData<List<Category>>
    val searchResult: MutableLiveData<Category>

    // user
    private var userViewModel: UserViewModel

    private var mLifecycleOwner: LifecycleOwner

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

    fun insertCategory(category: Category) {
        repository.insertCateogry(category)
//        Observable.just(repository.insertCateogry(category))
//            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ Log.d("TESTANDO", "value")  },
//                { throwable -> Log.d("TESTANDO", "ERROR "+throwable.message)},
//                { Log.d("TESTANDO", "COMPLETADO")
//                    callback.onSucess()
//                })

    }

    fun updateCategory(category: Category, callback: Callback) {
        val action = Action {
            repository.updateCategory(category)
        }

        val completable = Completable.fromAction(action)

        completable.subscribe(
            {Log.d("TESTANDO", "value")
                callback.onSucess()},
            { throwable ->  Log.d("TESTANDO", "ERROR " + throwable.message) }
        )

//        Observable.just(category)
//            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ categoryObserver ->
//                repository.updateCategory(categoryObserver)
//                Log.d("TESTANDO", "value" + categoryObserver)
//            },
//                { throwable -> Log.d("TESTANDO", "ERROR " + throwable.message) },
//                {
//                    Log.d("TESTANDO", "COMPLETADO")
//                    callback.onSucess()
//                })
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