package com.example.myshoppinglist.database.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.card_blue
import org.burnoutcrew.reorderable.ItemPosition

class CreateCardCreditFieldViewModel : BaseFieldViewModel() {
    var name: MutableLiveData<String> = MutableLiveData("")
    var nameCard: MutableLiveData<String> = MutableLiveData("")
    var colorCurrent: MutableLiveData<Color> = MutableLiveData(card_blue)
    var isErrorName: MutableLiveData<Boolean> = MutableLiveData(false)
    var isErrorNameCard: MutableLiveData<Boolean> = MutableLiveData(false)
    var flagCurrent: MutableLiveData<Int> = MutableLiveData(CardCreditFlag.MONEY.flag)
    var creditCardCollection: MutableLiveData<MutableList<CreditCardDTO>> =
        MutableLiveData(mutableListOf())
    var value: MutableLiveData<Float> = MutableLiveData(0F)
    var typeCard: MutableLiveData<TypeCard> = MutableLiveData(TypeCard.MONEY)
    var idCreditCard: MutableLiveData<Long> = MutableLiveData()
    var lastPosition: MutableLiveData<Int> = MutableLiveData(0)

    fun updateCreditCardCollection(newTesteList: MutableList<CreditCardDTO>) {
        creditCardCollection.value = newTesteList
    }

    fun onTaskReordered(
        creditCardViewModel: CreditCardViewModel,
        creditCardCollectionUpdate: MutableList<CreditCardDTO>,
        fromPos: ItemPosition,
        toPos: ItemPosition
    ) {
        val auxCreditCardCollection = ArrayList(creditCardCollection.value!!)
        val positionFrom = creditCardCollection.value!![fromPos.index].position
        val positionTo = creditCardCollection.value!![toPos.index].position

        auxCreditCardCollection[fromPos.index].position = positionTo
        auxCreditCardCollection[toPos.index].position = positionFrom

        auxCreditCardCollection[fromPos.index] = creditCardCollectionUpdate[toPos.index]
        auxCreditCardCollection[toPos.index] = creditCardCollectionUpdate[fromPos.index]

        creditCardViewModel.updateCreditCard(auxCreditCardCollection[fromPos.index].toCreditCard())
        creditCardViewModel.updateCreditCard(auxCreditCardCollection[toPos.index].toCreditCard())

        updateCreditCardCollection(auxCreditCardCollection)
    }

    fun onChangeLastPosition(newLastPosition: Int){
        lastPosition.value = newLastPosition
    }

    fun onChangeIdCreditCard(newIdCreditCard: Long){
        idCreditCard.value = newIdCreditCard
    }

    fun onChangeTypeCard(newTypeCard: TypeCard){
        typeCard.value = newTypeCard
    }

    fun onChangeValue(newValue: Float){
        value.value = newValue
    }

    fun onChangeFlagCurrent(newFlagCurrent: Int) {
        flagCurrent.value = newFlagCurrent
    }

    fun onChangeColorCurrent(newColorCurrent: Color) {
        colorCurrent.value = newColorCurrent
    }

    fun onChangeName(newName: String) {
        onChangeIsErrorName(newName.isBlank())
        name.value = newName
    }

    fun onChangeNameCard(newNameCard: String) {
        onChangeIsErrorNameCard(newNameCard.isBlank())
        nameCard.value = newNameCard
    }

    fun onChangeIsErrorName(newIsError: Boolean) {
        isErrorName.value = newIsError
    }

    fun onChangeIsErrorNameCard(newIsError: Boolean) {
        isErrorNameCard.value = newIsError
    }

    override fun checkFileds(): Boolean {
        if (name.value!!.isBlank()) {
            onChangeIsErrorName(true)
            return false
        }

        if (nameCard.value!!.isBlank()) {
            onChangeIsErrorNameCard(true)
            return false
        }

        return true
    }
}