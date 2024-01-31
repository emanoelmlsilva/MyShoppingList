package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MountStructureCrediCard
import com.example.myshoppinglist.utils.SeparateDateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class ProductManagerFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {

    private val product = MutableStateFlow("")
    val cardCreditCollection = MutableStateFlow(emptyList<CreditCardDTODB>())
    val categoryCollection = MutableStateFlow(emptyList<Category>())
    var purchaseInfoCollection = MutableStateFlow(emptyList<PurchaseInfo>())
    private val quantityPurchases = MutableStateFlow("00")
    private val valueSum = MutableStateFlow(0.0)

    private val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val categoryController = CategoryController.getData(context, lifecycleOwner)

    fun updateCategories(){
        viewModelScope.launch(Dispatchers.Main) {
            categoryController.getAllDB().observeForever{
                categoryCollection.value = it
            }
        }
    }

    fun updateCreditCardCollection(){
        viewModelScope.launch(Dispatchers.Main) {
            creditCardController.findAllDB().observeForever{
                cardCreditCollection.value = MountStructureCrediCard().mountSpedingDate(
                    it
                )
            }
        }
    }

    fun getProduct(): Flow<String> {
        return product
    }

    fun getPurchaseInfoCollection(): Flow<List<PurchaseInfo>> {
        return purchaseInfoCollection
    }

    fun getValueSum(): Flow<Double> {
        return valueSum
    }

    fun getQuantityPurchases(): Flow<String> {
        return quantityPurchases
    }

    private fun onChangePurchaseInfoCollection(newPurchaseInfoCollection: List<PurchaseInfo>) {
        purchaseInfoCollection.value = newPurchaseInfoCollection
    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

    private fun mountObjectSearchDatabase(objectFilter: ObjectFilter): Result {

        var nameFields: String = ""
        var collectionSearch: MutableList<Any> = mutableListOf()

        if (objectFilter.textCollection.isNotEmpty()) {

            if (collectionSearch.isNotEmpty()) {
                nameFields += " AND "
            }

            objectFilter.textCollection.forEachIndexed { index, product ->
                nameFields += "name LIKE '%' || ? || '%'"
                collectionSearch.add(product.trim())

                if (objectFilter.textCollection.size > 1 && index < objectFilter.textCollection.size - 1) {
                    nameFields += " OR "
                }
            }
        }

        if (objectFilter.priceMin != null && objectFilter.priceMin!! >= 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price >= ?"
            collectionSearch.add(objectFilter.priceMin!!)
        }

        if (objectFilter.priceMax != null && objectFilter.priceMax!! >= 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price <= ?"
            collectionSearch.add(objectFilter.priceMax!!)
        }

        if (objectFilter.idCard > 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "purchaseCardId =?"
            collectionSearch.add(objectFilter.idCard)
        }

        if (objectFilter.month.isNotBlank()) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "date LIKE '%' || ? || '%'"
            collectionSearch.add(objectFilter.month)
        }

        if (objectFilter.categoryCollection.size > 0) {

            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }

            objectFilter.categoryCollection.forEachIndexed { index, category ->
                nameFields += "categoryOwnerId = ?"
                collectionSearch.add(category.myShoppingId)

                if (objectFilter.categoryCollection.size > 1 && index < objectFilter.categoryCollection.size - 1) {
                    nameFields += " OR "
                }
            }
        }

        if (objectFilter.cardFilter.nickName.isNotBlank()) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }

            nameFields += "purchaseCardId = ?"
            collectionSearch.add(objectFilter.cardFilter.id)
        }

        return Result(nameFields, collectionSearch)
    }

    fun searchPurchases(objectFilter: ObjectFilter) {

        val result = mountObjectSearchDatabase(objectFilter)

        updateSumOfSearchPurchase(result)

        val purchaseMountItem = purchaseController.getPurchasesOfSearchDB(
            result.collectionSearch,
            result.nameFields,
            if (objectFilter.textCollection.size > 0) "GROUP BY purchases.myShoppingIdPurchase" else ""
        ).map { mountItemPurchase(it) }


        viewModelScope.launch(Dispatchers.Main) {
            purchaseMountItem.collect {
                quantityPurchases.value =
                    if (it.size > 100) it.size.toString() else if (it.size < 10) "00${it.size}" else "0${it.size}"

                onChangePurchaseInfoCollection(it)
            }
        }

    }

    private fun updateSumOfSearchPurchase(result: Result) {

        viewModelScope.launch(Dispatchers.Main) {

            purchaseController.getPurchasesSumOfSearchDB(result.collectionSearch, result.nameFields)
                .collect {
                    valueSum.value = it
                }

        }
    }

    data class Result(val nameFields: String, val collectionSearch: MutableList<Any>)

}


private fun mountItemPurchase(purchaseCollection: List<PurchaseAndCategory>): List<PurchaseInfo> {
    val purchaseInfoCollection = mutableListOf<PurchaseInfo>()

    if (purchaseCollection.isEmpty()) return purchaseInfoCollection

    purchaseCollection.forEach { purchaseAndCategory ->

        val purchase = purchaseAndCategory.purchase
        val category = purchaseAndCategory.category

        val purchaseFilterCollection: List<PurchaseAndCategory> =
            purchaseCollection.filter { item -> purchase.name == item.purchase.name }

        val hasNotItemPurchase =
            purchaseInfoCollection.firstOrNull { item -> item.title == purchase.name } == null

        if (hasNotItemPurchase) {
            if (purchaseFilterCollection.isNotEmpty()) {
                val purchaseMultCollection: MutableList<PurchaseAndCategory> =
                    purchaseFilterCollection.map { it } as MutableList<PurchaseAndCategory>


                val resultPurchaseInfo = purchaseFilterCollection.map{purchaseAndCategory ->
                    val purchaseFormatData = PurchaseDTO(purchaseAndCategory.purchase)
                    val categoryFormatData = CategoryDTO()
                    categoryFormatData.toCategoryDTO(purchaseAndCategory.category)

                    val purchaseAndCategoryDTO =
                        PurchaseAndCategoryDTO(purchaseFormatData, categoryFormatData)

                    purchaseAndCategoryDTO.dateFormat = FormatDateUtils().getNameDay(purchaseFormatData.date).uppercase()

                    purchaseAndCategoryDTO.priceFormat = MaskUtils.maskValue(String.format("%.2f", purchaseAndCategory.purchase.price))

                    purchaseAndCategoryDTO.discountFormat = MaskUtils.maskValue(String.format("%.2f", purchaseAndCategory.purchase.discount))

                    purchaseAndCategoryDTO.totalWithoutDiscountFormat = MaskUtils.maskValue(String.format("%.2f", (purchaseAndCategory.purchase.price - purchaseAndCategory.purchase.discount)).toString())

                    purchaseAndCategoryDTO}

                val valueSum =
                    purchaseMultCollection.sumOf { if (it.purchase.discount > 0) it.purchase.price - it.purchase.discount else it.purchase.price * if (it.purchase.typeProduct == TypeProduct.QUANTITY) it.purchase.quantiOrKilo.toInt() else 1 }

                val purchaseInfo = PurchaseInfo(
                    purchase.name, category.idImage, String.format("%.2f", valueSum).toDouble(), Color(category.color),
                    resultPurchaseInfo as MutableList<PurchaseAndCategoryDTO>
                )

                purchaseInfoCollection.add(purchaseInfo)
            }
        }

    }

    return purchaseInfoCollection
}