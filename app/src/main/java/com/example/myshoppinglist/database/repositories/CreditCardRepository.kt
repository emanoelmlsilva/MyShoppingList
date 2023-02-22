package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.utils.FormatUtils
import kotlinx.coroutines.*
import java.util.*

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

    fun findCardCreditById(emailUser: String, id: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchResult.value = asyncFind(emailUser, id).await()
        }
    }

    fun getAll(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionResult.value = asynFindAll(emailUser).await()
        }
    }

    fun getAllWithSum(emailUser: String){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionResult.value = ansyFindAllWithSum(emailUser).await()
        }
    }

    private fun asyncFind(emailUser: String, id: Long): Deferred<CreditCard?> = coroutineScope.async(Dispatchers.IO){
        return@async cardCreditCardDAO.findCreditCardById(emailUser, id)
    }

    private fun asynFindAll(emailUser: String): Deferred<List<CreditCard>> = coroutineScope.async(Dispatchers.IO) {
        return@async cardCreditCardDAO.getAll(emailUser)
    }

    private fun ansyFindAllWithSum(email: String): Deferred<List<CreditCard>> = coroutineScope.async(Dispatchers.IO) {
        val dateMonth = FormatUtils().getMonthAndYear()
        return@async cardCreditCardDAO.getAllWithSum(email, dateMonth)
    }
}