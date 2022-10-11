package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard

class MountStructureCrediCard {

    fun mountSpedingDate(creditCardCollection: List<CreditCard>): List<List<CreditCardDTO>> {
        val MAX_ITEM = 3
        var countIndex = 0
        val SIZE_COLLECTION = creditCardCollection.size
        var spedingCollection = mutableListOf<CreditCardDTO>()
        val spedingDateCollection = mutableListOf<List<CreditCardDTO>>()

        if (creditCardCollection.isEmpty()) return spedingDateCollection

        var countSizeCollection = 0

        while(countSizeCollection < SIZE_COLLECTION){
            val creditCard = creditCardCollection[countSizeCollection]

            spedingCollection.add(CreditCardDTO().fromCreditCardDTO(creditCard))
            countSizeCollection++
            countIndex++

            if (countIndex == MAX_ITEM || (MAX_ITEM > SIZE_COLLECTION && countIndex == SIZE_COLLECTION) || (countSizeCollection == SIZE_COLLECTION)) {
                spedingDateCollection.add(spedingCollection)
                spedingCollection = mutableListOf()
                countIndex = 0

            }
        }

        return spedingDateCollection
    }
}