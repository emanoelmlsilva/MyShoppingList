package com.example.myshoppinglist.services.dtos

import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.*
import com.example.myshoppinglist.enums.TypeProduct
import com.google.gson.annotations.SerializedName

class PurchaseDTO() {

    @SerializedName("myShoppingId")
    var myShoppingId: Long = 0
    @SerializedName("name")
    lateinit var name: String
    @SerializedName("locale")
    lateinit var locale: String
    @SerializedName("creditCard")
    lateinit var creditCard: CreditCardDTO
    @SerializedName("quantiOrKilo")
    lateinit var quantiOrKilo: String
    @SerializedName("typeProduct")
    var typeProduct: Int = 0
    @SerializedName("date")
    lateinit var date: String
    @SerializedName("price")
    var price: Double = 0.0
    @SerializedName("category")
    lateinit var category: CategoryDTO
    @SerializedName("id")
    var idPurchase: Long = 0
    @SerializedName("discount")
    var discount: Double = 0.0

    constructor(purchase: Purchase) : this() {

        this.idPurchase = purchase.idPurchaseApi
        this.myShoppingId = purchase.myShoppingId
        this.name = purchase.name
        this.locale = purchase.locale
        this.quantiOrKilo = purchase.quantiOrKilo
        this.typeProduct = purchase.typeProduct.ordinal
        this.date = purchase.date
        this.price = purchase.price
        this.discount = purchase.discount
    }

    constructor(purchase: Purchase, category: Category, creditCard: CreditCard) : this(purchase) {
        val categoryDTO = CategoryDTO()
        categoryDTO.toCategoryDTO(category)

        val creditCardDTODB = CreditCardDTODB().fromCreditCardDTODB(creditCard)

        this.category = categoryDTO
        this.creditCard = creditCardDTODB.fromCreditCardDTO()
    }


    fun toPurchase() : Purchase{
        return Purchase(idPurchase, myShoppingId, name, locale, creditCard.idCard, quantiOrKilo, TypeProduct.typeProductId(typeProduct), date, price, category.myShoppingId, category.userDTO.email, discount)
    }

    fun toPurchaseApi() : Purchase {
        val purchase = toPurchase()
        purchase.categoryOwnerId = this.category.id
        purchase.purchaseCardId = this.creditCard.id

        purchase.idPurchaseApi = this.idPurchase
        purchase.myShoppingId = this.myShoppingId
        return purchase
    }
}