package com.example.myshoppinglist.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.myshoppinglist.database.dtos.PurchaseAndCategoryDTO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.ui.theme.background_card_gray_light

class PurchaseInfo {
    var color: Color
    var avatar: String
    var title: String
    var value: Double = 0.0
    var purchaseCollection: MutableList<PurchaseAndCategoryDTO> = mutableListOf()

    constructor(){
        this.avatar = ""
        this.title = ""
        this.color = background_card_gray_light
        this.value = 0.0
    }

    constructor(title: String, purchaseCollection: MutableList<PurchaseAndCategoryDTO>) {
        this.avatar = ""
        this.title = title
        this.purchaseCollection = purchaseCollection
        this.color = background_card_gray_light
        this.value = 0.0
    }

    constructor(title: String, avatar: String, purchaseCollection: MutableList<PurchaseAndCategoryDTO>): this(title, purchaseCollection){
        this.avatar = avatar
    }

    constructor(title: String, avatar: String, color: Color, purchaseCollection: MutableList<PurchaseAndCategoryDTO>): this(title, avatar, purchaseCollection){
        this.color = color
    }

    constructor(title: String, avatar: String, value: Double, color: Color, purchaseCollection: MutableList<PurchaseAndCategoryDTO>): this(title, avatar, color, purchaseCollection){
        this.value = value
    }

    override fun toString(): String {
        return "PurchaseInfo(color=$color, avatar='$avatar', title='$title', value=$value, purchaseCollection=$purchaseCollection)"
    }

}