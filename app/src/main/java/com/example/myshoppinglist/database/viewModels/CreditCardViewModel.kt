package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.*
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.entities.relations.UserWithCreditCards
import com.example.myshoppinglist.database.repositories.CreditCardRepository
import com.example.myshoppinglist.enums.TypeProduct

class CreditCardViewModel(context: Context, lifecycleOwner: LifecycleOwner): ViewModel() {

    private val repository: CreditCardRepository
    val searchResult: MutableLiveData<CreditCard>
    val searchCollectionResult: MutableLiveData<List<CreditCard>>
    private var userViewModel : UserViewModel
    private var mLifecycleOwner: LifecycleOwner

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val creditCardDao = myShopListDataBase.creditCardDao()
        userViewModel = UserViewModel(context)
        userViewModel.getUserCurrent()
        repository = CreditCardRepository(creditCardDao)
        searchResult = repository.searchResult
        searchCollectionResult = repository.searchCollectionResult
        mLifecycleOwner = lifecycleOwner
    }

    fun insertCreditCard(creditCard: CreditCard){
        repository.insertCreditCard(creditCard)
    }

    fun updateCreditCard(creditCard: CreditCard){
        repository.updateCreditCard(creditCard)
    }

    fun findCreditCardById(id: Long){
        userViewModel.searchResult.observe(mLifecycleOwner) { repository.findCardCreditById(it.name, id) }
    }

    fun getAll() {
        var nameUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner) {
            nameUser = it.name
            repository.getAll(nameUser)
        }
    }

    fun getAllWithSum(){
        var nameUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            nameUser = it.name
            repository.getAllWithSum(nameUser)
        }
    }
}