package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCateogry

@Dao
interface ItemListDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insetItem(itemList: ItemList)

    @Update
    fun updateItemList(itemList: ItemList)

    @Query("SELECT * FROM itemLists, category WHERE category.id = itemLists.categoryOwnerIdItem")
    fun getAll(): List<ItemListAndCateogry>
}