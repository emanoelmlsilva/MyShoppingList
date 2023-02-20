package com.example.myshoppinglist.database.sharedPreference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class UserLoggedShared private constructor(context: Context) {
    init {
        sharedPreference = context.getSharedPreferences("USER_LOGGED",Context.MODE_PRIVATE)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var instance: UserLoggedShared? = null
        var sharedPreference: SharedPreferences = TODO()
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
            var editor = sharedPreference.edit()

            editor.putString(USER_LOGGER, emailUser)
            editor.apply()
        }

        @JvmStatic
        fun logout(){
            var editor = sharedPreference.edit()
            editor.clear()
            editor.apply()
        }
    }
}