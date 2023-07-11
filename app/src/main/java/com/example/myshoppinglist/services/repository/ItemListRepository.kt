package com.example.myshoppinglist.services.repository

import ResultData
import android.util.Log
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.services.ItemListService
import com.example.myshoppinglist.services.dtos.ItemListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemListRepository(private val itemListService: ItemListService) {

    private val TAG = "ItemListRepository"

    suspend fun update(itemList: ItemListDTO): ResultData<ItemListDTO>{
        return withContext(Dispatchers.IO){
            val itemListExecute = itemListService.update(itemList).execute()

            return@withContext if(itemListExecute.isSuccessful){
                val itemListResponse = itemListExecute.body()?:ItemListDTO()
                ResultData.Success(itemListResponse)
            }else{
                Log.d(TAG, "message error ${itemListExecute.message()}")
                ResultData.Error(Exception("ERROR UPDATE ${itemListExecute.errorBody()}"))
            }
        }
    }

    suspend fun save(itemList: ItemListDTO): ResultData<ItemListDTO>{
        return withContext(Dispatchers.IO){
            val itemListExecute = itemListService.save(itemList).execute()

            return@withContext if(itemListExecute.isSuccessful){
                val itemListResponse = itemListExecute.body()?:ItemListDTO()
                ResultData.Success(itemListResponse)
            }else{
                Log.d(TAG, "message error ${itemListExecute.message()}")
                ResultData.Error(Exception("ERROR SAVE ${itemListExecute.errorBody()}"))
            }
        }
    }

    suspend fun getAll(idCard: Long): ResultData<List<ItemListDTO>>{
        return withContext(Dispatchers.IO){
            val itemListExecute = itemListService.findAllByCardId(idCard).execute()
            return@withContext if (itemListExecute.isSuccessful){
                val itemListResponse = itemListExecute.body()?: listOf()
                ResultData.Success(itemListResponse)
            }else{
                Log.d(TAG, "message error ${itemListExecute.message()}")
                ResultData.Error(Exception("ERROR FIND ALL ${itemListExecute.errorBody()}"))
            }
        }

    }

}