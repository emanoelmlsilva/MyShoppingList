package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory

interface CallbackItemList : Callback{

    fun onUpdateListAndCategory(list: List<ItemListAndCategory>){}

    fun itemList(itemList: ItemList){}

    fun onDelete(){}

    fun onUpdate(item: ItemListAndCategory){}

    fun onFinish(){}
}