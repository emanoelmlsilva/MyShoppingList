package com.example.myshoppinglist.database.daos

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.room.*
import com.example.myshoppinglist.database.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun findUserByEmail(email: String): User

    @Query("DELETE FROM users")
    fun deleteUser(): Int
}