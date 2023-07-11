package com.example.myshoppinglist.model

import com.example.myshoppinglist.database.dtos.CreditCardDTODB

class CardCreditFilter(){
    var id: Long = 0
    var nickName: String = ""
    var avatar: Int = 0

    constructor(id: Long, nickName: String, avatar: Int) : this() {
        this.id = id
        this.nickName = nickName
        this.avatar = avatar
    }

    constructor(creditCardDTO: CreditCardDTODB?) : this() {
        if(creditCardDTO != null){
            this.id = creditCardDTO.myShoppingId
            this.nickName = creditCardDTO.cardName
            this.avatar = creditCardDTO.flag
        }

    }

    override fun toString(): String {
        return "CardCreditFilter(id=$id, nickName='$nickName', avatar=$avatar)"
    }
}