package com.example.myshoppinglist.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User
import com.google.gson.annotations.SerializedName

class UserWithCreditCards(
    @SerializedName("user")
    @Embedded val user: User,
    @SerializedName("creditCards")
    @Relation(parentColumn = "email", entityColumn = "cardUserId")val creditCards: List<CreditCard>
) {
    override fun toString(): String {
        return "UserWithCreditCards(user=$user, creditCards=$creditCards)"
    }
}