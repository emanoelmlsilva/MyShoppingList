package com.example.myshoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.entities.User

@Database(entities = [(User::class)], version = 1, exportSchema = false)
abstract class MyShopListDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var instance: MyShopListDataBase? = null
        
        fun getInstance(context: Context): MyShopListDataBase {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyShopListDataBase::class.java,
                        "teste234"
                    ).allowMainThreadQueries().build()
                }
                return this.instance!!
        }
    }
}