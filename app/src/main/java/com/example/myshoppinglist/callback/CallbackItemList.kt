package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCateogry

interface CallbackItemList : Callback{

    fun itemList(itemList: ItemList){}

    fun onDelete(){}

    fun onUpdate(item: ItemListAndCateogry){}

    fun onFinish(){}
}