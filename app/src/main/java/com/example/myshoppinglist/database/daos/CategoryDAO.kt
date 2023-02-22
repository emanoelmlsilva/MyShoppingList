package com.example.myshoppinglist.database.daos

import androidx.room.*
import com.example.myshoppinglist.database.entities.Category

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT )
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category, users WHERE category = :category AND users.email = :emailUser AND categoryUserId = users.email")
    fun getByCategory(category: String, emailUser: String): Category

    @Query("SELECT * FROM category, users WHERE id = :idCategory AND users.email = :emailUser AND categoryUserId = users.email")
    fun getById(idCategory: Long, emailUser: String): Category

    @Query("SELECT * FROM category, users WHERE users.email = :emailUser AND categoryUserId = users.email")
    fun getAll(emailUser: String): List<Category>
}