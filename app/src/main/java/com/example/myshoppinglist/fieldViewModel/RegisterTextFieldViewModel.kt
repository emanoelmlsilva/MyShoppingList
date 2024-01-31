package com.example.myshoppinglist.fieldViewModel

import android.os.Handler
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.MaskUtils

class RegisterTextFieldViewModel : BaseFieldViewModel() {
    var product: MutableLiveData<String> = MutableLiveData("")
    var price: MutableLiveData<String> = MutableLiveData("")
    var quantOrKilo: MutableLiveData<String> = MutableLiveData("")
    var locale: MutableLiveData<String> = MutableLiveData("")
    var creditCard: MutableLiveData<CreditCard> = MutableLiveData(CreditCard())
    var dateCurrent: MutableLiveData<String> = MutableLiveData("")
    var category: MutableLiveData<Long> = MutableLiveData(null)
    var isBlock: MutableLiveData<Boolean> = MutableLiveData(false)
    var resetDate: MutableLiveData<Boolean> = MutableLiveData(false)
    var updateDate: MutableLiveData<Boolean> = MutableLiveData(false)
    var typeProduct: MutableLiveData<TypeProduct> = MutableLiveData(TypeProduct.QUANTITY)
    val purchaseAndCategoryDTOCollection: MutableLiveData<MutableList<PurchaseAndCategoryDTO>> =
        MutableLiveData<MutableList<PurchaseAndCategoryDTO>>(mutableListOf())
    private val categoryCurrent: MutableLiveData<Category> = MutableLiveData(Category())
    val countProduct: MutableLiveData<Int> = MutableLiveData(0)
    val discount: MutableLiveData<String> = MutableLiveData("")
    val enableButtonAdd: MutableLiveData<Boolean> = MutableLiveData(false)

    //variavel de error
    val productError: MutableLiveData<Boolean> = MutableLiveData(false)
    val priceError: MutableLiveData<Boolean> = MutableLiveData(false)
    val localeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val idCardError: MutableLiveData<Boolean> = MutableLiveData(false)
    val typeCategoryError: MutableLiveData<Boolean> = MutableLiveData(false)
    val quantOrKiloError: MutableLiveData<Boolean> = MutableLiveData(false)
    val discountError: MutableLiveData<Boolean> = MutableLiveData(false)

    val index: MutableLiveData<Int> = MutableLiveData(-1)
    val categoryCollection = MutableLiveData<MutableList<Category>>(mutableListOf())

    val email = UserLoggedShared.getEmailUserCurrent()

    fun updateData(purchase: Purchase, newIndex : Int) {
        onChangeProduct(purchase.name)
        onChangePrice(purchase.price.toString())
        onChangeCategory(purchase.categoryOwnerId)
        onChangeTypeProduct(purchase.typeProduct)
        onChangeLocale(purchase.locale)
        onChangeDateCurrent(purchase.date)
        creditCard.value!!.myShoppingId = purchase.purchaseCardId
        onChangeQuantOrKilo(purchase.quantiOrKilo)
        onChangeDiscountCurrent(purchase.discount.toString())
        index.value = newIndex
        updateDate.value = true
    }

    override fun checkFields(disableError: Boolean): Boolean {

        if (!disableError) {
            productError.value = product.value!!.isBlank()

            priceError.value = price.value!!.isBlank()

            discountError.value = discount.value!!.isBlank()

            localeError.value = locale.value!!.isBlank()

            idCardError.value = creditCard.value!!.myShoppingId == -1L

            typeCategoryError.value = category.value == null

            quantOrKiloError.value =
                quantOrKilo.value!!.isBlank() || MaskUtils.replaceAll(quantOrKilo.value!!)
                    .toInt() == 0

        }

        if (product.value!!.isBlank()) return false

        if (price.value!!.isBlank()) return false

        if (quantOrKilo.value!!.isBlank()) return false

//        if (locale.value!!.isBlank()) return false

        if (creditCard.value!!.myShoppingId == -1L) return false

        if (category.value == null) return false

        if (quantOrKilo.value!!.isBlank() || MaskUtils.replaceAll(quantOrKilo.value!!)
                .toInt() == 0
        ) return false

        return true
    }


    override fun checkFields(): Boolean {

        return checkFields(false)
    }

