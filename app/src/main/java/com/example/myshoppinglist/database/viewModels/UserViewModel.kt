package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.repositories.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job

class UserViewModel(context: Context): ViewModel() {

    private val repository: UserRepository
    val searchResult: MutableLiveData<User>

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val userDao = myShopListDataBase.userDao()

        repository = UserRepository(userDao)

        searchResult = repository.seachResult
    }

    fun updateUser(user: User){
        repository.updateUser(user)
    }

    fun insertUser(user: User, handler: CoroutineExceptionHandler) {
        repository.insertUser(user, handler)
    }

    fun findUserByName(email: String) {
        repository.findUserByEmail(email)
    }

    fun deleteUser(name: String) {
        repository.deleteUser(name)
    }

    fun hasExistUser(email: String): Boolean{
        return repository.hasExistUser(email)
    }
}