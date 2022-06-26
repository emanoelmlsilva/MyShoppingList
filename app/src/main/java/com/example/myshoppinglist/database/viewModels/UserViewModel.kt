package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.repositories.UserRepository

class UserViewModel(context: Context): ViewModel() {

    private val repository: UserRepository
    val searchResult: MutableLiveData<User>

    init {
        val userDb = MyShopListDataBase.getInstance(context)
        val userDao = userDb.userDao()

        repository = UserRepository(userDao)

        searchResult = repository.seachResult
    }

    fun insertUser(user: User) {
        repository.insertUser(user)
    }

    fun findUserByName(name: String) {
        repository.findUserByName(name)
    }

    fun deleteUser(name: String) {
        repository.deleteUser(name)
    }

    fun hasExistUser(): Boolean{
        return repository.hasExistUser()
    }
}