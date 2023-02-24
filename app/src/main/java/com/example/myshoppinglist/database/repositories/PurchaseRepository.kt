package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.utils.FormatUtils
import kotlinx.coroutines.*
import java.math.RoundingMode
import java.util.*

class PurchaseRepository(private val purchaseDAO: PurchaseDAO) {

    val searchResult = MutableLiveData<Purchase>()
    val searchCollectionPurchaseAndCategory = MutableLiveData<List<PurchaseAndCategory>>()
    val searchCollecitonPurchase = MutableLiveData<List<Purchase>>()
    val searchMonthsCollection = MutableLiveData<List<String>>()
    val searchPrice = MutableLiveData<Double>()
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertPurhcase(purchaseCollection: List<Purchase>){
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.inserPurchase(purchaseCollection)
        }
    }

    fun getPurchaseAll(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
//            searchCollecitonResult.value = asyncFindAll(emailUser).await()
        }
    }

    fun getPurchaseAll(emailUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
//            searchCollecitonResult.value = asyncFindAllByUserAndIdCard(emailUser, idCard).await()
        }
    }

    fun getPurchaseByMonth(emailUser: String, date: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionPurchaseAndCategory.value = asyncFindAllByMonth(date, emailUser, idCard)
        }
    }

    fun getPurchaseAllByIdCard(emailUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
//            searchCollecitonResult.value = asyncFindAllByIdCard(emailUser, idCard).await()
        }
    }

    fun getMonthByIdCard(emailUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchMonthsCollection. value = asyncFindAllMonthByIdCard(emailUser, idCard).await()
        }
    }

    fun getMonthDistinctByIdCard(emailUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchMonthsCollection. value = asyncFindAllMonthDistinctByIdCard(emailUser, idCard).await()
        }
    }

    fun sumPriceAllCard(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
            val value = asyncSumPriceAllCard(emailUser).await()
            searchPrice.value = value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
    }

    fun sumPriceById(emailUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            val value = asyncSumPriceById(idCard, emailUser).await()
            searchPrice.value = value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
    }

    fun sumPriceByMonth(emailUser: String, idCard: Long, date: String){
        coroutineScope.launch(Dispatchers.Main){
            val value = asyncSumPriceByMonth(idCard, emailUser, date).await()
            searchPrice.value = value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
    }

    fun getPurchasesAndCategoryWeek(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val limitWeek = FormatUtils().getDateString(calendar.time)
            searchCollectionPurchaseAndCategory.value = asyncGetPurchsesAndCategoryWeek(emailUser, limitWeek).await()
        }
    }

    fun getPurchasesWeek(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val limitWeek = FormatUtils().getDateString(calendar.time)
            searchCollecitonPurchase.value = asyncGetPurchsesWeek(emailUser, limitWeek).await()
        }
    }

    fun getPurchasesOfSearch(query: SupportSQLiteQuery){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionPurchaseAndCategory.value = asyncGetPurchasesSearch(query)
        }

    }

    fun getPurchasesSearchSum(query: SupportSQLiteQuery){
        coroutineScope.launch(Dispatchers.Main){
            searchPrice.value = asyncGetPurchasesSearchSum(query).await()
        }
    }

    private suspend fun asyncGetPurchasesSearch(query: SupportSQLiteQuery): List<PurchaseAndCategory> =
        coroutineScope.async(Dispatchers.IO){
            return@async purchaseDAO.getPurchasesSearch(query)
        }.await()

    private fun asyncGetPurchasesSearchSum(query: SupportSQLiteQuery): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchasesSearchSum(query)
    }

    private fun asyncFindAll(emailUser: String): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAll(emailUser)
    }

    private fun asyncFindAllByUserAndIdCard(emailUser: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAllByUserAndIdCard(emailUser, idCard)
    }

    private suspend fun asyncFindAllByMonth(date: String, emailUser: String, idCard: Long): List<PurchaseAndCategory> =
        coroutineScope.async(Dispatchers.IO){
            return@async purchaseDAO.getPurchaseByMonth(date, emailUser, idCard)
        }.await()

    private fun asyncFindAllByIdCard(emailUser: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAllByIdCard(emailUser, idCard)
    }

    private fun asyncFindAllMonthByIdCard(emailUser: String, idCard: Long): Deferred<List<String>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getMonthByIdCard(emailUser, idCard)
    }

    private fun asyncFindAllMonthDistinctByIdCard(emailUser: String, idCard: Long): Deferred<List<String>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getMonthDistinctByIdCard(emailUser, idCard)
    }

    private fun asyncSumPriceById(idCard: Long, emailUser: String): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.sumPriceById(emailUser, idCard)
    }

    private fun asyncSumPriceAllCard(emailUser: String): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.sumPriceAllCard(emailUser)
    }

    private fun asyncSumPriceByMonth(idCard: Long, emailUser: String, date: String): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.sumPriceByMonth(emailUser, idCard, date = date)
    }

    private fun asyncGetPurchsesAndCategoryWeek(emailUser: String, week: String): Deferred<List<PurchaseAndCategory>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchasesAndCategoryWeek(week, emailUser)
    }

    private fun asyncGetPurchsesWeek(emailUser: String, week: String): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchasesWeek(week, emailUser)
    }
}