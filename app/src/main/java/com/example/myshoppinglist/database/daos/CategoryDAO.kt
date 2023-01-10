package com.example.myshoppinglist.database.daos

import androidx.room.*
import com.example.myshoppinglist.database.entities.Category

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT )
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category WHERE category = :category")
    fun getByCategory(category: String): Category

    @Query("SELECT * FROM category WHERE id = :idCategory")
    fun getById(idCategory: Long): Category

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>
}