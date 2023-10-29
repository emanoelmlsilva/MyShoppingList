package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

class LoadingDataController {

    val TAG = "LoadingDataController"

    companion object {
        private lateinit var creditCardController: CreditCardController
        private lateinit var categoryController: CategoryController
        private lateinit var itemListController: ItemListController
        private lateinit var purchaseController: PurchaseController

        fun getData(context: Context, lifecycleOwner: LifecycleOwner): LoadingDataController {
            categoryController = CategoryController.getData(context, lifecycleOwner)
            itemListController = ItemListController.getData(context, lifecycleOwner)
            creditCardController = CreditCardController.getData(context, lifecycleOwner)
            purchaseController = PurchaseController.getData(context, lifecycleOwner)

            Log.d("TAG", "getData")

            return LoadingDataController()
        }
    }

    fun loadingData(user: User, callback: com.example.myshoppinglist.callback.Callback) {
        val email = user.email

        Log.d(TAG, "loadingData - findAllCreditCard")

        loadingDataCategories(email, object : com.example.myshoppinglist.callback.Callback {
            override fun onSuccess() {
                loadingDataCreditCard(email, object : CallbackObject<List<CreditCardDTO>> {
                    override fun onSuccess(creditCardCollection: List<CreditCardDTO>) {
                        Log.d(
                            TAG,
                            "findAllCreditCard - onSuccess ${creditCardCollection.size}"
                        )

                        creditCardCollection.toObservable().subscribeBy(
                            onNext = {
                                loadingDataItemList(it.id, callback)
                                loadingDataPurchase(it.id, callback)
                            },
                            onError = { callback.onCancel() },
                            onComplete = {
                                callback.onSuccess()
                            }
                        )
                    }

                    override fun onFailed(messageError: String) {
                        Log.d(TAG, "findAllCreditCard - onFailed ")
                        callback.onFailed(messageError)
                    }
                })

            }

            override fun onFailed(messageError: String) {
                Log.d(TAG, "findAndSaveCategories - onFailed ")
                callback.onFailed(messageError)
            }
        })
    }

    fun loadingDataCreditCard(email: String, callback: CallbackObject<List<CreditCardDTO>>) {
        creditCardController.saveAllCreditCard(email, callback)
    }

    fun loadingDataCategories(
        email: String,
        callback: com.example.myshoppinglist.callback.Callback
    ) {
        categoryController.findAndSaveCategories(email, callback)
    }

    fun loadingDataItemList(id: Long, callback: com.example.myshoppinglist.callback.Callback) {
        itemListController.saveItemListAll(id, callback)
    }

    fun loadingDataPurchase(idCard: Long, callback: Callback) {
        purchaseController.savePurchaseAll(idCard, callback)
    }
}