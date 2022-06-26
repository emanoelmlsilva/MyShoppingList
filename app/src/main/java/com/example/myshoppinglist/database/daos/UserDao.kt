package com.example.myshoppinglist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myshoppinglist.database.entities.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name")
    fun findUserByName(name: String): User

    @Query("SELECT * FROM users WHERE name = :name")
    fun deleteUser(name: String): Boolean

    @Query("SELECT COUNT(users.name), * FROM users")
    fun hasContainUser(): Int
}