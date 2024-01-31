package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeProduct

class PurchaseDTO() {

    var myShoppingId: Long = 0
    lateinit var name: String
    lateinit var locale: String
    var purchaseCardId: Long = 0
    lateinit var quantiOrKilo: String
    lateinit var typeProduct: TypeProduct
    lateinit var date: String
    var price: Double = 0.0
    var categoryOwnerId: Long = 0
    lateinit var purchaseUserId: String
    var idMyShoppingApi: Long = 0
    var discount: Double = 0.0

    constructor(purchase: Purchase) : this() {
        this.idMyShoppingApi = purchase.idPurchaseApi
        this.myShoppingId = purchase.myShoppingId
        this.name = purchase.name
        this.locale = purchase.locale
        this.purchaseCardId = purchase.purchaseCardId
        this.quantiOrKilo = purchase.quantiOrKilo
        this.typeProduct = purchase.typeProduct
        this.date = purchase.date
        this.price = purchase.price
        this.categoryOwnerId = purchase.categoryOwnerId
        this.purchaseUserId = purchase.purchaseUserId
        this.discount = purchase.discount
    }

    fun toPurchase(email: String) : Purchase{
        return Purchase(idMyShoppingApi, myShoppingId, name, locale, purchaseCardId, quantiOrKilo, typeProduct, date, price, categoryOwnerId, email, discount)
    }
}