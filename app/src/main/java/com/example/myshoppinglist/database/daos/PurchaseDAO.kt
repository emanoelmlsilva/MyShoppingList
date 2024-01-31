package com.example.myshoppinglist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.utils.FormatDateUtils
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PurchaseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun insetPurchase(purchase: List<Purchase>)

    @Insert(onConflict = OnConflictStrategy.ABORT )
    fun insetPurchase(purchase: Purchase)

    @Update
    fun updatePurchase(purchase: Purchase)

    @Query("DELETE FROM purchases WHERE myShoppingIdPurchase = :myShoppingId AND purchaseUserId = :emailUser")
    fun deletePurchaseById(myShoppingId: Long, emailUser: String)

    @Query("DELETE FROM purchases WHERE idPurchaseApi = :myShoppingIdApi AND purchaseUserId = :emailUser")
    fun deletePurchaseByIdApi(myShoppingIdApi: Long, emailUser: String)

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards  WHERE purchaseCardId = :idCard AND cardUserId = :emailUser ORDER BY date DESC")
    fun getPurchaseAllByUserAndIdCard(emailUser: String, idCard: Long): List<Purchase>

    @Query("SELECT * FROM purchases, credit_cards WHERE purchaseCardId = credit_cards.myShoppingId AND cardUserId = :emailUser ORDER BY date")
    fun getPurchaseAll(emailUser: String): List<Purchase>

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId ORDER BY date DESC")
    fun getPurchaseAllByIdCard(emailUser: String, idCard: Long): LiveData<List<Purchase>>

    @Transaction
    @Query("SELECT * FROM purchases, category WHERE category.myShoppingIdCategory = purchases.categoryOwnerId AND category.categoryUserId = :emailUser AND purchaseCardId = :idCard AND date LIKE '%' || :date || '%' ORDER BY date DESC ")
    fun getPurchaseByMonth(date: String, emailUser: String, idCard: Long): LiveData<List<PurchaseAndCategory>>

    @Query("SELECT date FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId GROUP BY date ORDER BY date DESC ")
    fun getMonthByIdCard(emailUser: String, idCard: Long): LiveData<List<String>>

    @Query("SELECT DISTINCT(SUBSTR(date, 1, LENGTH(date) - 3)) as date FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId GROUP BY date ORDER BY date ASC")
    fun getMonthDistinctByIdCard(emailUser: String, idCard: Long):List<String>

    @Query("SELECT SUM(COALESCE(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END, 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId")
    fun sumPriceById(emailUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT COALESCE(SUM(CASE :typeProduct WHEN typeProduct THEN CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CAST(quantiOrKilo AS NUMBER) ELSE CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END END), 0.0) FROM credit_cards LEFT JOIN purchases ON credit_cards.myShoppingId = purchases.purchaseCardId AND cardUserId = :emailUser AND purchases.date LIKE '%' || :date || '%'")
    fun sumPriceAllCard(emailUser: String, date : String = FormatDateUtils().getMonthAndYear(), typeProduct: TypeProduct = TypeProduct.QUANTITY): Double

    @Query("SELECT SUM(COALESCE(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END , 1) * CASE :typeProduct WHEN typeProduct THEN CAST(quantiOrKilo AS INT) ELSE 1 END) FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId AND date LIKE '%' || :date || '%'")
    fun sumPriceByMonth(emailUser: String, idCard: Long, typeProduct: TypeProduct = TypeProduct.QUANTITY, date: String): Double

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards, category WHERE purchases.categoryOwnerId = category.myShoppingIdCategory AND credit_cards.cardUserId = :emailUser AND credit_cards.myShoppingId = purchases.purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) GROUP BY purchases.myShoppingIdPurchase ORDER BY date DESC")
    fun getPurchasesAndCategoryWeek(week: String, emailUser: String): LiveData<List<PurchaseAndCategory>>

    @Query("SELECT * FROM purchases, credit_cards  WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date, purchases.myShoppingIdPurchase")
    fun getPurchasesWeek(week: String, emailUser: String): List<Purchase>

    @Transaction
    @Query("SELECT * FROM purchases, credit_cards WHERE cardUserId = :emailUser AND credit_cards.myShoppingId = :idCard AND credit_cards.myShoppingId = purchaseCardId AND strftime('%J',date) >= strftime('%J',:week) ORDER BY date DESC")
    fun getPurchasesWeekById(week: String, emailUser: String, idCard: Long): List<Purchase>

    @Transaction
    @RawQuery(observedEntities = [PurchaseAndCategory::class])
    fun getPurchasesSearch(query: SupportSQLiteQuery): Flow<List<PurchaseAndCategory>>

    @RawQuery(observedEntities = [Purchase::class])
    fun getPurchasesSearchSum(query: SupportSQLiteQuery): Flow<Double>
}
