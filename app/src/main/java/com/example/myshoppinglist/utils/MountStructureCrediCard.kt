package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.CreditCard

class MountStructureCrediCard {

    fun mountSpedingDate(creditCardCollection: List<CreditCard>): List<CreditCardDTODB> {
        val MAX_ITEM = 3
        var countIndex = 0
        val SIZE_COLLECTION = creditCardCollection.size
        var spedingCollection = mutableListOf<CreditCardDTODB>()
        val spedingDateCollection = mutableListOf<CreditCardDTODB>()

        if (creditCardCollection.isEmpty()) return spedingDateCollection

        var countSizeCollection = 0

        while(countSizeCollection < SIZE_COLLECTION){
            val creditCard = creditCardCollection[countSizeCollection]

            spedingCollection.add(CreditCardDTODB().fromCreditCardDTODB(creditCard))
            countSizeCollection++
            countIndex++

            if (countIndex == MAX_ITEM || (MAX_ITEM > SIZE_COLLECTION && countIndex == SIZE_COLLECTION) || (countSizeCollection == SIZE_COLLECTION)) {
                spedingDateCollection.addAll(spedingCollection)
                spedingCollection = mutableListOf()
                countIndex = 0

            }
        }

        return spedingDateCollection
    }
}