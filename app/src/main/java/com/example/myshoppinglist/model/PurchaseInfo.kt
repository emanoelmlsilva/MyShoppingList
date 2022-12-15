package com.example.myshoppinglist.model

import com.example.myshoppinglist.database.entities.Purchase

class PurchaseInfo {
    var avatar: Int
    var title: String
    var purchaseCollection: MutableList<Purchase>

    constructor(title: String, purchaseCollection: MutableList<Purchase>) {
        this.avatar = 0
        this.title = title
        this.purchaseCollection = purchaseCollection
    }

    constructor(title: String, avatar: Int, purchaseCollection: MutableList<Purchase>): this(title, purchaseCollection){
        this.avatar = avatar
    }

    override fun toString(): String {
        return "PurchaseInfo(avatar=$avatar, title='$title', purchaseCollection=$purchaseCollection) \n"
    }
}