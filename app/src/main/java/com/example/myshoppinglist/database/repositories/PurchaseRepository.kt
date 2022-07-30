package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.utils.FormatUtils
import kotlinx.coroutines.*
import java.util.*

class PurchaseRepository(private val purchaseDAO: PurchaseDAO) {

    val searchResult = MutableLiveData<Purchase>()
    val searchCollecitonResult = MutableLiveData<List<Purchase>>()
    val searchMonthsCollection = MutableLiveData<List<String>>()
    val searchPrice = MutableLiveData<Double>()
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertPurhcase(purchaseCollection: List<Purchase>){
        coroutineScope.launch(Dispatchers.IO) {
            purchaseDAO.inserPurchase(purchaseCollection)
        }
    }

    fun getPurchaseAll(){
        coroutineScope.launch(Dispatchers.Main){
            searchCollecitonResult.value = asyncFindAll().await()
        }
    }

    fun getPurchaseAll(nameUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchCollecitonResult.value = asyncFindAllByUserAndIdCard(nameUser, idCard).await()
        }
    }

    fun getPurchaseByMonth(nameUser: String, date: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchCollecitonResult.value = asyncFindAllByMonth(date, nameUser, idCard).await()
        }
    }

    fun getPurchaseAllByIdCard(nameUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchCollecitonResult.value = asyncFindAllByIdCard(nameUser, idCard).await()
        }
    }

    fun getMonthByIdCard(nameUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchMonthsCollection. value = asyncFindAllMonthByIdCard(nameUser, idCard).await()
        }
    }

    fun sumPriceById(nameUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchPrice.value = asyncSumPriceById(idCard, nameUser).await()
        }
    }

    fun sumPriceByMonth(nameUser: String, idCard: Long, date: String){
        coroutineScope.launch(Dispatchers.Main){
            searchPrice.value = asyncSumPriceByMonth(idCard, nameUser, date).await()
        }
    }

    fun getPurchasesWeek(nameUser: String, idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val initWeek = FormatUtils().getDateString(calendar.time)
            searchCollecitonResult.value = asyncGetPurchsesWeek(idCard, nameUser, initWeek).await()
        }
    }

    private fun asyncFindAll(): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAll()
    }

    private fun asyncFindAllByUserAndIdCard(name: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAllByUserAndIdCard(name, idCard)
    }

    private fun asyncFindAllByMonth(date: String, nameUser: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseByMonth(date, nameUser, idCard)
    }

    private fun asyncFindAllByIdCard(nameUser: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAllByIdCard(nameUser, idCard)
    }

    private fun asyncFindAllMonthByIdCard(nameUser: String, idCard: Long): Deferred<List<String>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getMonthByIdCard(nameUser, idCard)
    }

    private fun asyncSumPriceById(idCard: Long, nameUser: String): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.sumPriceById(nameUser, idCard)
    }

    private fun asyncSumPriceByMonth(idCard: Long, nameUser: String, date: String): Deferred<Double> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.sumPriceByMonth(nameUser, idCard, date = date)
    }

    private fun asyncGetPurchsesWeek(idCard: Long, nameUser: String, from: String): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchasesWeek(nameUser, idCard)
    }
}