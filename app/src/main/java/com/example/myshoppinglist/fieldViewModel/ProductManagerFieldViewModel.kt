package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController


class ProductManagerFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {

    private val product: MutableLiveData<String> = MutableLiveData("")
    private var cardCreditCollection: MutableLiveData<List<CreditCardDTODB>> =
        MutableLiveData(mutableListOf())
    private var purchaseInfoCollection: MutableLiveData<List<PurchaseInfo>> =
        MutableLiveData(mutableListOf())
    private val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val categoryController = CategoryController.getData(context, lifecycleOwner)
    private val valueSum: MutableLiveData<Double> = MutableLiveData(0.0)
    private val quantityPurchases: MutableLiveData<String> = MutableLiveData("00")

    fun getProduct(): LiveData<String>{
        return product
    }


    fun getCategoryController(): CategoryController{
        return categoryController
    }

    fun getCardCreditCollection(): List<CreditCardDTODB>{
        return cardCreditCollection.value!!
    }

    fun getPurchaseInfoCollection(): LiveData<List<PurchaseInfo>>{
        return purchaseInfoCollection
    }

    fun getPurchaseController(): PurchaseController{
        return purchaseController
    }

    fun getCreditCardController(): CreditCardController{
        return creditCardController
    }

    fun getValueSum(): LiveData<Double>{
        return valueSum
    }

    fun getQuantityPurchases(): LiveData<String>{
        return quantityPurchases
    }

    private fun onChangePurchaseInfoCollection(newPurchaseInfoCollection: List<PurchaseInfo>) {
        purchaseInfoCollection.value = newPurchaseInfoCollection
    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
    }

    fun onChangeCardCreditCollection(newCardCreditCollection: List<CreditCardDTODB>) {
        cardCreditCollection.value = newCardCreditCollection
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

        val searchPurchases = purchaseController.getPurchasesOfSearchDB(
            result.collectionSearch,
            result.nameFields,
            if (objectFilter.textCollection.size > 0) "GROUP BY purchases.myShoppingId" else ""
        )

        onChangePurchaseInfoCollection(mountItemPurchase(searchPurchases))

        quantityPurchases.value =
            if (searchPurchases.size > 100) searchPurchases.size.toString() else if (searchPurchases.size < 10) "00${searchPurchases.size}" else "0${searchPurchases.size}"


        updateSumOfSearchPurchase(result)
    }

    private fun updateSumOfSearchPurchase(result: Result) {
        valueSum.value =
            purchaseController.getPurchasesSumOfSearchDB(result.collectionSearch, result.nameFields)
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

                val valueSum =
                    purchaseMultCollection.sumOf { if (it.purchase.discount > 0) it.purchase.price - it.purchase.discount else it.purchase.price * if (it.purchase.typeProduct == TypeProduct.QUANTITY) it.purchase.quantiOrKilo.toInt() else 1 }

                val purchaseInfo = PurchaseInfo(
                    purchase.name, category.idImage, valueSum, Color(category.color),
                    purchaseMultCollection
                )

                purchaseInfoCollection.add(purchaseInfo)
            }
        }

    }

    return purchaseInfoCollection
}