package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.TypeProduct

@Dao
interface CreditCardDAO {

    @Insert
    fun inserCreditCard(creditCard: CreditCard)

    @Update
    fun updateCreditCard(creditCard: CreditCard)

    @Query("SELECT * FROM credit_cards WHERE idCard = :idCard AND cardUserId = :nameUser")
    fun findCreditCardById(nameUser: String, idCard: Long): CreditCard

    @Query("SELECT * FROM credit_cards, users WHERE cardUserId = :nameUser ORDER BY position ASC")
    fun getAll(nameUser: String): List<CreditCard>

    @Query("SELECT *, COALESCE(SUM(CAST(price AS NUMBER) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM credit_cards LEFT JOIN purchases ON credit_cards.idCard = purchases.purchaseCardId AND cardUserId = :nameUser AND purchases.date LIKE '%' || :date || '%' GROUP BY idCard ORDER BY position ASC")
    fun getAllWithSum(nameUser: String, date: String, typeProduct: TypeProduct? = TypeProduct.QUANTITY): List<CreditCard>

}