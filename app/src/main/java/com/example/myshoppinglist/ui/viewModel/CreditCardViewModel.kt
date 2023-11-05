package com.example.myshoppinglist.ui.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.services.repository.CreditCardRepository
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class CreditCardViewModel(
    private val creditCardRepository: CreditCardRepository,
    private val creditCardViewModel: com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
) : ViewModel() {

    private val TAG = "CreditCardViewModel"

    fun getAutoIncrement(): Int {
        return creditCardViewModel.getAutoIncrement()
    }

    fun updateCreditCardDB(cardCreditCard: CreditCard) {
        creditCardViewModel.updateCreditCard(cardCreditCard)
    }

    fun findCreditCardById(idCard: Long): LiveData<CreditCard> {
        return creditCardViewModel.findCreditCardById(idCard)
    }

    fun getAllWithSum(): LiveData<List<CreditCard>> {
        return creditCardViewModel.getAllWithSum()
    }

    fun getAll(): LiveData<List<CreditCard>> {
        return creditCardViewModel.getAll()
    }

    fun update(creditCardDTO: CreditCardDTO, callback: CallbackObject<CreditCardDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                creditCardRepository.update(creditCardDTO)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(creditCardDTO)
                    }
                    is SocketTimeoutException -> {
                        callback.onChangeValue(MeasureTimeService.messageNoService)
                        ResultData.NotConnectionService(creditCardDTO)
                    }
                    else -> {
                        ResultData.Error(Exception(e.message))
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val creditCard = result.data

                    Log.d(TAG, "creditCard $creditCard")

                    creditCard.idCard = creditCardDTO.idCard

                    creditCardViewModel.updateCreditCard(creditCard.toCreditCard())

                    callback.onSuccess()
                }
                is ResultData.NotConnectionService -> {
                    val creditCardData = result.data.toCreditCard()

                    Log.d(TAG, "creditCardData $creditCardData")

                    creditCardViewModel.updateCreditCard(creditCardData)

                    MeasureTimeService.resetMeasureTime(MeasureTimeService.TIME_DELAY_CONNECTION,
                        object : Callback {
                            override fun onChangeValue(newValue: Boolean) {
                                callback.onSuccess()
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

    fun save(creditCardDTO: CreditCardDTO, callback: CallbackObject<CreditCardDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                creditCardRepository.save(creditCardDTO)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(creditCardDTO)
                    }
                    is SocketTimeoutException -> {
                        ResultData.NotConnectionService(creditCardDTO)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val creditCard = result.data
                    creditCard.position = creditCardViewModel.getAutoIncrement()

                    Log.d(TAG, "creditCard $creditCard")

                    creditCardViewModel.insertCreditCard(creditCard.toCreditCard())

                    callback.onSuccess()
                }
                is ResultData.NotConnectionService -> {
                    val creditCardData = result.data.toCreditCard()
                    creditCardData.position = creditCardViewModel.getAutoIncrement()

                    Log.d(TAG, "creditCardData $creditCardData")

                    creditCardViewModel.insertCreditCard(creditCardData)

                    MeasureTimeService.resetMeasureTime(MeasureTimeService.TIME_DELAY_CONNECTION,
                        object : Callback {
                            override fun onChangeValue(newValue: Boolean) {
                                callback.onSuccess()
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

    fun findAndSaveAllCreditCard(email: String, callback: CallbackObject<List<CreditCardDTO>>) {
        viewModelScope.launch {
            val result = try {
                creditCardRepository.findAllCreditCard(email)
            } catch (e: Exception) {
                ResultData.Error(Exception(e.message))
            }

            when (result) {
                is ResultData.Success -> {
                    val creditCardResponse = result.data

                    val creditCardCollection = creditCardResponse.map { it.toCreditCardIdApi() }

                    creditCardViewModel.insertAllCreditCards(creditCardCollection)

                    callback.onSuccess(creditCardResponse)
                }
                else -> {
                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }
}