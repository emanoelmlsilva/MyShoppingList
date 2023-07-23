package com.example.myshoppinglist.services.repository

import ResultData
import android.util.Log
import com.example.myshoppinglist.services.PurchaseService
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PurchaseRepository(private val purchaseService: PurchaseService) {

    private val TAG = "PurchaseRepository"

    suspend fun save(purchase: PurchaseDTO): ResultData<PurchaseDTO>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.save(purchase).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                val purchaseResponse = purchaseExecute.body()?: PurchaseDTO()
                ResultData.Success(purchaseResponse)
            }else{
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR SAVE ${purchaseExecute.errorBody()}"))
            }
        }

    }

    suspend fun update(purchase: PurchaseDTO): ResultData<PurchaseDTO>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.update(purchase).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                val purchaseResponse = purchaseExecute.body()?:PurchaseDTO()

                ResultData.Success(purchaseResponse)
            }else{
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR UPDATE ${purchaseExecute.errorBody()}"))
            }
        }
    }

    suspend fun getAll(idCard: Long): ResultData<List<PurchaseDTO>>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.findAllByCardId(idCard).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                val purchaseResponse = purchaseExecute.body()?: listOf()
                ResultData.Success(purchaseResponse)
            }else{
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR FIND ALL ${purchaseExecute.errorBody()}"))
            }
        }
    }
}