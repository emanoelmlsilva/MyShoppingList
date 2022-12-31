package com.example.myshoppinglist.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.myshoppinglist.database.daos.CategoryDAO
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.daos.UserDao
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.User

@Database(entities = [User::class, CreditCard::class, Purchase::class, Category::class], version = 5, exportSchema = true
, autoMigrations = [AutoMigration (
    from = 4,
    to = 5,
    spec = MyShopListDataBase.MyAutoMigration::class
)])
abstract class MyShopListDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDAO
    abstract fun purchaseDAO(): PurchaseDAO
    abstract fun categoryDAO(): CategoryDAO

    @DeleteColumn(tableName = "purchases",columnName = "category")
    @RenameColumn(
        tableName = "purchases",
        fromColumnName = "category",
        toColumnName = "categoryOwnerId"
    )
    class MyAutoMigration : AutoMigrationSpec{}

    companion object {
        private var instance: MyShopListDataBase? = null

        fun getInstance(context: Context): MyShopListDataBase {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyShopListDataBase::class.java,
                        "MyShopList.db"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
                return this.instance!!
        }
    }
}