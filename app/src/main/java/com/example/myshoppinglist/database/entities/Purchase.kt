package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import org.jetbrains.annotations.NotNull
import java.util.*

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
    @ColumnInfo(name = "category")
    var category: TypeCategory = TypeCategory.DRINKS

    constructor(
        name: String,
        locale: String,
        purchaseCardId: Long,
        quantiOrKilo: String,
        typeProduct: TypeProduct,
        date: String,
        price: Double,
        category: TypeCategory
    ) {
        this.name = name
        this.locale = locale
        this.purchaseCardId = purchaseCardId
        this.quantiOrKilo = quantiOrKilo
        this.typeProduct = typeProduct
        this.date = date
        this.price = price
        this.category = category
    }

    override fun toString(): String {
        return "Purchase(id=$id, name='$name', locale='$locale', purchaseCardId=$purchaseCardId, quantiOrKilo='$quantiOrKilo', typeProduct=$typeProduct, date=$date, price=$price, category=$category)"
    }


}