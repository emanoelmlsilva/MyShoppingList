package com.example.myshoppinglist.model

import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory

class PurchaseAndCategoryInfo {
    var avatar: Int
    var title: String
    var purchaseCollection: MutableList<PurchaseAndCategory> = mutableListOf()

    constructor(title: String, purchaseCollection: MutableList<PurchaseAndCategory>) {
        this.avatar = 0
        this.title = title
        this.purchaseCollection = purchaseCollection
    }

    constructor(title: String, avatar: Int, purchaseCollection: MutableList<PurchaseAndCategory>): this(title, purchaseCollection){
        this.avatar = avatar
    }

    override fun toString(): String {
        return "PurchaseInfo(avatar=$avatar, title='$title', purchaseCollection=$purchaseCollection) \n"
    }
}