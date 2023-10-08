package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.ItemListViewModelDB
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.ItemListService
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.services.repository.ItemListRepository
import com.example.myshoppinglist.ui.viewModel.ItemListViewModel

class ItemListController {

    val TAG = "ItemListController"

    companion object {
        private lateinit var itemListViewModel: ItemListViewModel
        private val email = UserLoggedShared.getEmailUserCurrent()
        private lateinit var lifecycleOwner: LifecycleOwner

        fun getData(context: Context, mLifecycleOwner: LifecycleOwner): ItemListController {

            lifecycleOwner = mLifecycleOwner
            itemListViewModel = ItemListViewModel(
                ItemListRepository(ItemListService.getItemPurchaseService()),
                ItemListViewModelDB(context, lifecycleOwner)
            )

            return ItemListController()
        }
    }

    fun deleteItemListDB(itemList: ItemList, callback: Callback){
        itemListViewModel.deleteItemListDB(itemList, callback)
    }

    fun getAllWithCategoryDB(idCard: Long): LiveData<List<ItemListAndCategory>>{
        return itemListViewModel.getAllWithCategoryDB(idCard)
    }

    fun updateItemListDB(itemList: ItemList, callback: Callback) {
        itemListViewModel.updateItemListDB(itemList, callback)
    }

    fun saveItemListDB(itemList: ItemList, callback: com.example.myshoppinglist.callback.Callback) {
        itemListViewModel.insertItemListDB(itemList, callback)
    }

    fun saveItemList(itemList: ItemListDTO, callback: CallbackObject<ItemListDTO>) {
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(lifecycleOwner) {
            itemList.categoryDTO.userDTO = it
            itemList.creditCardDTO.userDTO = it

            itemListViewModel.save(itemList, callback)
        }
    }

    fun updateItemList(itemList: ItemListDTO, callback: Callback){
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(lifecycleOwner){
            itemList.categoryDTO.userDTO = it
            itemList.creditCardDTO.userDTO = it

            itemListViewModel.update(itemList, object : CallbackObject<ItemListDTO> {
                override fun onSuccess() {
                    Log.d(TAG, "updateItemList - onSuccess")
                    callback.onSuccess()                }

                override fun onFailed(messageError: String) {
                    callback.onFailed(messageError)
                }
            })
        }
    }

    fun saveItemListAll(
        idCard: Long,
        callback: Callback
    ) {

        itemListViewModel.findAndSaveAllItemList(
            idCard,
            object : CallbackObject<List<ItemListDTO>> {
                override fun onSuccess(itemListCollection: List<ItemListDTO>) {

                    Log.d(TAG, "saveItemListAll - onSuccess ${itemListCollection.size}")

                }

                override fun onFailed(messageError: String) {
                    Log.d(TAG, "findAndSaveAllItemList - onFailed ")
                    callback.onFailed(messageError)
                }
            })
    }
}