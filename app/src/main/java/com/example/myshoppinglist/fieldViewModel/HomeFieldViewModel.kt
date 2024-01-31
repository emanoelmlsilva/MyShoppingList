package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MountStructureCrediCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    val purchaseCollection: MutableLiveData<List<PurchaseAndCategoryDTO>> =
        MutableLiveData(emptyList())
    var creditCardCollection = MutableLiveData(emptyList<CreditCardDTODB>())

    fun updateCreditCards(){
        viewModelScope.launch(Dispatchers.Main) {
            creditCardController.getAllWithSumDB().observeForever { creditCards ->

                val creditCardWitchCreateCollection = MountStructureCrediCard().mountSpedingDate(
                    creditCards
                ).toMutableList()

                creditCardWitchCreateCollection.add(CreditCardDTODB())

                creditCardCollection.value = creditCardWitchCreateCollection
            }
        }
    }

    fun updatePurchases() {
        viewModelScope.launch(Dispatchers.Main) {
            purchaseController.getPurchasesAndCategoryWeekDB().observeForever{ list->
                purchaseCollection.value = list.map { purchaseAndCategory ->
                    val purchaseFormatData = PurchaseDTO(purchaseAndCategory.purchase)
                    val categoryFormatData = CategoryDTO()
                    categoryFormatData.toCategoryDTO(purchaseAndCategory.category)

                    val purchaseAndCategoryDTO =
                        PurchaseAndCategoryDTO(purchaseFormatData, categoryFormatData)

                    purchaseAndCategoryDTO.dateFormat =
                        FormatDateUtils().getNameDay(purchaseFormatData.date).uppercase()

                    purchaseAndCategoryDTO.priceFormat = MaskUtils.maskValue(String.format("%.2f", purchaseAndCategory.purchase.price))

                    purchaseAndCategoryDTO.discountFormat = MaskUtils.maskValue(String.format("%.2f", purchaseAndCategory.purchase.discount))

                    purchaseAndCategoryDTO.totalWithoutDiscountFormat = MaskUtils.maskValue(String.format("%.2f", (purchaseAndCategory.purchase.price - purchaseAndCategory.purchase.discount)).toString())

                    purchaseAndCategoryDTO
                }
            }
        }

    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

}