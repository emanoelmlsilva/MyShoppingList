package com.example.myshoppinglist.model

import com.example.myshoppinglist.database.entities.Purchase

class PurchaseInfo {
    var title: String
    var purchaseCollection: MutableList<Purchase>

    constructor(title: String, purchaseCollection: MutableList<Purchase>) {
        this.title = title
        this.purchaseCollection = purchaseCollection
    }
}