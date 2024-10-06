package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.viewModels.PurchaseViewModelDB
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.services.dtos.PurchaseDTOService

class PurchaseViewModel(context: Context, val lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {

    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val purchaseList: MutableLiveData<List<PurchaseDTO>> = MutableLiveData(emptyList())
    private val purchaseViewModelDB = PurchaseViewModelDB(context)

    fun getPurchaseAllWithRepeatByIdCard(idCard: Long) {
        purchaseViewModelDB.getPurchaseAllWithRepeatByIdCard(idCard)
            .observeForever { purchaseList ->
                changePurchaseList(purchaseList.map { PurchaseDTO(it) })
            }
    }

    fun changePurchaseList(newPurchaseList: List<PurchaseDTO>) {
        purchaseList.value = newPurchaseList
    }

    fun getPurchaseList(): LiveData<List<PurchaseDTO>> {
        return purchaseList
    }

    fun removeRepeatPurchase(purchaseDTO: PurchaseDTO, callback: Callback) {
        purchaseController.removeRepeatPurchase(purchaseDTO, callback)
    }
}