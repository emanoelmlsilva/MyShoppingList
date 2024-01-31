package com.example.myshoppinglist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.TypeProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCreditCards(players: List<CreditCard>)

    @Insert
    fun insertCreditCard(creditCard: CreditCard)

    @Update
    fun updateCreditCard(creditCard: CreditCard)

    @Query("SELECT * FROM credit_cards WHERE myShoppingId = :idCard AND cardUserId = :emailUser")
    fun findCreditCardById(emailUser: String, idCard: Long): LiveData<CreditCard>

    @Query("SELECT * FROM credit_cards, users WHERE cardUserId = :emailUser AND users.email = :emailUser ORDER BY position ASC")
    fun getAll(emailUser: String): LiveData<List<CreditCard>>

    @Query("SELECT *,credit_cards.myShoppingId as myShoppingId , COALESCE(SUM(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM credit_cards,users ON users.email = :emailUser AND credit_cards.cardUserId = users.email LEFT JOIN purchases ON credit_cards.myShoppingId = purchases.purchaseCardId AND purchases.date LIKE '%' || :date || '%' GROUP BY credit_cards.myShoppingId ORDER BY position ASC")
    fun getAllWithSum(emailUser: String, date: String, typeProduct: TypeProduct = TypeProduct.QUANTITY): LiveData<List<CreditCard>>

    @Query("SELECT MAX(position)+1 from credit_cards")
    fun getAutoIncrement(): Int
}