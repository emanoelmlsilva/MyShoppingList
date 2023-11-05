package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.PurchaseViewModelDB
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.PurchaseService
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import com.example.myshoppinglist.services.repository.PurchaseRepository
import com.example.myshoppinglist.ui.viewModel.PurchaseViewModel
import io.reactivex.rxjava3.core.Observable

class PurchaseController {

    private val TAG = "PurchaseController"

    companion object {
        private lateinit var purchaseViewModel: PurchaseViewModel
        private val email = UserLoggedShared.getEmailUserCurrent()
        private lateinit var lifecycleOwner: LifecycleOwner

        fun getData(context: Context, mLifecycleOwner: LifecycleOwner): PurchaseController {
            lifecycleOwner = mLifecycleOwner
            purchaseViewModel = PurchaseViewModel(
                PurchaseRepository(PurchaseService.getPurchaseService()),
                PurchaseViewModelDB(context)
            )

            return PurchaseController()
        }
    }

    fun getMonthByIdCardDB(idCard: Long): LiveData<List<String>>{
        return purchaseViewModel.getMonthByIdCardDB(idCard)
    }

    fun getPurchasesAndCategoryWeekDB(): LiveData<List<PurchaseAndCategory>> {
        return purchaseViewModel.getPurchasesAndCategoryWeekDB()
    }

    fun savePurchaseDB(purchases: List<Purchase>, callback: Callback) {
        purchaseViewModel.insertPurchasesDB(purchases, callback)
    }

    fun getPurchaseByMonthDB(idCard: Long, month: String): LiveData<List<PurchaseAndCategory>>{
        return purchaseViewModel.getPurchaseByMonthDB(idCard, month)
    }

    fun sumPriceByMonthDB(idCard: Long, month: String): Double{
        return purchaseViewModel.sumPriceByMonthDB(idCard, month)
    }

    fun getPurchasesAll(idCard: Long): LiveData<List<Purchase>> {
        return purchaseViewModel.getPurchaseAllByIdCard(idCard)
    }

    fun getPurchasesOfSearchDB(
        arguments: MutableList<Any>,
        condition: String
    ): List<PurchaseAndCategory> {
        return getPurchasesOfSearchDB(arguments, condition, null)
    }

    fun getPurchasesOfSearchDB(
        arguments: MutableList<Any>,
        condition: String,
        valueGroupBy: String?
    ): List<PurchaseAndCategory> {
        return purchaseViewModel.getPurchasesOfSearchDB(arguments, condition, valueGroupBy)
    }

    fun getPurchasesSumOfSearchDB(arguments: MutableList<Any>, condition: String): Double {
        return purchaseViewModel.getPurchasesSumOfSearchDB(arguments, condition)
    }

    fun savePurchases(purchaseCollection: List<PurchaseDTO>, callback: Callback) {
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(
            lifecycleOwner
        ) {

            val indexObservable = Observable.range(0, purchaseCollection.size)

            Observable.fromIterable(purchaseCollection)
                .zipWith(indexObservable) { value, index ->
                    Pair(value, index)
                }
                .subscribe { (purchaseDTO, index) ->
                    purchaseDTO.category.userDTO = it
                    purchaseDTO.creditCard.userDTO = it

                    purchaseViewModel.save(purchaseDTO, object : CallbackObject<PurchaseDTO> {
                        override fun onSuccess() {
                            Log.d(TAG, "saveItemList - onSuccess")
                            if (index == purchaseCollection.size - 1) {
                                Log.d(TAG, "saveItemList - completed")
                                callback.onSuccess()
                            }
                        }

                        override fun onFailed(messageError: String) {
                            Log.d(TAG, "saveItemList - onFailed")
                            callback.onFailed(messageError)
                        }

                        override fun onChangeValue(newValue: String) {
                            Log.d(TAG, "saveItemList - onChangeValue")
                            callback.onChangeValue(newValue)
                        }

                        override fun onChangeValue(newValue: Boolean) {
                            Log.d(TAG, "saveItemList - onChangeValue")
                            callback.onChangeValue(newValue)
                        }
                    })
                }
        }
    }

    fun updatePurchase(purchaseDTO: PurchaseDTO, callback: Callback) {
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(
            lifecycleOwner
        ) {
            purchaseDTO.category.userDTO = it
            purchaseDTO.creditCard.userDTO = it

            purchaseViewModel.update(purchaseDTO, object : CallbackObject<PurchaseDTO> {
                override fun onSuccess() {
                    Log.d(TAG, "updatePurchase - onSuccess")
                    callback.onSuccess()
                }

                override fun onFailed(messageError: String) {
                    Log.d(TAG, "updatePurchase - onFailed")
                    callback.onFailed(messageError)
                }

                override fun onChangeValue(newValue: String) {
                    Log.d(TAG, "updatePurchase - onFailed")
                    callback.onChangeValue(newValue)
                }

                override fun onChangeValue(newValue: Boolean) {
                    Log.d(TAG, "saveItemList - onChangeValue")
                    callback.onChangeValue(newValue)
                }
            })
        }
    }

    fun savePurchaseAll(idCard: Long, callback: Callback) {
        purchaseViewModel.findAndSaveAllPurchase(
            idCard,
            object : CallbackObject<List<PurchaseDTO>> {
                override fun onSuccess(purchaseCollection: List<PurchaseDTO>) {
                    Log.d(TAG, "savePurchaseAll - onSuccess ${purchaseCollection.size}")
                }

                override fun onFailed(messageError: String) {
                    Log.d(TAG, "savePurchaseAll - onFailed ")
                    callback.onFailed(messageError)
                }
            })
    }

    fun deletePurchase(idPurchaseApi: Long, idPurchase: Long, callback: Callback){
        purchaseViewModel.deletePurchase(idPurchaseApi, idPurchase, object : CallbackObject<Any> {
            override fun onSuccess() {
                Log.d(TAG, "delete Purchase By Id - onSuccess")
                callback.onSuccess()
            }

            override fun onFailed(messageError: String) {
                Log.d(TAG, "delete Purchase By Id - onFailed ")
                callback.onFailed(messageError)
            }

            override fun onChangeValue(newValue: String) {
                Log.d(TAG, "delete Purchase By Id - onChangeValue")
                callback.onChangeValue(newValue)
            }

            override fun onChangeValue(newValue: Boolean) {
                Log.d(TAG, "saveItemList - onChangeValue")
                callback.onChangeValue(newValue)
            }
        })
    }
}