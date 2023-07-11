package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.repositories.CreditCardRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared

class CreditCardViewModelDB(context: Context, lifecycleOwner: LifecycleOwner): ViewModel() {

    private val repository: CreditCardRepository
    private var mLifecycleOwner: LifecycleOwner
    private var email: String

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val creditCardDao = myShopListDataBase.creditCardDao()

        email = UserLoggedShared.getEmailUserCurrent()

        repository = CreditCardRepository(creditCardDao)
        mLifecycleOwner = lifecycleOwner
    }

    fun insertAllCreditCards(creditCards: List<CreditCard>){
        repository.insertAllCreditCards(creditCards)
    }

    fun insertCreditCard(creditCard: CreditCard){
        repository.insertCreditCard(creditCard)
    }

    fun updateCreditCard(creditCard: CreditCard){
        repository.updateCreditCard(creditCard)
    }

    fun findCreditCardById(id: Long): LiveData<CreditCard>{
        return repository.findCardCreditById(email, id)
    }

    fun getAll(): LiveData<List<CreditCard>> {
        return repository.getAll(email)
    }

    fun getAllWithSum(): LiveData<List<CreditCard>>{
        return repository.getAllWithSum(email)
    }
}