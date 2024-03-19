package com.example.myshoppinglist.model

import android.content.Context
import com.example.myshoppinglist.database.viewModels.UserViewModelDB

class UserInstanceImpl{

    companion object {
        private var userViewModel: UserViewModelDB? = null
        private var instance: UserInstanceImpl? = null

        fun getInstance(context: Context): UserInstanceImpl {
            userViewModel = UserViewModelDB(context)

            if (instance == null) {
                instance = UserInstanceImpl()
            }
            return this.instance!!
        }
    }

    fun getUserViewModelCurrent(): UserViewModelDB{
        return userViewModel!!
    }

    fun reset(){
        instance = null
        userViewModel = null
    }

}