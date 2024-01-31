package com.example.myshoppinglist.database.sharedPreference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.example.myshoppinglist.database.viewModels.UserViewModelDB

class UserLoggedShared private constructor(context: Context) {

    init {
        sharedPreference = context.getSharedPreferences("USER_LOGGED",Context.MODE_PRIVATE)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var instance: UserLoggedShared? = null
        lateinit var sharedPreference: SharedPreferences
        private const val USER_LOGGER = "user_logger"

        @JvmStatic
        fun getInstance(context: Context): UserLoggedShared{
            if(instance == null){
                instance = UserLoggedShared(context)
            }

            return instance!!
        }

        @JvmStatic
        fun insertUserLogged(emailUser: String){
            val editor = sharedPreference.edit()

            editor.putString(USER_LOGGER, emailUser)
            editor.apply()
        }

        @JvmStatic
        fun getEmailUserCurrent(): String{
            return sharedPreference.getString(USER_LOGGER, "")!!
        }

        @JvmStatic
        fun logout(context: Context){
            val userViewModel = UserViewModelDB(context)

            userViewModel.deleteUser()

            val editor = sharedPreference.edit()
            editor.clear()
            editor.apply()
        }
    }
}