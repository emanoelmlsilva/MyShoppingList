package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.entities.CreditCard
import java.util.HashMap

object CardUtils{

    @JvmStatic
    fun getNameCard(creditCardColelction: List<CreditCard>): HashMap<String, Long> {
        val cardCreditFormated: HashMap<String, Long> = HashMap<String, Long> ()

        cardCreditFormated.put("Cartões" , -1)

        creditCardColelction.forEachIndexed { index, creditCard ->
            cardCreditFormated.put(
                creditCard.cardName,
                creditCard.id
            )
        }

        return cardCreditFormated.entries.sortedBy { it.value }.associate { it.toPair() } as HashMap<String, Long>

    }

}