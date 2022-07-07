package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.entities.relations.UserWithCreditCards
import com.example.myshoppinglist.database.repositories.CreditCardRepository

class CreditCardViewModel(context: Context): ViewModel() {

    private val repository: CreditCardRepository
    val searchResult: MutableLiveData<CreditCard>
    val searchCollectionResult: MutableLiveData<List<CreditCard>>
    private var userViewModel : UserViewModel

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val creditCardDao = myShopListDataBase.creditCardDao()
        userViewModel = UserViewModel(context)
        userViewModel.getUserCurrent()
        repository = CreditCardRepository(creditCardDao)
        searchResult = repository.searchResult
        searchCollectionResult = repository.searchCollectionResult
    }

    fun insertCreditCard(creditCard: CreditCard){
        repository.insertCreditCard(creditCard)
    }

    fun updateCreditCard(creditCard: CreditCard){
        repository.updateCreditCard(creditCard)
    }

    fun findCreditCardById(id: Long){
        userViewModel.searchResult.observeForever { repository.findCardCreditById(it.name, id) }
    }

    fun getAll() {
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getAll(nameUser)
        }
    }
}