package com.example.myshoppinglist.database.dtos

class PurchaseAndCategoryDTO(val purchaseDTO: PurchaseDTO, val categoryDTO: CategoryDTO) {

    var dateFormat = ""
    var priceFormat = ""
    var discountFormat = ""
    var totalWithoutDiscountFormat = ""

}