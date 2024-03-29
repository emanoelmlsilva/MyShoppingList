package com.example.myshoppinglist.services.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.PurchaseViewModelDB
import com.example.myshoppinglist.services.dtos.PurchaseDTO
import com.example.myshoppinglist.services.repository.PurchaseRepository
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class PurchaseViewModel(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseViewModelDB: PurchaseViewModelDB
) : ViewModel() {

    private val TAG = "PurchaseViewModel"

    fun getMonthByIdCardDB(idCard: Long): LiveData<List<String>> {
        return purchaseViewModelDB.getMonthByIdCard(idCard)
    }

    fun getPurchasesAndCategoryWeekDB(): LiveData<List<PurchaseAndCategory>> {
        return purchaseViewModelDB.getPurchasesAndCategoryWeek()
    }

    fun getPurchasesSumOfSearchDB(arguments: String): Flow<Double> {
        return purchaseViewModelDB.getPurchasesSumOfSearch(arguments)
    }

    fun getPurchasesOfSearchDB(arguments: String): Flow<List<PurchaseAndCategory>> {
        return purchaseViewModelDB.getPurchasesOfSearch(arguments)
    }

    fun getPurchaseByMonthDB(idCard: Long, month: String): LiveData<List<PurchaseAndCategory>> {
        return purchaseViewModelDB.getPurchaseByMonth(idCard, month)
    }

    fun sumPriceByMonthDB(idCard: Long, month: String): Double {
        return purchaseViewModelDB.sumPriceByMonth(idCard, month)
    }

    fun getPurchaseAllByIdCard(idCard: Long): LiveData<List<Purchase>> {
        return purchaseViewModelDB.getPurchaseAllByIdCard(idCard)
    }

    fun insertPurchasesDB(purchases: List<Purchase>, callback: Callback) {
        purchaseViewModelDB.insertPurchases(purchases, callback)
    }

    fun insertPurchaseDB(purchase: Purchase, callback: Callback) {
        purchaseViewModelDB.insertPurchase(purchase, callback)
    }

    fun save(purchase: PurchaseDTO, callback: CallbackObject<PurchaseDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                purchaseRepository.save(purchase)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(purchase)
                    }
                    is SocketTimeoutException -> {
                        callback.onChangeValue(MeasureTimeService.messageNoService)
                        ResultData.NotConnectionService(purchase)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val purchaseResponse = result.data

                    purchaseResponse.creditCard = purchase.creditCard
                    purchaseResponse.category = purchase.category

                    purchaseViewModelDB.insertPurchase(purchaseResponse.toPurchase(),
                        object : Callback {
                            override fun onSuccess() {
                                callback.onSuccess()
                            }

                            override fun onFailed(messageError: String) {
                                val messageError =
                                    (result as ResultData.Error).exception.message

                                Log.d(TAG, "error $messageError")

                                callback.onFailed(messageError.toString())
                            }
                        })
                }
                is ResultData.NotConnectionService -> {

                    val purchaseData = result.data.toPurchase()

                    purchaseViewModelDB.insertPurchase(purchaseData,
                        object : Callback {
                            override fun onSuccess() {
                                MeasureTimeService.resetMeasureTime(
                                    MeasureTimeService.TIME_DELAY_CONNECTION,
                                    object : Callback {
                                        override fun onChangeValue(newValue: Boolean) {
                                            callback.onSuccess()
                                        }
                                    })
                            }

                            override fun onFailed(messageError: String) {
                                val messageError =
                                    (result as ResultData.Error).exception.message

                                Log.d(TAG, "error $messageError")

                                callback.onFailed(messageError.toString())
                            }
                        })
                }
                else -> {
                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }

    fun update(purchase: PurchaseDTO, callback: CallbackObject<PurchaseDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                purchaseRepository.update(purchase)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(purchase)
                    }
                    is SocketTimeoutException -> {
                        ResultData.NotConnectionService(purchase)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val purchaseResponse = result.data

                    purchaseResponse.creditCard = purchase.creditCard
                    purchaseResponse.category = purchase.category
                    purchaseResponse.myShoppingId = purchase.myShoppingId

                    purchaseViewModelDB.updatePurchase(purchaseResponse.toPurchase(),
                        object : Callback {
                            override fun onSuccess() {
                                callback.onSuccess()
                            }

                            override fun onFailed(messageError: String) {
                                val messageError = (result as ResultData.Error).exception.message

                                Log.d(TAG, "error $messageError")

                                callback.onFailed(messageError.toString())
                            }
                        })
                }
                is ResultData.NotConnectionService -> {

                    val purchaseData = result.data.toPurchase()

                    purchaseViewModelDB.updatePurchase(purchaseData,
                        object : Callback {
                            override fun onSuccess() {
                                MeasureTimeService.resetMeasureTime(
                                    MeasureTimeService.TIME_DELAY_CONNECTION,
                                    object : Callback {
                                        override fun onChangeValue(newValue: Boolean) {
                                            callback.onSuccess()
                                        }
                                    })
                            }

                            override fun onFailed(messageError: String) {
                                val messageError = (result as ResultData.Error).exception.message

                                Log.d(TAG, "error $messageError")

                                callback.onFailed(messageError.toString())
                            }
                        })
                }
                else -> {
                    val messageError =
                        (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }

    fun findAndSaveAllPurchase(idCard: Long, callback: CallbackObject<List<PurchaseDTO>>) {
        viewModelScope.launch {
            val result = try {
                purchaseRepository.getAll(idCard)
            } catch (e: Exception) {
                ResultData.Error(Exception(e.message))
            }

            when (result) {
                is ResultData.Success -> {
                    val purchaseCollection = result.data

                    val purchaseCollectionDB = purchaseCollection.map { it.toPurchaseApi() }

                    purchaseViewModelDB.insertPurchases(purchaseCollectionDB,
                        object : Callback {
                            override fun onSuccess() {
                                callback.onSuccess()
                            }

                            override fun onFailed(messageError: String) {
                                val messageError =
                                    (result as ResultData.Error).exception.message

                                Log.d(TAG, "error $messageError")

                                callback.onFailed(messageError.toString())
                            }
                        })
                }
                else -> {
                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }


    fun deletePurchase(idPurchaseApi: Long, idPurchase: Long, callback: CallbackObject<Any>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                purchaseRepository.delete(idPurchaseApi)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(idPurchaseApi)
                    }
                    is SocketTimeoutException -> {
                        callback.onChangeValue(MeasureTimeService.messageNoService)
                        ResultData.NotConnectionService(idPurchaseApi)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    purchaseViewModelDB.deletePurchaseByIdApi(idPurchaseApi, object : Callback {
                        override fun onSuccess() {
                            callback.onSuccess()
                        }

                        override fun onFailed(messageError: String) {
                            val messageError = (result as ResultData.Error).exception.message

                            Log.d(TAG, "error $messageError")

                            callback.onFailed(messageError.toString())
                        }
                    })
                }
                is ResultData.NotConnectionService -> {

                    purchaseViewModelDB.deletePurchaseByI(idPurchase, object : Callback {
                        override fun onSuccess() {
                            MeasureTimeService.resetMeasureTime(
                                MeasureTimeService.TIME_DELAY_CONNECTION,
                                object : Callback {
                                    override fun onChangeValue(newValue: Boolean) {
                                        callback.onSuccess()
                                    }
                                })
                        }

                        override fun onFailed(messageError: String) {

                            Log.d(TAG, "error $messageError")

                            callback.onFailed(messageError)
                        }
                    })

                }
                else -> {
                    val messageError =
                        (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }
}