package com.example.myshoppinglist.ui.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.services.repository.ItemListRepository
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class ItemListViewModel(
    private val itemListRepository: ItemListRepository,
    private val itemListViewModelDB: com.example.myshoppinglist.database.viewModels.ItemListViewModelDB
) : ViewModel() {

    private val TAG = "ItemListViewModel"

    fun deleteItemListDB(itemList: ItemList, callback: Callback) {
        itemListViewModelDB.deleteItemList(itemList, callback)
    }

    fun getAllWithCategoryDB(idCard: Long): LiveData<List<ItemListAndCategory>> {
        return itemListViewModelDB.getAllWithCategory(idCard)
    }

    fun updateItemListDB(itemListDB: ItemList, callback: Callback) {
        itemListViewModelDB.updateItemList(itemListDB, callback)
    }

    fun insertItemListDB(itemListDB: ItemList, callback: Callback) {
        itemListViewModelDB.insertItemList(itemListDB, callback)
    }

    fun save(itemList: ItemListDTO, callback: CallbackObject<ItemListDTO>) {
        viewModelScope.launch {
            val result = try {
                itemListRepository.save(itemList)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        ResultData.NotConnectionService(itemList)
                    }
                    is SocketTimeoutException -> {
                        ResultData.NotConnectionService(itemList)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val itemListResponse = result.data

                    itemListResponse.creditCardDTO = itemList.creditCardDTO
                    itemListResponse.categoryDTO = itemList.categoryDTO

                    itemListViewModelDB.insertItemList(
                        itemListResponse.toItemList(),
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
                    val itemListData = result.data.toItemList()

                    itemListViewModelDB.insertItemList(
                        itemListData,
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
                    val messageError =
                        (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }

    fun update(itemList: ItemListDTO, callback: CallbackObject<ItemListDTO>) {
        viewModelScope.launch {
            val result = try {
                itemListRepository.update(itemList)
            } catch (e: Exception) {
                when (e) {
                    is ConnectException -> {
                        ResultData.NotConnectionService(itemList)
                    }
                    is SocketTimeoutException -> {
                        ResultData.NotConnectionService(itemList)
                    }
                    else -> {
                        ResultData.Error(e)
                    }
                }
            }

            when (result) {
                is ResultData.Success -> {
                    val itemListResponse = result.data

                    itemListResponse.creditCardDTO = itemList.creditCardDTO
                    itemListResponse.categoryDTO = itemList.categoryDTO
                    itemListResponse.myShoppingId = itemList.myShoppingId

                    itemListViewModelDB.updateItemList(
                        itemListResponse.toItemList(),
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
                    val itemListData = result.data.toItemList()

                    itemListViewModelDB.updateItemList(
                        itemListData,
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
                    val messageError =
                        (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")

                    callback.onFailed(messageError.toString())
                }
            }
        }
    }

    fun findAndSaveAllItemList(idCard: Long, callback: CallbackObject<List<ItemListDTO>>) {
        viewModelScope.launch {
            val result = try {
                itemListRepository.getAll(idCard)
            } catch (e: Exception) {
                ResultData.Error(Exception(e.message))
            }

            when (result) {
                is ResultData.Success -> {
                    val itemListCollection = result.data

                    Log.d(TAG, "itemListCollection $itemListCollection")

                    val itemListCollectionDB = itemListCollection.map { it.toItemListApi() }

                    itemListViewModelDB.insertItemListAll(
                        itemListCollectionDB,
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

}