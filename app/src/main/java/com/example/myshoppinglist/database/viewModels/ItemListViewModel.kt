package com.example.myshoppinglist.database.viewModels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.repositories.ItemListRepository
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared

class ItemListViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val repository: ItemListRepository
    val searchItemListResult: MutableLiveData<List<ItemListAndCategory>>
    private var userViewModel: UserViewModel
    private var mLifecycleOwner: LifecycleOwner

    init{
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val itemListDAO = myShopListDataBase.itemListDAO()
        userViewModel = UserViewModel(context)
        val email = UserLoggedShared.getEmailUserCurrent()
        userViewModel.findUserByName(email)
        repository = ItemListRepository(itemListDAO)
        searchItemListResult = repository.searchItemListResult
        mLifecycleOwner = lifecycleOwner
    }

    fun insertItemList(itemList: ItemList){
        repository.insertItemList(itemList)
    }

    fun updateItemList(itemList: ItemList){
        repository.updateItemList(itemList)
    }

    fun deleteItemList(itemList: ItemList){
        repository.deleteItemList(itemList)
    }

    fun getAll(idCard: Long){
        var nameUser = ""
        userViewModel.searchResult.observe(mLifecycleOwner){
            nameUser = it.name
            repository.getAll(idCard)
        }
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }
}