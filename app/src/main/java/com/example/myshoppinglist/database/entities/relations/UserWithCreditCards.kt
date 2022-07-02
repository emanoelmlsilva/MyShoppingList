package com.example.myshoppinglist.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.User

class UserWithCreditCards(
    @Embedded val user: User,
    @Relation(parentColumn = "userId", entityColumn = "cardUserId")val creditCards: List<CreditCard>
) {
    override fun toString(): String {
        return "UserWithCreditCards(user=$user, creditCards=$creditCards)"
    }
}