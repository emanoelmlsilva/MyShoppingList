package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.CreditCard

@Dao
interface CreditCardDAO {

    @Insert
    fun inserCreditCard(creditCard: CreditCard)

    @Update
    fun updateCreditCard(creditCard: CreditCard)

    @Query("SELECT * FROM credit_cards WHERE idCard = :idCard AND cardUserId = :nameUser")
    fun findCreditCardById(nameUser: String, idCard: Long): CreditCard

    @Query("SELECT * FROM credit_cards, users WHERE cardUserId = :nameUser")
    fun getAll(nameUser: String): List<CreditCard>
}