//package com.example.myshoppinglist.database.sharedPreference
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.SharedPreferences
//
//class ControllerThemeShared private constructor(context: Context) {
//    init {
//        sharedPreference = context.getSharedPreferences("CONTROLLER_THEME",Context.MODE_PRIVATE)
//    }
//
//    companion object{
//        @SuppressLint("StaticFieldLeak")
//        private var instance: ControllerThemeShared? = null
//        private lateinit var sharedPreference: SharedPreferences
//        private const val CONTROLLER_THEME = "controller_theme"
//
//        @JvmStatic
//        fun getInstance(context: Context): ControllerThemeShared{
//            if(instance == null){
//                instance = ControllerThemeShared(context)
//            }
//
//            return instance!!
//        }
//
//        @JvmStatic
//        fun updateTheme(){
//            val editor = sharedPreference.edit()
//            val theme = sharedPreference.getBoolean(CONTROLLER_THEME, false)
//
//            editor.putBoolean(CONTROLLER_THEME, !theme)
//            editor.apply()
//        }
//
//        @JvmStatic
//        fun getThemeDark(): Boolean{
//            return sharedPreference.getBoolean(CONTROLLER_THEME, false)
//        }
//    }
//}