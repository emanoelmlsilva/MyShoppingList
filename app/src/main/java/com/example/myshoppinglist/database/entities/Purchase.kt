package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.enums.TypeProduct
import com.google.gson.annotations.SerializedName

@Entity(tableName = "purchases")
class Purchase {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "myShoppingId")
    var myShoppingId: Long = 0

    @SerializedName("idMyShoppingApi")
    @ColumnInfo(name = "idPurchaseApi")
    var idPurchaseApi: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "locale")
    var locale: String = ""

    @ColumnInfo(name = "purchaseCardId")
    var purchaseCardId: Long = 0

    @ColumnInfo(name = "quantiOrKilo")
    var quantiOrKilo: String = ""

    @ColumnInfo(name = "typeProduct")
    var typeProduct: TypeProduct = TypeProduct.QUANTITY

    @ColumnInfo(name = "date")
    var date: String = "24-01-2022"

    @ColumnInfo(name = "price")
    var price: Double = 0.0

    @ColumnInfo(name = "categoryOwnerId")
    var categoryOwnerId: Long = 0

    @ColumnInfo(name = "purchaseUserId")
    var purchaseUserId: String = ""

    @ColumnInfo(name = "discount")
    var discount: Double = 0.0

    constructor(name: String) {
        this.name = name
    }

    constructor() : super() {
    }

    constructor(
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        categoryOwnerId: Long,
        purchaseUserId: String
    ) {
        this.name = name
        this.locale = locale
        this.purchaseCardId = purchaseCardId
        this.quantiOrKilo = quantiOrKilo
        this.typeProduct = typeProduct
        this.date = date
        this.price = price
        this.categoryOwnerId = categoryOwnerId
        this.purchaseUserId = purchaseUserId
    }

    constructor(
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        categoryOwnerId: Long,
        purchaseUserId: String,
        discount: Double
    ) : this(
        name,
        locale,
        purchaseCardId,
        quantiOrKilo,
        typeProduct,
        date,
        price,
        categoryOwnerId,
        purchaseUserId
    ) {
        this.discount = discount
    }

    constructor(
        idMyShoppingApi: Long,
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        categoryOwnerId: Long,
        purchaseUserId: String,
        discount: Double
    ) {
        this.idPurchaseApi = idMyShoppingApi
        this.name = name
        this.locale = locale
        this.purchaseCardId = purchaseCardId
        this.quantiOrKilo = quantiOrKilo
        this.typeProduct = typeProduct
        this.date = date
        this.price = price
        this.categoryOwnerId = categoryOwnerId
        this.purchaseUserId = purchaseUserId
        this.discount = discount
    }

    constructor(
        idMyShoppingApi: Long,
        myShoppingId: Long,
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        categoryOwnerId: Long,
        purchaseUserId: String,
        discount: Double
    ) : this(
        idMyShoppingApi,
        name,
        locale,
        purchaseCardId,
        quantiOrKilo,
        typeProduct,
        date,
        price,
        categoryOwnerId,
        purchaseUserId,
        discount
    ) {
        this.myShoppingId = myShoppingId
    }

    override fun toString(): String {
        return "Purchase(myShoppingId=$myShoppingId, name='$name', locale='$locale', purchaseCardId=$purchaseCardId, quantiOrKilo='$quantiOrKilo', typeProduct=$typeProduct, date=$date, price=$price, categoryOwnerId=$categoryOwnerId)"
    }

    fun toDTO(purchaseDTO: PurchaseDTO) {
        this.idPurchaseApi = purchaseDTO.idMyShoppingApi
        this.myShoppingId = purchaseDTO.myShoppingId
        this.name = purchaseDTO.name
        this.locale = purchaseDTO.locale
        this.purchaseCardId = purchaseDTO.purchaseCardId
        this.quantiOrKilo = purchaseDTO.quantiOrKilo
        this.typeProduct = purchaseDTO.typeProduct
        this.date = purchaseDTO.date
        this.price = purchaseDTO.price
        this.categoryOwnerId = purchaseDTO.categoryOwnerId
        this.purchaseUserId = purchaseDTO.purchaseUserId
        this.discount = purchaseDTO.discount
    }
}