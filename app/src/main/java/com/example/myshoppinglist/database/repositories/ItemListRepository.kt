package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.LiveData
import com.example.myshoppinglist.database.daos.ItemListDAO
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemListRepository(private val itemListDAO: ItemListDAO) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertItemListAll(itemListCollection: List<ItemList>){
        coroutineScope.launch(Dispatchers.IO) {
            itemListDAO.insertItemAll(itemListCollection)
        }
    }

    fun insertItemList(itemList: ItemList){
        coroutineScope.launch(Dispatchers.IO) {
            itemListDAO.insertItem(itemList)
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

    fun getAll(idCard: Long): LiveData<List<ItemListAndCategory>>{
        return itemListDAO.getAll(idCard)
    }

}