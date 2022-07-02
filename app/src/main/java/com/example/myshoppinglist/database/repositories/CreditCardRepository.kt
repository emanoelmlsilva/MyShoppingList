package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.entities.CreditCard
import kotlinx.coroutines.*

class CreditCardRepository(private val cardCreditCardDAO: CreditCardDAO){

    val searchResult = MutableLiveData<CreditCard>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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

    fun getAll(nameUser: String): List<CreditCard>{
        return cardCreditCardDAO.getAll(nameUser)
    }

    private fun asyncFind(name: String, id: Long): Deferred<CreditCard?> = coroutineScope.async(Dispatchers.IO){
        return@async cardCreditCardDAO.findCreditCardById(name, id)
    }
}