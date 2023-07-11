package com.example.myshoppinglist.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(private val userDao: UserDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateUser(newUser: User) {
        coroutineScope.launch(Dispatchers.IO) {
            userDao.updateUser(newUser)
        }
    }

    fun insertUser(newUser: User) {
        coroutineScope.launch(Dispatchers.IO) {
            userDao.insertUser(newUser)
        }
    }

    fun deleteUser(context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            MyShopListDataBase.getInstance(context).clearAllTables()
        }
    }

    fun hasExistUser(email: String): Boolean {
        val userDTO = userDao.findUserByEmail(email).value
        return userDTO != null && userDTO.email.isNotBlank()
    }

    fun findUserByEmail(name: String): LiveData<UserDTO> {
        return userDao.findUserByEmail(name)
    }
}