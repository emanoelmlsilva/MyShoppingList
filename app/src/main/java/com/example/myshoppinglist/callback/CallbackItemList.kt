package com.example.myshoppinglist.callback

import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.services.dtos.ItemListDTO

interface CallbackItemList : Callback{

    fun onUpdateListAndCategory(list: List<ItemListAndCategory>){}

    fun onUpdate(itemList: ItemListDTO)

    fun onInsert(itemList: ItemListDTO){}

    fun onDelete(){}

    fun onUpdate(item: ItemListAndCategory){}

    fun onFinish(){}
}