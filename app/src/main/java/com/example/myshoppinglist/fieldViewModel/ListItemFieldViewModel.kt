package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.ItemListController
import com.example.myshoppinglist.services.dtos.ItemListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListItemFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {

    private val itemListController = ItemListController.getData(context, lifecycleOwner)
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    var creditCardDTO = MutableLiveData(CreditCardDTODB())
    val itemListCollection = MutableLiveData(emptyList<ItemListDTO>())

    fun updateItemListAll(idCard: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            itemListController.getAllWithCategoryDB(idCard).observeForever { itemListAndCategory ->
                itemListCollection.value = itemListAndCategory.map {
                    ItemListDTO(
                        it.itemList,
                        it.category
                    )
                }
            }
        }

    }

    fun updateCreditCardDTO(idCard: Long){
        viewModelScope.launch(Dispatchers.Main) {
            creditCardController.findCreditCardByIdDB(idCard).observeForever{
                creditCardDTO.value = CreditCardDTODB().fromCreditCardDTODB(it)
            }
        }
    }

}