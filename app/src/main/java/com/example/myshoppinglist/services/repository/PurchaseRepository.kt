package com.example.myshoppinglist.services.repository

import ResultData
import android.util.Log
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.enums.StatusSaveData
import com.example.myshoppinglist.services.PurchaseService
import com.example.myshoppinglist.services.dtos.PurchaseDTOService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PurchaseRepository(private val purchaseService: PurchaseService) {

    private val TAG = "PurchaseRepository"

    suspend fun delete(idPurchaseApi: Long, callback: Callback): ResultData<PurchaseDTOService>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.delete(idPurchaseApi).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                delay(1000L)

                callback.onChangeStatus(StatusSaveData.SEND)
                delay(2500L)

                val purchaseResponse = purchaseExecute.body()?: PurchaseDTOService()
                ResultData.Success(purchaseResponse)
            }else{
                callback.onChangeStatus(StatusSaveData.ERROR)
                delay(2000L)

                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR DELETE ${purchaseExecute.errorBody()}"))
            }
        }
    }

    suspend fun save(purchase: PurchaseDTOService, callback: Callback): ResultData<PurchaseDTOService>{
        return withContext(Dispatchers.IO){
            val purchaseRequest = if(purchase.idOriginal != 0L) purchaseService.save(purchase.idOriginal, purchase) else purchaseService.save(purchase)

            val purchaseExecute = purchaseRequest.execute()

            return@withContext if(purchaseExecute.isSuccessful){
                delay(1000L)

                callback.onChangeStatus(StatusSaveData.SEND)
                delay(2500L)

                val purchaseResponse = purchaseExecute.body()?: PurchaseDTOService()
                ResultData.Success(purchaseResponse)
            }else{
                callback.onChangeStatus(StatusSaveData.ERROR)
                delay(2000L)

                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR SAVE ${purchaseExecute.errorBody()}"))
            }
        }

    }

    suspend fun update(purchase: PurchaseDTOService, callback: Callback): ResultData<PurchaseDTOService>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.update(purchase).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                delay(1000L)

                callback.onChangeStatus(StatusSaveData.SEND)
                delay(2500L)

                val purchaseResponse = purchaseExecute.body()?:PurchaseDTOService()

                ResultData.Success(purchaseResponse)
            }else{
                callback.onChangeStatus(StatusSaveData.ERROR)
                delay(2000L)
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR UPDATE ${purchaseExecute.errorBody()}"))
            }
        }
    }

    suspend fun getAll(idCard: Long): ResultData<List<PurchaseDTOService>>{
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

    suspend fun getPurchaseRepeatHabilitated(): ResultData<List<PurchaseDTOService>>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.findAllPurchaseRepeatDate().execute()

            return@withContext if(purchaseExecute.isSuccessful){
                val purchaseResponse = purchaseExecute.body()?: listOf()
                ResultData.Success(purchaseResponse)
            } else {
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR FIND ALL REPEAT HABILITATED ${purchaseExecute.errorBody()}"))
            }
        }
    }

    suspend fun removeRepeatPurchase(idPurchase: Long, callback: Callback): ResultData<PurchaseDTOService>{
        return withContext(Dispatchers.IO){
            val purchaseExecute = purchaseService.removeRepeatPurchase(idPurchase).execute()

            return@withContext if(purchaseExecute.isSuccessful){
                delay(1000L)

                callback.onChangeStatus(StatusSaveData.SEND)
                delay(2500L)

                val purchaseResponse = purchaseExecute.body()?: PurchaseDTOService()

                ResultData.Success(purchaseResponse)
            }else{
                callback.onChangeStatus(StatusSaveData.ERROR)
                delay(2000L)
                Log.d(TAG, "message error ${purchaseExecute.message()}")

                ResultData.Error(Exception("ERROR UPDATE ${purchaseExecute.errorBody()}"))
            }
        }
    }
}