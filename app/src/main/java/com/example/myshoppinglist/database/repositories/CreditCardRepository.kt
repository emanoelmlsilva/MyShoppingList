package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.TypeProduct
import kotlinx.coroutines.*

class CreditCardRepository(private val cardCreditCardDAO: CreditCardDAO){

    val searchResult = MutableLiveData<CreditCard>()
    val searchCollectionResult = MutableLiveData<List<CreditCard>>()
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCreditCard(newCreditCard: CreditCard){
        coroutineScope.launch(Dispatchers.IO) {
            cardCreditCardDAO.inserCreditCard(newCreditCard)
        }
    }

    fun updateCreditCard(newCreditCard: CreditCard){
        coroutineScope.launch(Dispatchers.IO) {
            cardCreditCardDAO.updateCreditCard(newCreditCard)
        }
    }

    fun findCardCreditById(nameUser: String, id: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchResult.value = asyncFind(nameUser, id).await()
        }
    }

    fun getAll(nameUser: String){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionResult.value = asynFindAll(nameUser).await()
        }
    }

    fun getAllWithSum(nameUser: String){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionResult.value = ansyFindAllWithSum(nameUser).await()
        }
    }

    private fun asyncFind(name: String, id: Long): Deferred<CreditCard?> = coroutineScope.async(Dispatchers.IO){
        return@async cardCreditCardDAO.findCreditCardById(name, id)
    }

    private fun asynFindAll(name: String): Deferred<List<CreditCard>> = coroutineScope.async(Dispatchers.IO) {
        return@async cardCreditCardDAO.getAll(name)
    }

    private fun ansyFindAllWithSum(name: String): Deferred<List<CreditCard>> = coroutineScope.async(Dispatchers.IO) {
        return@async cardCreditCardDAO.getAllWithSum(name)
    }
}