package com.example.myshoppinglist.database.daos

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.utils.FormatUtils
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface PurchaseDAO {

    @Insert
    fun inserPurchase(purchase: List<Purchase>)

    @Update
    fun updatePurchase(purchase: Purchase)

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards  WHERE purchaseCardId = :idCard AND cardUserId = :emailUser ORDER BY date DESC")
    fun getPurchaseAllByUserAndIdCard(emailUser: String, idCard: Long): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE purchaseCardId = idCard AND cardUserId = :emailUser ORDER BY date")
    fun getPurchaseAll(emailUser: String): List<Purchase>

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId ORDER BY date DESC")
    fun getPurchaseAllByIdCard(emailUser: String, idCard: Long): List<Purchase>

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards, category WHERE category.id = categoryOwnerId AND cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId AND date LIKE '%' || :date || '%' ORDER BY date DESC ")
    fun getPurchaseByMonth(date: String, emailUser: String, idCard: Long): List<PurchaseAndCategory>

    @Query("SELECT date FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId GROUP BY date ORDER BY date DESC ")
    fun getMonthByIdCard(emailUser: String, idCard: Long):List<String>

    @Query("SELECT DISTINCT(SUBSTR(date, 1, LENGTH(date) - 3)) as date FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId GROUP BY date ORDER BY date ASC")
    fun getMonthDistinctByIdCard(emailUser: String, idCard: Long):List<String>

    @Query("SELECT SUM(COALESCE(price, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId")
    fun sumPriceById(emailUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT COALESCE(SUM(CASE :typeProduct WHEN typeProduct THEN price * CAST(quantiOrKilo AS NUMBER) ELSE price END), 0.0) FROM credit_cards LEFT JOIN purchases ON credit_cards.idCard = purchases.purchaseCardId AND cardUserId = :emailUser AND purchases.date LIKE '%' || :date || '%'")
    fun sumPriceAllCard(emailUser: String, date : String = FormatUtils().getMonthAndYear(), typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT SUM(COALESCE(price, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId AND date LIKE '%' || :date || '%'")
    fun sumPriceByMonth(emailUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY, date: String): Double

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards, category WHERE categoryOwnerId = category.id AND cardUserId = :emailUser AND idCard = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date, purchases.idPruchase")
    fun getPurchasesAndCategoryWeek(week: String, emailUser: String): List<PurchaseAndCategory>

    @Query("SELECT * FROM purchases, credit_cards  WHERE cardUserId = :emailUser AND idCard = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date, purchases.idPruchase")
    fun getPurchasesWeek(week: String, emailUser: String): List<Purchase>

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :emailUser AND idCard = :idCard AND idCard = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date DESC")
    fun getPurchasesWeekById(week: String, emailUser: String, idCard: Long): List<Purchase>

    @Transaction
    @RawQuery(observedEntities = [PurchaseAndCategory::class])
    fun getPurchasesSearch(query: SupportSQLiteQuery): List<PurchaseAndCategory>

    @RawQuery(observedEntities = [Purchase::class])
    fun getPurchasesSearchSum(query: SupportSQLiteQuery): Double
}
