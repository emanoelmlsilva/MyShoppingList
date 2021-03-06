package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.repositories.PurchaseRepository

class PurchaseViewModel(context: Context): ViewModel() {

    private val repository: PurchaseRepository
    val searchResult: MutableLiveData<Purchase>
    val searchCollectionResults: MutableLiveData<List<Purchase>>
    private var userViewModel : UserViewModel

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val purchaseDAO = myShopListDataBase.purchaseDAO()
        userViewModel = UserViewModel(context)
        userViewModel.getUserCurrent()
        repository = PurchaseRepository(purchaseDAO)
        searchResult = repository.searchResult
        searchCollectionResults = repository.searchCollecitonResult
    }

    fun insertPurchase(purchaseCollection: List<Purchase>){
        repository.insertPurhcase(purchaseCollection)
    }

    fun getPurchaseAll() {
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseAll()
        }
    }

    fun getPurchaseAll(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseAll(nameUser, idCard)
        }
    }
}