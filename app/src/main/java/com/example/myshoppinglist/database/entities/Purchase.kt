package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.enums.TypeProduct
import org.jetbrains.annotations.NotNull

@Entity(tableName = "purchases")
class Purchase {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "idPruchase")
    var id: Long = 0
    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "locale")
    var locale: String = ""
    @ColumnInfo(name = "purchaseCardId")
    var purchaseCardId:Long = 0
    @ColumnInfo(name = "quantiOrKilo")
    var quantiOrKilo: String = ""
    @ColumnInfo(name = "typeProduct")
    var typeProduct: TypeProduct = TypeProduct.QUANTITY
    @ColumnInfo(name = "date")
    var date: String = "24-01-2022"
    @ColumnInfo(name = "price")
    var price: Double = 0.0
    @ColumnInfo(name = "categoryOwnerId")
    var categoryOwnerId: String = ""

    constructor(name: String){
        this.name = name
    }

    constructor(): super(){
    }

    constructor(
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        categoryOwnerId: String
    ) {
        this.name = name
        this.locale = locale
        this.purchaseCardId = purchaseCardId
        this.quantiOrKilo = quantiOrKilo
        this.typeProduct = typeProduct
        this.date = date
        this.price = price
        this.categoryOwnerId = categoryOwnerId
    }

    override fun toString(): String {
        return "Purchase(id=$id, name='$name', locale='$locale', purchaseCardId=$purchaseCardId, quantiOrKilo='$quantiOrKilo', typeProduct=$typeProduct, date=$date, price=$price, categoryOwnerId=$categoryOwnerId)"
    }

}