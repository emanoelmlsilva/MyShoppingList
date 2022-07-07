package com.example.myshoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User

@Database(entities = [User::class, CreditCard::class], version = 2, exportSchema = false)
abstract class MyShopListDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDAO

    companion object {
        private var instance: MyShopListDataBase? = null

        fun getInstance(context: Context): MyShopListDataBase {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyShopListDataBase::class.java,
                        "teste02"
                    ).allowMainThreadQueries().build()
                }
                return this.instance!!
        }
    }
}