    private fun updatePurchaseAndCategoryDTOCollection() {
        val auxPurchaseCollection = mutableListOf<PurchaseAndCategoryDTO>()

        auxPurchaseCollection.addAll(purchaseAndCategoryDTOCollection.value!!)

        purchaseAndCategoryDTOCollection.value!!.clear()

        purchaseAndCategoryDTOCollection.value = auxPurchaseCollection
    }

    fun removerPurchase(index: Int) {
        purchaseAndCategoryDTOCollection.value?.removeAt(index)
//            updatePurchaseInfoCollection()
        countProduct.value = purchaseAndCategoryDTOCollection.value!!.size
    }

    fun addPurchase() {

        val purchase = Purchase(
            product.value!!,
            locale.value!!,
            creditCard.value!!.myShoppingId,
            quantOrKilo.value!!,
            typeProduct.value!!,
            dateCurrent.value!!,
            MaskUtils.convertValueStringToDouble(
                price.value!!
            ),
            category.value!!,
            email,
            MaskUtils.convertValueStringToDouble(
                if (discount.value!!.isNotBlank()) {
                    discount.value!!
                } else {
                    "0.0"
                }
            )
        )

        val purchaseFormatData = PurchaseDTO(purchase)

        val categoryFormatData = CategoryDTO()
        categoryFormatData.toCategoryDTO(categoryCurrent.value!!)

        val purchaserAndCategory = PurchaseAndCategoryDTO(purchaseFormatData, categoryFormatData)

        if(index.value != -1){
            purchaseAndCategoryDTOCollection.value!!.set(index.value!!, purchaserAndCategory)
        }else{
            purchaseAndCategoryDTOCollection.value!!.add(purchaserAndCategory)
        }

        updatePurchaseAndCategoryDTOCollection()

        countProduct.value = purchaseAndCategoryDTOCollection.value!!.size

    }

    fun onChangeDiscountCurrent(discount: String) {
        this.discount.value = discount
    }

    fun onChangeCategoryCurrent(category: Category) {
        this.categoryCurrent.value = category
    }

    fun onChangeCategoryCollection(newCategoryCollection: List<Category>) {
        categoryCollection.value?.removeAll(newCategoryCollection)
        categoryCollection.value?.addAll(newCategoryCollection)
    }

    fun onChangeResetDate() {
        enableButtonAdd.value = false
        resetDate.value = true

        product.value = ""
        price.value = ""
        category.value = null
        index.value = -1
        quantOrKilo.value = ""
        discount.value = ""

        if (!isBlock.value!!) {
            locale.value = ""
            dateCurrent.value = FormatDateUtils().getDateFormatted(formatPtBR = false)
        }
    }

    fun onFieldResetData() {
        Handler().postDelayed({
            kotlin.run {
                resetDate.value = false
            }
        }, 1000)
    }

    fun onChangeCategory(newCategoty: Long) {
        category.value = newCategoty
        typeCategoryError.value = false
        enableButtonAdd.value = checkFields(true)

    }

    fun onChangeTypeProduct(newTypeProduct: TypeProduct) {
        typeProduct.value = newTypeProduct
    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
        productError.value = newProduct.isBlank()
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangePrice(newPrice: String) {
        price.value = newPrice
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangeQuantOrKilo(newQuantOrKilo: String) {
        quantOrKilo.value = newQuantOrKilo
        quantOrKiloError.value =
            newQuantOrKilo.isBlank() || MaskUtils.replaceAll(newQuantOrKilo).toInt() == 0
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangeLocale(newLocale: String) {
        locale.value = newLocale
        localeError.value = newLocale.isBlank()
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangeCreditCard(newCreditCard: CreditCard?) {
        creditCard.value = newCreditCard
        idCardError.value = newCreditCard!!.myShoppingId == -1L
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangeIsBlock(newIsBlock: Boolean) {
        isBlock.value = newIsBlock
        enableButtonAdd.value = checkFields(true)
    }

    fun onChangeDateCurrent(newDateCurrent: String) {
        dateCurrent.value = newDateCurrent
        enableButtonAdd.value = checkFields(true)
    }

    fun onResetUpdateData() {
        Handler().postDelayed({
            kotlin.run {
                updateDate.value = false
            }
        }, 1000)
    }

}