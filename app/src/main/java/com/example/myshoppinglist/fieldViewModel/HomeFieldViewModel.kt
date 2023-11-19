package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.utils.MountStructureCrediCard


class HomeFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    private val purchaseCollection: MutableLiveData<MutableList<PurchaseAndCategory>> =
        MutableLiveData(mutableListOf())
    private val creditCardCollection: MutableLiveData<MutableList<CreditCardDTODB>> =
        MutableLiveData(mutableListOf())
    private val idAvatar = MutableLiveData(0)
    private val nickName = MutableLiveData("")
    private var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getPurchaseCollection(): List<PurchaseAndCategory> {
        return purchaseCollection.value!!
    }

    fun getCreditCardCollection(): List<CreditCardDTODB> {
        return creditCardCollection.value!!
    }

    fun getIdAvatar(): Int {
        return idAvatar.value!!
    }

    fun getNickName(): String {
        return nickName.value!!
    }

    fun onChangeIdAvatar(newIdAvatar: Int) {
        idAvatar.value = newIdAvatar
    }

    fun onChangeNickName(newNickName: String) {
        nickName.value = newNickName
    }

    fun onChangeVisibleValue() {
        isVisibleValue.value = !isVisibleValue.value!!
    }

    fun getPurchaseController(): PurchaseController {
        return purchaseController
    }

    fun getCreditCardController(): CreditCardController {
        return creditCardController
    }

    fun onChangePurchaseCollection(newPurchaseCollection: List<PurchaseAndCategory>) {
        purchaseCollection.value!!.clear()
        purchaseCollection.value!!.addAll(newPurchaseCollection.reversed())
    }

    fun onChangeCreditCardCollection(newCreditCardCollection: List<CreditCard>) {
        creditCardCollection.value!!.clear()
        creditCardCollection.value!!.addAll(
            MountStructureCrediCard().mountSpedingDate(
                newCreditCardCollection
            )
        )
        creditCardCollection.value!!.add(CreditCardDTODB())

    }

    fun isVisibleValue(): Boolean {
        return isVisibleValue.value!!
    }

    fun getUser(email: String): LiveData<UserDTO> {
        return UserInstanceImpl.getUserViewModelCurrent().findUserByName(email)
    }

    fun onUpdateUser(email: String, lifecycleOwner: LifecycleOwner){
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(lifecycleOwner){
            onChangeNickName(it.nickName)
            onChangeIdAvatar(it.idAvatar)
        }
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

}