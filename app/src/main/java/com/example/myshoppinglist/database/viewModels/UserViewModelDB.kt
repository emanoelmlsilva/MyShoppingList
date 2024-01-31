package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class UserViewModelDB(var context: Context): ViewModel() {

    private val repository: UserRepository

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val userDao = myShopListDataBase.userDao()

        repository = UserRepository(userDao)
    }

    fun updateUser(user: User){
        repository.updateUser(user)
    }

    fun insertUser(user: User) {
        repository.insertUser(user)
    }

    fun findUserByName(email: String) : LiveData<UserDTO> {
        return repository.findUserByEmail(email)
    }

    fun deleteUser() {
        repository.deleteUser(context)
    }

    fun hasExistUser(email: String): Boolean{
        return repository.hasExistUser(email)
    }
}