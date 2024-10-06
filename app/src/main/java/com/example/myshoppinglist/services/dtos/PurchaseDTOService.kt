package com.example.myshoppinglist.services.dtos

import androidx.room.ColumnInfo
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.*
import com.example.myshoppinglist.enums.TypeFrequencyRepeat
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.utils.FormatDateUtils
import com.google.gson.annotations.SerializedName
import java.util.*

class PurchaseDTOService() {

    @SerializedName("myShoppingId")
    var myShoppingId: Long = 0
    @SerializedName("name")
    lateinit var name: String
    @SerializedName("locale")
    lateinit var locale: String
    @SerializedName("creditCard")
    lateinit var creditCard: CreditCardDTO
    @SerializedName("amount_or_kilo")
    lateinit var amount_or_kilo: String
    @SerializedName("type_product")
    var type_product: Int = 0
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
    @ColumnInfo(name = "is_repeat")
    var is_repeat: Boolean = false
    @ColumnInfo(name = "date_repeat")
    var date_repeat: String = ""
    @ColumnInfo(name = "frequency_repeat")
    var frequency_repeat: TypeFrequencyRepeat = TypeFrequencyRepeat.NEVER
    @ColumnInfo(name = "idOriginal")
    var idOriginal: Long = 0

    constructor(purchase: Purchase) : this() {
        this.idPurchase = purchase.idPurchaseApi
        this.myShoppingId = purchase.myShoppingId
        this.name = purchase.name
        this.locale = purchase.locale
        this.amount_or_kilo = purchase.amountOrKilo
        this.type_product = purchase.typeProduct.ordinal
        this.date = purchase.date
        this.price = purchase.price
        this.discount = purchase.discount
        this.is_repeat = purchase.isRepeat
        this.date_repeat = purchase.dateRepeat
        this.frequency_repeat = purchase.frequencyRepeat
    }

    constructor(purchase: Purchase, category: Category, creditCard: CreditCard) : this(purchase) {
        val categoryDTO = CategoryDTO()
        categoryDTO.toCategoryDTO(category)

        val creditCardDTODB = CreditCardDTODB().fromCreditCardDTODB(creditCard)

        this.category = categoryDTO
        this.creditCard = creditCardDTODB.fromCreditCardDTO()
    }


    fun toPurchase() : Purchase{
        return Purchase(idPurchase, myShoppingId, name, locale, creditCard.idCard, amount_or_kilo, TypeProduct.typeProductId(type_product), date, price, category.myShoppingId, creditCard.userDTO.email, discount, is_repeat, date_repeat, frequency_repeat)
    }

    fun toPurchaseApi() : Purchase {
        val purchase = toPurchase()
        purchase.categoryOwnerId = this.category.id
        purchase.purchaseCardId = this.creditCard.id

        purchase.idPurchaseApi = this.idPurchase
        purchase.myShoppingId = this.myShoppingId
        return purchase
    }

    fun toPurchaseRepeatData() {
        this.idOriginal = this.idPurchase
        this.is_repeat = false
        this.myShoppingId = 0
        this.date_repeat = ""
        this.idPurchase = 0
        this.frequency_repeat = TypeFrequencyRepeat.NEVER
        this.date = FormatDateUtils().getDateFormatted(Date(), false)
    }
}