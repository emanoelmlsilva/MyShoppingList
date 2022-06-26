package com.example.myshoppinglist.database.repositories

import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.entities.User
import kotlinx.coroutines.*

class UserRepository(private val userDao: UserDao) {

    val seachResult = MutableLiveData<User>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertUser(newUser: User){
        coroutineScope.launch(Dispatchers.IO) {
            userDao.insertUser(newUser)
        }
    }

    fun deleteUser(name: String){
        coroutineScope.launch(Dispatchers.IO){
            userDao.deleteUser(name)
        }
    }

    fun findUserByName(name: String){
        coroutineScope.launch(Dispatchers.Main){
            seachResult.value = asyncFind(name).await()
        }
    }

    fun hasExistUser(): Boolean{
      return userDao.hasContainUser() > 0
    }

    private fun asyncFind(name: String): Deferred<User?> = coroutineScope.async(Dispatchers.IO){
        return@async userDao.findUserByName(name)
    }
}