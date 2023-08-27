package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.repositories.PurchaseRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.utils.FormatUtils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import java.util.*

class PurchaseViewModelDB(context: Context) : ViewModel() {

    private val TAG = "PurchaseViewModelDB"
    private val repository: PurchaseRepository

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val purchaseDAO = myShopListDataBase.purchaseDAO()
        repository = PurchaseRepository(purchaseDAO)
    }

    fun getPurchasesOfSearch(
        arguments: MutableList<Any>,
        condition: String
    ): List<PurchaseAndCategory> {
        return getPurchasesOfSearch(arguments, condition, null)
    }

    fun getPurchasesOfSearch(
        arguments: MutableList<Any>,
        condition: String,
        valueGroupBy: String?
    ): List<PurchaseAndCategory> {
        val email = UserLoggedShared.getEmailUserCurrent()

        val query: SimpleSQLiteQuery = if (arguments.size == 0 || condition.isBlank()) {
            val monthAndYearNumber =
                FormatUtils().getMonthAndYearNumber(FormatUtils().getNameMonth((Date().month + 1).toString()))

            SimpleSQLiteQuery(
                "SELECT * FROM purchases, category WHERE category.myShoppingIdCategory = categoryOwnerId AND date LIKE '%' || ? || '%' AND purchaseUserId = ?",
                arrayOf(monthAndYearNumber, email)
            )
        } else {
            val argumentsToJson = arguments.map { it }.toMutableList()
            var conditionGroupBy = ""

            argumentsToJson.add(email)

            if (valueGroupBy != null) {
                conditionGroupBy = valueGroupBy
            }

            SimpleSQLiteQuery(
                "SELECT * FROM purchases, category WHERE category.myShoppingIdCategory = categoryOwnerId AND $condition AND purchaseUserId = ? $conditionGroupBy",
                argumentsToJson.toTypedArray()
            )
        }
        return repository.getPurchasesOfSearch(query)
    }

    fun getPurchasesSumOfSearch(arguments: MutableList<Any>, condition: String): Double {

        val email = UserLoggedShared.getEmailUserCurrent()

        arguments.add(0, "QUANTITY")

        val monthAndYearNumber =
            FormatUtils().getMonthAndYearNumber(FormatUtils().getNameMonth((Date().month + 1).toString()))

        if (arguments.size == 0 || condition.isBlank()) {
            arguments.add(monthAndYearNumber)
        }

        arguments.add(email)

        val query: SimpleSQLiteQuery = if (arguments.size == 0 || condition.isBlank()) {
            SimpleSQLiteQuery(
                "SELECT COALESCE(SUM(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CASE ? WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM purchases WHERE date LIKE '%' || ? || '%' AND purchaseUserId = ?",
                arguments.toTypedArray()
            )
        } else {
            SimpleSQLiteQuery(
                "SELECT COALESCE(SUM(CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CASE ? WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM purchases WHERE $condition AND purchaseUserId = ? ",
                arguments.toTypedArray()
            )
        }

        return repository.getPurchasesSearchSum(query)
    }

    fun insertPurchase(purchase: Purchase, callback: Callback) {
        val action = Action {
            repository.insertPurchase(purchase)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onFailed(throwable.message.toString())
        })
    }

    fun insertPurchases(purchaseCollection: List<Purchase>, callback: Callback){
        val action = Action {
            repository.insertPurchases(purchaseCollection)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onFailed(throwable.message.toString())
        })
    }

    fun updatePurchase(purchase: Purchase, callback: Callback) {

        val action = Action {
            repository.updatePurchase(purchase)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun deletePurchaseByIdApi(myShoppingIdApi: Long, callback: Callback) {
        val action = Action {
            repository.deletePurchaseByIdApi(myShoppingIdApi)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun deletePurchaseByI(myShoppingId: Long, callback: Callback) {
        val action = Action {
            repository.deletePurchaseById(myShoppingId)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun getPurchaseAll(): List<Purchase> {
        return repository.getPurchaseAll()
    }

    fun getPurchaseAll(idCard: Long): List<Purchase> {
        return repository.getPurchaseAll(idCard)
    }

    fun getPurchaseByMonth(idCard: Long, date: String): LiveData<List<PurchaseAndCategory>> {
        return repository.getPurchaseByMonth(date, idCard)
    }

    fun getPurchaseAllByIdCard(idCard: Long): LiveData<List<Purchase>> {
        return repository.getPurchaseAllByIdCard(idCard)
    }

    fun getMonthByIdCard(idCard: Long): LiveData<List<String>> {
        return repository.getMonthByIdCard(idCard)
    }

    fun getMonthDistinctByIdCard(idCard: Long): List<String> {
        return repository.getMonthDistinctByIdCard(idCard)
    }

    fun sumPriceById(idCard: Long): Double {
        return repository.sumPriceById(idCard)
    }

    fun sumPriceAllCard(): Double {
        return repository.sumPriceAllCard()

    }

    fun sumPriceByMonth(idCard: Long, date: String): Double {
        return repository.sumPriceByMonth(idCard, date)
    }

    fun getPurchasesWeek(): List<Purchase> {
        return repository.getPurchasesWeek()
    }

    fun getPurchasesAndCategoryWeek(): LiveData<List<PurchaseAndCategory>> {
        return repository.getPurchasesAndCategoryWeek()
    }
}