package com.example.myshoppinglist.database.viewModels

 import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.Purchase
 import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
 import com.example.myshoppinglist.database.repositories.PurchaseRepository
 import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
 import com.example.myshoppinglist.utils.FormatUtils
import java.util.*

class PurchaseViewModel(context: Context): ViewModel() {

    private val repository: PurchaseRepository
    val searchResult: MutableLiveData<Purchase>
    val searchPurchaseAndCategory: MutableLiveData<List<PurchaseAndCategory>>
    val searchPurchases: MutableLiveData<List<Purchase>>
    val searchResultMonths: MutableLiveData<List<String>>
    val searchSumPriceResult: MutableLiveData<Double>
    @SuppressLint("StaticFieldLeak")
    var contextTest: Context

    private var userViewModel : UserViewModel

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val purchaseDAO = myShopListDataBase.purchaseDAO()
        userViewModel = UserViewModel(context)
        val email = UserLoggedShared.getEmailUserCurrent()
        userViewModel.findUserByName(email)
        repository = PurchaseRepository(purchaseDAO)
        searchResult = repository.searchResult
        searchPurchaseAndCategory = repository.searchCollecitonPurchaseAndCategory
        searchPurchases = repository.searchCollecitonPurchase
        searchResultMonths = repository.searchMonthsCollection
        searchSumPriceResult = repository.searchPrice
        contextTest = context
    }

    fun getPurchasesOfSearch(arguments: MutableList<Any>, condition: String){
        val email = UserLoggedShared.getEmailUserCurrent()

        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(FormatUtils().getNameMonth((Date().month + 1).toString()))

        val query : SimpleSQLiteQuery = if(arguments.size == 0 || condition.isBlank()){
            SimpleSQLiteQuery("SELECT * FROM purchases, category WHERE category.id = categoryOwnerId AND date LIKE '%' || ? || '%' AND purchaseUserId = ?", arrayOf(monthAndYearNumber, email))
        }else{
            SimpleSQLiteQuery("SELECT * FROM purchases, category WHERE category.id = categoryOwnerId AND $condition AND purchaseUserId = ?", arguments.toTypedArray())
        }

        repository.getPurchasesOfSearch(query)
    }

    fun getPurchasesSumOfSearch(arguments: MutableList<Any>, condition: String){

        val email = UserLoggedShared.getEmailUserCurrent()

        arguments.add(0, "QUANTITY")

        val monthAndYearNumber = FormatUtils().getMonthAndYearNumber(FormatUtils().getNameMonth((Date().month + 1).toString()))

        if(arguments.size == 0 || condition.isBlank()){
            arguments.add(monthAndYearNumber)
        }

        arguments.add(email)

        val query : SimpleSQLiteQuery = if(arguments.size == 0 || condition.isBlank()){
            SimpleSQLiteQuery("SELECT COALESCE(SUM(CAST(price AS NUMBER) * CASE ? WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM purchases WHERE date LIKE '%' || ? || '%' AND purchaseUserId = ?", arguments.toTypedArray())
        }else{
            SimpleSQLiteQuery("SELECT COALESCE(SUM(CAST(price AS NUMBER) * CASE ? WHEN typeProduct THEN CAST(quantiOrKilo AS NUMBER) ELSE 1 END), 0) as value FROM purchases WHERE $condition AND purchaseUserId = ?", arguments.toTypedArray())
        }
        repository.getPurchasesSearchSum(query)
    }

    fun insertPurchase(purchaseCollection: List<Purchase>){
        repository.insertPurhcase(purchaseCollection)
    }

    fun getPurchaseAll() {
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchaseAll(emailUser)
        }
    }

    fun getPurchaseAll(idCard: Long){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchaseAll(emailUser, idCard)
        }
    }

    fun getPurchaseByMonth(idCard: Long, date: String){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchaseByMonth(emailUser, date, idCard)
        }
    }

    fun getPurchaseAllByIdCard(idCard: Long){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchaseAllByIdCard(emailUser, idCard)
        }
    }

    fun getMonthByIdCard(idCard: Long){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getMonthByIdCard(emailUser, idCard)
        }
    }

    fun getMonthDistinctByIdCard(idCard: Long){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getMonthDistinctByIdCard(emailUser, idCard)
        }
    }

    fun sumPriceById(idCard: Long){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.sumPriceById(emailUser, idCard)
        }
    }

    fun sumPriceAllCard(){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.sumPriceAllCard(emailUser)
        }
    }

    fun sumPriceByMonth(idCard: Long, date: String){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.sumPriceByMonth(emailUser, idCard, date)
        }
    }

    fun getPurchasesWeek(){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchasesWeek(emailUser)
        }
    }

    fun getPurchasesAndCategoryWeek(){
        var emailUser = ""
        userViewModel.searchResult.observeForever {
            emailUser = it.email
            repository.getPurchasesAndCategoryWeek(emailUser)
        }
    }
}