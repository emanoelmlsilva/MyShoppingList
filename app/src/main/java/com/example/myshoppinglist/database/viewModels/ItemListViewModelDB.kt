package com.example.myshoppinglist.database.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.MyShopListDataBase
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.repositories.ItemListRepository
import com.example.myshoppinglist.fieldViewModel.BaseFieldViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import kotlinx.coroutines.flow.Flow

class ItemListViewModelDB(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val TAG = "ItemListViewModel"
    private val repository: ItemListRepository
    private var mLifecycleOwner: LifecycleOwner

    init {
        val myShopListDataBase = MyShopListDataBase.getInstance(context)
        val itemListDAO = myShopListDataBase.itemListDAO()
        repository = ItemListRepository(itemListDAO)
        mLifecycleOwner = lifecycleOwner
    }

    fun insertItemListAll(itemListCollection: List<ItemList>, callback: Callback) {
        val action = Action {
            repository.insertItemListAll(itemListCollection)
        }

        Completable.fromAction(action).subscribe({
            callback.onSuccess()
        },
            { throwable ->
                Log.d(TAG, "ERROR " + throwable.message)
                callback.onFailed(throwable.message.toString())
            })
    }

    fun insertItemList(itemList: ItemList, callback: Callback) {
        val action = Action {
            repository.insertItemList(itemList)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onFailed(throwable.message.toString())
        })
    }

    fun updateItemList(itemList: ItemList, callback: Callback) {
        val action = Action {
            repository.updateItemList(itemList)
        }

        Completable.fromAction(action).subscribe({ callback.onSuccess() }, { throwable ->
            Log.d(TAG, "ERROR " + throwable.message)
            callback.onCancel()
        })
    }

    fun deleteItemList(itemList: ItemList) {
        repository.deleteItemList(itemList)
    }

    fun deleteItemList(itemList: ItemList, callback: Callback) {
        val action = Action {
            repository.deleteItemList(itemList)
        }

        Completable.fromAction(action).subscribe({
            callback.onSuccess()
        }, { throwable ->

            Log.d(TAG, "ERROR " + throwable.message)
            callback.onFailed(throwable.message.toString())

        })
    }

    fun getAllWithCategory(idCard: Long): LiveData<List<ItemListAndCategory>> {
        return repository.getAll(idCard)
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }
}