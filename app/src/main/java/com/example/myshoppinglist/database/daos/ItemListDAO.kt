package com.example.myshoppinglist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory

@Dao
interface ItemListDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItemAll(itemListCollection: List<ItemList>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertItem(itemList: ItemList)

    @Update
    fun updateItemList(itemList: ItemList)

    @Delete
    fun deleteItemList(itemList: ItemList)

    @Query("SELECT * FROM itemLists, category WHERE category.myShoppingIdCategory = itemLists.categoryOwnerIdItem AND itemLists.creditCardOwnerIdItem = :idCard")
    fun getAll(idCard: Long): LiveData<List<ItemListAndCategory>>
}