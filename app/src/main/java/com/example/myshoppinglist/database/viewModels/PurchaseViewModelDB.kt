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
import com.example.myshoppinglist.utils.SintaxQueryUtils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import kotlinx.coroutines.flow.Flow

class PurchaseViewModelDB(context: Context) : ViewModel() {

    private val TAG = "PurchaseViewModelDB"
    private val repository: PurchaseRepository

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val purchaseDAO = myShopListDataBase.purchaseDAO()
        repository = PurchaseRepository(purchaseDAO)
    }

    fun getPurchaseAllWithRepeatByIdCard(idCard: Long): LiveData<List<Purchase>>{
        return repository.getPurchaseAllWithRepeatByIdCard(idCard)
    }

    fun getPurchasesOfSearch(
        arguments: String
    ): Flow<List<PurchaseAndCategory>> {

        var existSyntaxAnd = arguments.trimStart().startsWith(SintaxQueryUtils.AND.name, true)

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery("SELECT * FROM purchases, category, credit_cards WHERE category.myShoppingIdCategory = categoryOwnerId ${if (existSyntaxAnd) "" else SintaxQueryUtils.AND} $arguments")

        return repository.getPurchasesOfSearch(query)
    }

    fun getPurchasesSumOfSearch(arguments: String): Flow<Double?> {

        var initWithSyntaxAnd = arguments.trimStart().split(" ")[0] == SintaxQueryUtils.AND.name
        var newArguments: String? = null
        if (initWithSyntaxAnd){
            newArguments = " " + arguments.substring(SintaxQueryUtils.AND.name.length + 1)
        }

        val query: SimpleSQLiteQuery = SimpleSQLiteQuery("SELECT COALESCE(SUM(CASE 'QUANTITY' WHEN type_product THEN CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END * CAST(amount_or_kilo AS NUMBER) ELSE CASE 0 WHEN discount THEN CAST(price AS NUMBER) ELSE CAST(price AS NUMBER) - CAST(DISCOUNT as NUMBER) END END), 0.0) as value FROM purchases, credit_cards WHERE ${newArguments ?: arguments}")

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

    fun updatePurchase(purchase: Purchase) {
        updatePurchase(purchase, object : Callback {
            override fun onCancel() {
                super.onCancel()
            }

            override fun onSuccess() {
                super.onSuccess()
            }

            override fun onFailed(messageError: String) {
                super.onFailed(messageError)
            }
        } )
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

    fun getPurchasesAndCategoryWeekByIdCardCredit(idCard: Long): LiveData<List<PurchaseAndCategory>> {
        return repository.getPurchasesAndCategoryWeekByIdCardCredit(idCard)
    }

}