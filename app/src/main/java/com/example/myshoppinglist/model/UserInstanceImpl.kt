package com.example.myshoppinglist.model

import android.content.Context
import com.example.myshoppinglist.database.viewModels.UserViewModel

class UserInstanceImpl{

    companion object {
        var userViewModel: UserViewModel? = null
        private var instance: UserInstanceImpl? = null

        fun getInstance(context: Context, email: String): UserInstanceImpl {
            userViewModel = UserViewModel(context)

            if (instance == null) {
                instance = UserInstanceImpl()
                userViewModel!!.findUserByName(email)
            }
            return this.instance!!
        }


        fun getUserViewModelCurrent(): UserViewModel{
            return userViewModel!!
        }

        fun reset(){
            instance = null
            userViewModel = null
        }
    }

}