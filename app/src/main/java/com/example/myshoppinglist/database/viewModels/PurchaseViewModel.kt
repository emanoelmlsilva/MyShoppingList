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
    val searchResultMonths: MutableLiveData<List<String>>
    val searchSumPriceResult: MutableLiveData<Double>

    private var userViewModel : UserViewModel

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val purchaseDAO = myShopListDataBase.purchaseDAO()
        userViewModel = UserViewModel(context)
        userViewModel.getUserCurrent()
        repository = PurchaseRepository(purchaseDAO)
        searchResult = repository.searchResult
        searchCollectionResults = repository.searchCollecitonResult
        searchResultMonths = repository.searchMonthsCollection
        searchSumPriceResult = repository.searchPrice
    }

    fun insertPurchase(purchaseCollection: List<Purchase>){
        repository.insertPurhcase(purchaseCollection)
    }

    fun getPurchaseAll() {
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseAll(nameUser)
        }
    }

    fun getPurchaseAll(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseAll(nameUser, idCard)
        }
    }

    fun getPurchaseByMonth(idCard: Long, date: String){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseByMonth(nameUser, date, idCard)
        }
    }

    fun getPurchaseAllByIdCard(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchaseAllByIdCard(nameUser, idCard)
        }
    }

    fun getMonthByIdCard(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getMonthByIdCard(nameUser, idCard)
        }
    }

    fun sumPriceById(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.sumPriceById(nameUser, idCard)
        }
    }

    fun sumPriceAllCard(){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.sumPriceAllCard(nameUser)
        }
    }

    fun sumPriceByMonth(idCard: Long, date: String){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.sumPriceByMonth(nameUser, idCard, date)
        }
    }

    fun getPurchasesWeek(){
        var nameUser = ""
        userViewModel.searchResult.observeForever {
            nameUser = it.name
            repository.getPurchasesWeek(nameUser)
        }
    }
}