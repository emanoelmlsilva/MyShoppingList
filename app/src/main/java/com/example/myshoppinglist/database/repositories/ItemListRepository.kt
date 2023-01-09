package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.ItemListDAO
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCateogry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemListRepository(private val itemListDAO: ItemListDAO) {

    val searchItemListResult = MutableLiveData<List<ItemListAndCateogry>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertItemList(itemList: ItemList){
        coroutineScope.launch(Dispatchers.IO) {
            itemListDAO.insetItem(itemList)
        }
    }

    fun updateItemList(itemList: ItemList){
        coroutineScope.launch(Dispatchers.IO){
            itemListDAO.updateItemList(itemList)
        }
    }

    fun deleteItemList(itemList: ItemList){
        coroutineScope.launch(Dispatchers.IO) {
            itemListDAO.deleteItemList(itemList)
        }
    }

    fun getAll(idCard: Long){
        coroutineScope.launch(Dispatchers.Main){
            searchItemListResult.value = asyncFindAll(idCard)
        }
    }

    private suspend fun asyncFindAll(idCard: Long): List<ItemListAndCateogry> = coroutineScope.async(Dispatchers.IO) {
        return@async itemListDAO.getAll(idCard)
    }.await()
}