package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.Category

@Dao
interface CategoryDAO {

    @Insert
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>
}