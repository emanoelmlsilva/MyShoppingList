package com.example.myshoppinglist.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory

@Dao
interface ItemListDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insetItem(itemList: ItemList)

    @Update
    fun updateItemList(itemList: ItemList)

    @Delete
    fun deleteItemList(itemList: ItemList)

    @Query("SELECT * FROM itemLists, category WHERE category.id = itemLists.categoryOwnerIdItem AND itemLists.creditCardOwnerIdItem = :idCard")
    fun getAll(idCard: Long): List<ItemListAndCategory>
}