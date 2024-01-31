package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.screen.MarketItem
import com.example.myshoppinglist.services.dtos.ItemListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarketItemFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    val marketItemCollection = MutableLiveData(emptyList<MarketItem>())

    fun updateDiscount(idItemAndCategory: Long, newDiscount: Float){
        viewModelScope.launch(Dispatchers.IO) {
            val indexUpdate = marketItemCollection.value!!.indexOfFirst { it.itemListDTO.myShoppingId == idItemAndCategory }
//            for (index in 0 until marketItemCollection.value!!.size){
//                if(marketItemCollection.value!![index].itemListDTO.myShoppingId == idItemAndCategory){
                    marketItemCollection.value!![indexUpdate].discount = newDiscount
//                    break
//                }
//            }
        }
    }

    fun updateTypeProduct(idItemAndCategory: Long, newProduct: TypeProduct){
        val indexUpdate = marketItemCollection.value!!.indexOfFirst { it.itemListDTO.myShoppingId == idItemAndCategory }
        marketItemCollection.value!![indexUpdate].type = newProduct

    }

    fun updateCheckDiscount( idItemAndCategory: Long, checkDiscount: Boolean){
        val indexUpdate = marketItemCollection.value!!.indexOfFirst { it.itemListDTO.myShoppingId == idItemAndCategory }
        marketItemCollection.value!![indexUpdate].isCheckDiscount = checkDiscount
    }

    fun updateAmount( idItemAndCategory: Long, newAmount: String){
        val indexUpdate = marketItemCollection.value!!.indexOfFirst { it.itemListDTO.myShoppingId == idItemAndCategory }
        marketItemCollection.value!![indexUpdate].amount = newAmount
    }

    fun updatePrice( idItemAndCategory: Long, newPrice: Float){
        val indexUpdate = marketItemCollection.value!!.indexOfFirst { it.itemListDTO.myShoppingId == idItemAndCategory }
        marketItemCollection.value!![indexUpdate].price = newPrice
    }

    fun updateItemListCollection(itemListCollection: List<ItemListDTO>, callback: Callback? = null) {
        viewModelScope.launch(Dispatchers.Main) {
            updateMarketItemCollection(itemListCollection.map {
                MarketItem(
                    0F,
                    0F,
                    "0",
                    TypeProduct.QUANTITY,
                    false,
                    it
                )
            }, callback)
        }
    }

    fun updateJoinMarketItemCollection(itemListCollection: List<ItemListDTO>) {

        viewModelScope.launch(Dispatchers.Main) {
            val auxMarketItemCollection = marketItemCollection.value!! as MutableList

            updateItemListCollection(itemListCollection, object : Callback{
                override fun onSuccess() {
                    auxMarketItemCollection.addAll(marketItemCollection.value!!)

                    marketItemCollection.value = auxMarketItemCollection
                }
            })

        }

    }

    private fun updateMarketItemCollection(newMarketItemCollection: List<MarketItem>, callback: Callback?) {

        marketItemCollection.value = newMarketItemCollection

        callback?.onSuccess()

    }
}