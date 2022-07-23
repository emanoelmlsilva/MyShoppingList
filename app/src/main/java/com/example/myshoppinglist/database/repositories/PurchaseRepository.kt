package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.PurchaseDAO
import com.example.myshoppinglist.database.entities.Purchase
import kotlinx.coroutines.*

class PurchaseRepository(private val purchaseDAO: PurchaseDAO) {

    val searchResult = MutableLiveData<Purchase>()
    val searchCollecitonResult = MutableLiveData<List<Purchase>>()
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

    private fun asyncFindAll(): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAll()
    }

    private fun asyncFindAllByUserAndIdCard(name: String, idCard: Long): Deferred<List<Purchase>> = coroutineScope.async(Dispatchers.IO){
        return@async purchaseDAO.getPurchaseAllByUserAndIdCard(name, idCard)
    }
}