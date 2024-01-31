package com.example.myshoppinglist.database.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import org.burnoutcrew.reorderable.ItemPosition

class SettingsFieldViewModel : ViewModel() {

    var creditCardCollection: MutableLiveData<MutableList<CreditCardDTODB>> =
        MutableLiveData(mutableListOf())

    fun updateCreditCardCollection(newTesteList: MutableList<CreditCardDTODB>) {
        creditCardCollection.value = newTesteList
    }

    fun onTaskReordered(
        creditCardCollectionUpdate: MutableList<CreditCardDTODB>,
        fromPos: ItemPosition,
        toPos: ItemPosition
    ) {
        if(creditCardCollection.value!!.size > 0){
            val auxCreditCardCollection = ArrayList(creditCardCollection.value!!)
            val positionFrom = creditCardCollection.value!![fromPos.index].position
            val positionTo = creditCardCollection.value!![toPos.index].position

            auxCreditCardCollection[fromPos.index].position = positionTo
            auxCreditCardCollection[toPos.index].position = positionFrom

            auxCreditCardCollection[fromPos.index] = creditCardCollectionUpdate[toPos.index]
            auxCreditCardCollection[toPos.index] = creditCardCollectionUpdate[fromPos.index]

            updateCreditCardCollection(auxCreditCardCollection)
        }

    }
}