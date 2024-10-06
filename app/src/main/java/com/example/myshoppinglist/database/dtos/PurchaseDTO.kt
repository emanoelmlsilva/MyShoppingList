package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeFrequencyRepeat
import com.example.myshoppinglist.enums.TypeProduct

class PurchaseDTO() {

    var myShoppingId: Long = 0
    var name: String = ""
    var locale: String = ""
    var purchaseCardId: Long = 0
    var amountOrKilo: String = ""
    var typeProduct: TypeProduct = TypeProduct.QUANTITY
    var date: String = ""
    var price: Double = 0.0
    var categoryOwnerId: Long = 0
    var purchaseUserId: String = ""
    var idMyShoppingApi: Long = 0
    var discount: Double = 0.0
    var isRepeat: Boolean = false
    var dateRepeat: String = ""
    var frequencyRepeat: TypeFrequencyRepeat = TypeFrequencyRepeat.NEVER
    var isSynchronized: Boolean = false

    constructor(purchase: Purchase) : this() {
        this.idMyShoppingApi = purchase.idPurchaseApi
        this.myShoppingId = purchase.myShoppingId
        this.name = purchase.name
        this.locale = purchase.locale
        this.purchaseCardId = purchase.purchaseCardId
        this.amountOrKilo = purchase.amountOrKilo
        this.typeProduct = purchase.typeProduct
        this.date = purchase.date
        this.price = purchase.price
        this.categoryOwnerId = purchase.categoryOwnerId
        this.purchaseUserId = purchase.purchaseUserId
        this.discount = purchase.discount
        this.isSynchronized = purchase.isSynchronized
        this.isRepeat = purchase.isRepeat
        this.dateRepeat = purchase.dateRepeat
        this.frequencyRepeat = purchase.frequencyRepeat
    }

    fun toPurchase(email: String) : Purchase{
        return Purchase(idMyShoppingApi, myShoppingId, name, locale, purchaseCardId, amountOrKilo, typeProduct, date, price, categoryOwnerId, email, discount, isRepeat, dateRepeat, frequencyRepeat, isSynchronized)
    }
}