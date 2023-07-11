package com.example.myshoppinglist.database.daos

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteConstraintException::class)
    suspend fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun findUserByEmail(email: String): LiveData<UserDTO>

    @Query("DELETE FROM users")
    fun deleteUser(): Int
}