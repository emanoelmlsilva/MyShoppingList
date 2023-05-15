package com.example.myshoppinglist.database.repositories

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.daos.*
import com.example.myshoppinglist.database.entities.User
import kotlinx.coroutines.*

class UserRepository(private val userDao: UserDao) {

    val seachResult = MutableLiveData<User>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val defaultHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    fun updateUser(newUser: User){
        coroutineScope.launch(Dispatchers.IO) {
            userDao.updateUser(newUser)
        }
    }

    fun insertUser(newUser: User){
        insertUser(newUser, defaultHandler)
    }


    fun insertUser(newUser: User, handler: CoroutineExceptionHandler){
        coroutineScope.launch(Dispatchers.IO) {
            try {
                userDao.insertUser(newUser)
            } catch (e: Exception) {
                handler.handleException(coroutineScope.coroutineContext, e)
            }
        }
    }

    fun deleteUser(context: Context){
        coroutineScope.launch(Dispatchers.IO){
            MyShopListDataBase.getInstance(context).clearAllTables()
        }
    }

    fun findUserByEmail(name: String){
        coroutineScope.launch(Dispatchers.Main){
            seachResult.value = asyncFind(name).await()
        }
    }

    fun hasExistUser(email: String): Boolean{
        val user = userDao.findUserByEmail(email)
      return user != null && user.email.isNotBlank()
    }

    private fun asyncFind(name: String): Deferred<User?> = coroutineScope.async(Dispatchers.IO){
        return@async userDao.findUserByEmail(name)
    }
}