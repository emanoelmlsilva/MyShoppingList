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
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*


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
    private val email = UserLoggedShared.getEmailUserCurrent()

    fun updateCategories() {
        viewModelScope.launch(Dispatchers.Main) {
            categoryController.getAllDB().observeForever {
                categoryCollection.value = it
            }
        }
    }

    fun updateCreditCardCollection() {
        viewModelScope.launch(Dispatchers.Main) {
            creditCardController.findAllDB().observeForever {
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

    private fun mountArgumentsQuerySearchDatabase(objectFilter: ObjectFilter, isSumSearch: Boolean? = false): String {

        var argumentsQuery: String = ""

        if (objectFilter.textCollection.isNotEmpty()) {

            objectFilter.textCollection.forEachIndexed { index, product ->
                argumentsQuery += "name ${SintaxQueryUtils.LIKE} '%' || ${product.trim()} || '%'"

                if (objectFilter.textCollection.size > 1 && index < objectFilter.textCollection.size - 1) {
                    argumentsQuery += " ${SintaxQueryUtils.OR} "
                }
            }
        }

        if (objectFilter.priceMin != null && objectFilter.priceMin!! >= 0) {
            argumentsQuery += "price >= ${objectFilter.priceMin!!}"
        }

        if (objectFilter.priceMax != null && objectFilter.priceMax!! >= 0) {
            argumentsQuery += " ${SintaxQueryUtils.AND} price <= ${objectFilter.priceMax!!}"
        }

        if (objectFilter.idCard > 0 || objectFilter.cardFilter.nickName.isNotBlank()) {

            argumentsQuery += " ${if(argumentsQuery.isNotEmpty()) SintaxQueryUtils.AND else ""} purchaseCardId = ${if (objectFilter.idCard > 0) objectFilter.idCard else objectFilter.cardFilter.id}"

            argumentsQuery += " ${SintaxQueryUtils.AND} credit_cards.myShoppingId = purchaseCardId "
        }

        if (objectFilter.month.isNotBlank()) {

            val dateCurrent = objectFilter.month.split("-")
            val monthCurrent = dateCurrent[1].toInt()
            val yearMonth = dateCurrent[0].toInt()

            val nextMonthAndYear = FormatDateUtils().getNextMonthAndYear(monthCurrent, yearMonth)

            argumentsQuery += " ${SintaxQueryUtils.AND} purchases.date ${SintaxQueryUtils.BETWEEN} '${objectFilter.month}-' || (${SintaxQueryUtils.CASE} ${SintaxQueryUtils.WHEN} (credit_cards.dayClosedInvoice) > 9 ${SintaxQueryUtils.THEN} (credit_cards.dayClosedInvoice) ${SintaxQueryUtils.ELSE} '0' || (credit_cards.dayClosedInvoice) ${SintaxQueryUtils.END}) ${SintaxQueryUtils.AND} '$nextMonthAndYear' || (${SintaxQueryUtils.CASE} ${SintaxQueryUtils.WHEN} (credit_cards.dayClosedInvoice - 1) > 9 ${SintaxQueryUtils.THEN} (credit_cards.dayClosedInvoice - 1) ${SintaxQueryUtils.ELSE} '0' || (credit_cards.dayClosedInvoice - 1) ${SintaxQueryUtils.END})"

        } else{
            val monthAndYearNumber =
                FormatDateUtils().getMonthAndYearNumber(FormatDateUtils().getNameMonth((Date().month + 1).toString()))

            argumentsQuery += "${if(argumentsQuery.isNotEmpty()) " ${SintaxQueryUtils.AND} " else " "} date ${SintaxQueryUtils.LIKE} '%' || '$monthAndYearNumber' || '%' "
        }

        if (objectFilter.categoryCollection.size > 0) {

            objectFilter.categoryCollection.forEachIndexed { index, category ->
                argumentsQuery += " ${SintaxQueryUtils.AND} categoryOwnerId = ${category.myShoppingId}"

                if (objectFilter.categoryCollection.size > 1 && index < objectFilter.categoryCollection.size - 1) {
                    argumentsQuery += " ${SintaxQueryUtils.OR} "
                }
            }
        }

        if(!isSumSearch!!){
            argumentsQuery += " ${SintaxQueryUtils.AND} purchases.purchaseUserId = '$email' "

            argumentsQuery += "${SintaxQueryUtils.GROUP} ${SintaxQueryUtils.BY} purchases.myShoppingIdPurchase"
        }


        return argumentsQuery
    }


    fun searchPurchases(objectFilter: ObjectFilter) {

        val argumentsQuery = mountArgumentsQuerySearchDatabase(objectFilter)

        updateSumOfSearchPurchase(mountArgumentsQuerySearchDatabase(objectFilter, true))

        val purchaseMountItem = purchaseController.getPurchasesOfSearchDB(argumentsQuery).map { mountItemPurchase(it) }


        viewModelScope.launch(Dispatchers.Main) {
            purchaseMountItem.collect {
                quantityPurchases.value =
                    if (it.size > 100) it.size.toString() else if (it.size < 10) "00${it.size}" else "0${it.size}"

                onChangePurchaseInfoCollection(it)
            }
        }

    }

    private fun updateSumOfSearchPurchase(argumentsQuery: String) {

        viewModelScope.launch(Dispatchers.Main) {

            purchaseController.getPurchasesSumOfSearchDB(argumentsQuery).filterNotNull()
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


                val resultPurchaseInfo = purchaseFilterCollection.map { purchaseAndCategory ->
                    val purchaseFormatData = PurchaseDTO(purchaseAndCategory.purchase)
                    val categoryFormatData = CategoryDTO()
                    categoryFormatData.toCategoryDTO(purchaseAndCategory.category)

                    val purchaseAndCategoryDTO =
                        PurchaseAndCategoryDTO(purchaseFormatData, categoryFormatData)

                    purchaseAndCategoryDTO.dateFormat =
                        FormatDateUtils().getNameDay(purchaseFormatData.date).uppercase()

                    purchaseAndCategoryDTO.priceFormat = MaskUtils.maskValue(
                        String.format(
                            "%.2f",
                            purchaseAndCategory.purchase.price
                        )
                    )

                    purchaseAndCategoryDTO.discountFormat = MaskUtils.maskValue(
                        String.format(
                            "%.2f",
                            purchaseAndCategory.purchase.discount
                        )
                    )

                    purchaseAndCategoryDTO.totalWithoutDiscountFormat = MaskUtils.maskValue(
                        String.format(
                            "%.2f",
                            (purchaseAndCategory.purchase.price - purchaseAndCategory.purchase.discount)
                        ).toString()
                    )

                    purchaseAndCategoryDTO
                }

                val valueSum =
                    purchaseMultCollection.sumOf { if (it.purchase.discount > 0) it.purchase.price - it.purchase.discount else it.purchase.price * if (it.purchase.typeProduct == TypeProduct.QUANTITY) it.purchase.quantiOrKilo.toInt() else 1 }

                val purchaseInfo = PurchaseInfo(
                    purchase.name,
                    category.idImage,
                    String.format("%.2f", valueSum).toDouble(),
                    Color(category.color),
                    resultPurchaseInfo as MutableList<PurchaseAndCategoryDTO>
                )

                purchaseInfoCollection.add(purchaseInfo)
            }
        }

    }

    return purchaseInfoCollection
}