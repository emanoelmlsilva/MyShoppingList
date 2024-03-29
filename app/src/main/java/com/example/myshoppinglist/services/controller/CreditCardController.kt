package com.example.myshoppinglist.services.controller

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
import com.example.myshoppinglist.services.CreditCardService
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.services.repository.CreditCardRepository
import com.example.myshoppinglist.services.viewModel.CreditCardViewModel

class CreditCardController {

    private val TAG = "CreditCardController"

    companion object {
        private lateinit var creditCardViewModel: CreditCardViewModel

        fun getData(context: Context, lifecycleOwner: LifecycleOwner): CreditCardController {
            creditCardViewModel = CreditCardViewModel(
                CreditCardRepository(CreditCardService.getCreditCardService()),
                CreditCardViewModelDB(context, lifecycleOwner)
            )

            return CreditCardController()
        }
    }

    fun getAllWithSumDB(): LiveData<List<CreditCard>> {
        return creditCardViewModel.getAllWithSum()
    }

    fun findCreditCardByIdDB(idCard: Long): LiveData<CreditCard>{
        return creditCardViewModel.findCreditCardById(idCard)
    }

    fun findAllDB(): LiveData<List<CreditCard>>{
        return creditCardViewModel.getAll()
    }

    fun saveAllCreditCard(email: String, callback: CallbackObject<List<CreditCardDTO>>){
        creditCardViewModel.findAndSaveAllCreditCard(email, callback)
    }

    fun updateCreditCardDB(creditCard: CreditCard){
        creditCardViewModel.updateCreditCardDB(creditCard)
    }
}