package com.example.myshoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.User

@Database(entities = [User::class, CreditCard::class, Purchase::class], version = 3, exportSchema = false)
abstract class MyShopListDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDAO
    abstract fun purchaseDAO(): PurchaseDAO

    companion object {
        private var instance: MyShopListDataBase? = null

        fun getInstance(context: Context): MyShopListDataBase {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyShopListDataBase::class.java,
                        "teste03"
                    ).allowMainThreadQueries().build()
                }
                return this.instance!!
        }
    }
}