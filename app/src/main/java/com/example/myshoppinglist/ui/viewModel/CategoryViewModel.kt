package com.example.myshoppinglist.ui.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.viewModels.CategoryViewModelDB
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.services.repository.CategoryRepository
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class CategoryViewModel(
    private val categoryRepository: CategoryRepository,
    private val categoryViewModelDB: CategoryViewModelDB
) : ViewModel() {

    private val TAG = "CategoryViewModel"

    fun getAllDB(): LiveData<List<Category>> {
        return categoryViewModelDB.getAll()
    }

    fun getCategoryByIdDB(idCategory: Long): LiveData<Category> {
        return categoryViewModelDB.getCategoryById(idCategory)
    }

    fun findAndSaveAllCategory(email: String, callback: Callback) {
        viewModelScope.launch {
            val result = try {
                categoryRepository.findAndSaveCategories(email)
            } catch (e: Exception) {
                ResultData.Error(e)
            }

            when (result) {
                is ResultData.Success -> {
                    val categoryCollectionResponse = result.data

                    Log.d(TAG, "categoryCollectionResponse $categoryCollectionResponse")

                    val categories = categoryCollectionResponse.map{it.toCategoryApi()}

                    categoryViewModelDB.insertCategories(categories, object : Callback{
                        override fun onSuccess() {
                            callback.onSuccess()
                        }

                        override fun onFailed(messageError: String) {
                            callback.onFailed(messageError)
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

    fun save(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                categoryRepository.save(categoryDTO)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(categoryDTO)
                    }
                    is SocketTimeoutException -> {
                        callback.onChangeValue(MeasureTimeService.messageNoService)
                        ResultData.NotConnectionService(categoryDTO)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val categoryResponse = result.data

                    Log.d(TAG, "categoryResponse $categoryResponse")

                    categoryViewModelDB.insertCategory(categoryResponse.toCategory())

                    callback.onSuccess()
                }
                is ResultData.NotConnectionService -> {

                    val categoryData = result.data.toCategory()

                    Log.d(TAG, "categoryData $categoryData")

                    categoryViewModelDB.insertCategory(categoryData)

                    MeasureTimeService.resetMeasureTime(MeasureTimeService.TIME_DELAY_CONNECTION, object : Callback {
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

    fun update(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        viewModelScope.launch {
            MeasureTimeService.init()
            val result = try {
                MeasureTimeService.startMeasureTime(callback = callback)
                categoryRepository.update(categoryDTO)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        MeasureTimeService.resetMeasureTimeErrorConnection(callback)
                        ResultData.NotConnectionService(categoryDTO)
                    }
                    is SocketTimeoutException -> {
                        callback.onChangeValue(MeasureTimeService.messageNoService)
                        ResultData.NotConnectionService(categoryDTO)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val categoryResponse = result.data

                    Log.d(TAG, "categoryResponse $categoryResponse")

                    categoryViewModelDB.updateCategory(categoryDTO.toCategory())

                    callback.onSuccess()
                }
                is ResultData.NotConnectionService -> {

                    val categoryData = result.data.toCategory()

                    Log.d(TAG, "categoryData $categoryData")

                    categoryViewModelDB.updateCategory(categoryData)

                    MeasureTimeService.resetMeasureTime(MeasureTimeService.TIME_DELAY_CONNECTION, object : Callback {
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
}