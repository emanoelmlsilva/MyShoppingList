package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.Purchase

@Dao
interface PurchaseDAO {

    @Insert
    fun inserPurchase(purchase: List<Purchase>)

    @Update
    fun updatePurchase(purchase: Purchase)

    @Query("SELECT * FROM purchases, credit_cards  WHERE purchaseCardId = :idCard AND cardUserId = :nameUser ORDER BY date DESC")
    fun getPurchaseAllByUserAndIdCard(nameUser: String, idCard: Long): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards ORDER BY date DESC")
    fun getPurchaseAll(): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId ORDER BY date DESC")
    fun getAllByIdCard(nameUser: String, idCard: String): List<Purchase>
}