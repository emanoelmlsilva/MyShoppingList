package com.example.myshoppinglist.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.myshoppinglist.database.daos.*
import com.example.myshoppinglist.database.entities.*

@Database(entities = [User::class, CreditCard::class, Purchase::class, Category::class, ItemList::class], version = 6, exportSchema = true
, autoMigrations = [AutoMigration (
    from = 5,
    to = 6,
    spec = MyShopListDataBase.MyAutoMigration::class
)])
abstract class MyShopListDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDAO
    abstract fun purchaseDAO(): PurchaseDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun itemListDAO(): ItemListDAO

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