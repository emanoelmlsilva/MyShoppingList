package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.utils.FormatDateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class PurchaseRepository(private val purchaseDAO: PurchaseDAO) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val email = UserLoggedShared.getEmailUserCurrent()

    fun insertPurchases(purchaseCollection: List<Purchase>) {
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.insetPurchase(purchaseCollection)
        }
    }

    fun insertPurchase(purchase: Purchase){
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.insetPurchase(purchase)
        }
    }

    fun updatePurchase(purchase: Purchase) {
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.updatePurchase(purchase)
        }
    }

    fun deletePurchaseByIdApi(myShoppingIdApi: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.deletePurchaseByIdApi(myShoppingIdApi, email)
        }
    }

    fun deletePurchaseById(myShoppingId: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.deletePurchaseById(myShoppingId, email)
        }
    }

    fun getPurchaseAll(): List<Purchase> {
        return purchaseDAO.getPurchaseAll(email)
    }

    fun getPurchaseAll(idCard: Long): List<Purchase> {
        return purchaseDAO.getPurchaseAllByUserAndIdCard(email, idCard)
    }

    fun getPurchaseByMonth(
        date: String,
        idCard: Long
    ): LiveData<List<PurchaseAndCategory>> {
        return purchaseDAO.getPurchaseByMonth(date, email, idCard)
    }

    fun getPurchaseAllByIdCard(idCard: Long): LiveData<List<Purchase>> {
        return purchaseDAO.getPurchaseAllByIdCard(email, idCard)
    }

    fun getMonthByIdCard(idCard: Long): LiveData<List<String>> {
        return purchaseDAO.getMonthByIdCard(email, idCard)
    }

    fun getMonthDistinctByIdCard(idCard: Long): List<String> {
        return purchaseDAO.getMonthDistinctByIdCard(email, idCard)
    }

    fun sumPriceAllCard(): Double {
        return purchaseDAO.sumPriceAllCard(email)
    }

    fun sumPriceById(idCard: Long): Double {
        return purchaseDAO.sumPriceById(email, idCard)
    }

    fun sumPriceByMonth(idCard: Long, date: String): Double {
        return purchaseDAO.sumPriceByMonth(email, idCard, date = date)
    }

    fun getPurchasesAndCategoryWeek(): LiveData<List<PurchaseAndCategory>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val limitWeek = FormatDateUtils().getDateString(calendar.time)
        return purchaseDAO.getPurchasesAndCategoryWeek(limitWeek, email)
    }

    fun getPurchasesWeek(): List<Purchase> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val limitWeek = FormatDateUtils().getDateString(calendar.time)
        return purchaseDAO.getPurchasesWeek(limitWeek, email)

    }

    fun getPurchasesOfSearch(query: SupportSQLiteQuery): Flow<List<PurchaseAndCategory>> {
        return purchaseDAO.getPurchasesSearch(query)
    }

    fun getPurchasesSearchSum(query: SupportSQLiteQuery): Flow<Double> {
        return purchaseDAO.getPurchasesSearchSum(query)
    }
}