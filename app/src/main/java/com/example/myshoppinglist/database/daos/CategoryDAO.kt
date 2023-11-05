package com.example.myshoppinglist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myshoppinglist.database.entities.Category

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.ABORT )
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category, users WHERE category = :category AND users.email = :emailUser AND categoryUserId = users.email")
    fun getByCategory(category: String, emailUser: String): Category

    @Query("SELECT * FROM category, users WHERE category.myShoppingIdCategory = :idCategory AND users.email = :emailUser AND categoryUserId = users.email")
    fun getById(idCategory: Long, emailUser: String): LiveData<Category>

    @Query("SELECT * FROM category, users WHERE users.email = :emailUser AND categoryUserId = users.email")
    fun getAll(emailUser: String): LiveData<List<Category>>
}