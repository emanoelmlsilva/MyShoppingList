package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeProduct
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface PurchaseDAO {

    @Insert
    fun inserPurchase(purchase: List<Purchase>)

    @Update
    fun updatePurchase(purchase: Purchase)

    @Query("SELECT * FROM purchases, credit_cards  WHERE purchaseCardId = :idCard AND cardUserId = :nameUser ORDER BY date DESC")
    fun getPurchaseAllByUserAndIdCard(nameUser: String, idCard: Long): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE purchaseCardId = idCard AND cardUserId = :nameUser ORDER BY date")
    fun getPurchaseAll(nameUser: String): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId ORDER BY date DESC")
    fun getPurchaseAllByIdCard(nameUser: String, idCard: Long): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId AND date LIKE '%' || :date || '%' ORDER BY date DESC ")
    fun getPurchaseByMonth(date: String, nameUser: String, idCard: Long): List<Purchase>

    @Query("SELECT date FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId GROUP BY date ORDER BY date DESC ")
    fun getMonthByIdCard(nameUser: String, idCard: Long):List<String>

    @Query("SELECT SUM(COALESCE(price, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId")
    fun sumPriceById(nameUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT SUM(COALESCE(price, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE purchaseCardId = idCard AND cardUserId = :nameUser")
    fun sumPriceAllCard(nameUser: String, typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT SUM(COALESCE(price, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId AND date LIKE '%' || :date || '%'")
    fun sumPriceByMonth(nameUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY, date: String): Double

    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date, purchases.idPruchase")
    fun getPurchasesWeek(week: String, nameUser: String): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :nameUser AND idCard = :idCard AND idCard = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date DESC")
    fun getPurchasesWeekById(week: String, nameUser: String, idCard: Long): List<Purchase>
}
