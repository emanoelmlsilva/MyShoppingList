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

    @Query("SELECT * FROM credit_cards WHERE idCard = :idCard AND cardUserId = :emailUser")
    fun findCreditCardById(emailUser: String, idCard: Long): CreditCard

    @Query("SELECT * FROM credit_cards, users WHERE cardUserId = :emailUser AND users.email = :emailUser ORDER BY position ASC")
    fun getAll(emailUser: String): List<CreditCard>

    @Query("SELECT *, COALESCE(SUM(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM credit_cards,users ON users.email = :emailUser AND credit_cards.cardUserId = users.email LEFT JOIN purchases ON credit_cards.idCard = purchases.purchaseCardId AND purchases.date LIKE '%' || :date || '%' GROUP BY idCard ORDER BY position ASC")
    fun getAllWithSum(emailUser: String, date: String, typeProduct: TypeProduct = TypeProduct.QUANTITY): List<CreditCard>

}