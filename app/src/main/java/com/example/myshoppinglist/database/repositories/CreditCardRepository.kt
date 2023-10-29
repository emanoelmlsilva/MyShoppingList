package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.LiveData
import com.example.myshoppinglist.database.daos.CreditCardDAO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.utils.FormatDateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreditCardRepository(private val cardCreditCardDAO: CreditCardDAO){

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertAllCreditCards(newCreditCards: List<CreditCard>){
        coroutineScope.launch(Dispatchers.IO) {
            cardCreditCardDAO.insertAllCreditCards(newCreditCards)
        }
    }

    fun insertCreditCard(newCreditCard: CreditCard){
        coroutineScope.launch(Dispatchers.IO) {
            cardCreditCardDAO.insertCreditCard(newCreditCard)
        }
    }

    fun updateCreditCard(newCreditCard: CreditCard){
        coroutineScope.launch(Dispatchers.IO) {
            cardCreditCardDAO.updateCreditCard(newCreditCard)
        }
    }

    fun findCardCreditById(emailUser: String, id: Long): LiveData<CreditCard>{
        return cardCreditCardDAO.findCreditCardById(emailUser, id)
    }

    fun getAll(emailUser: String): LiveData<List<CreditCard>>{
        return cardCreditCardDAO.getAll(emailUser)
    }

    fun getAllWithSum(emailUser: String): LiveData<List<CreditCard>>{
        val dateMonth = FormatDateUtils().getMonthAndYear()

        return cardCreditCardDAO.getAllWithSum(emailUser, dateMonth)
    }

    fun getAutoIncrement(): Int{
        return cardCreditCardDAO.getAutoIncrement()
    }

}