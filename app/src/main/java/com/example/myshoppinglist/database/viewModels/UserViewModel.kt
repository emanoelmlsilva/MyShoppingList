package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.repositories.UserRepository

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

    fun getUserCurrent(){
        return repository.getUserCurrent()
    }
}