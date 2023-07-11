package com.example.myshoppinglist.services.repository

import ResultData

import com.example.myshoppinglist.services.CreditCardService
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreditCardRepository(private val creditCardService: CreditCardService) {

    private val TAG = "CreditCardRepository"

    suspend fun update(creditCardDTO: CreditCardDTO): ResultData<CreditCardDTO>{
        return withContext(Dispatchers.IO){
            val creditCardExecute = creditCardService.update(creditCardDTO).execute()

            return@withContext if(creditCardExecute.isSuccessful){
                val creditCardResponse = creditCardExecute.body()?:CreditCardDTO()

                ResultData.Success(creditCardResponse)
            }else{
                ResultData.Error(Exception("ERRROR UPDATE ${creditCardExecute.errorBody()}"))
            }
        }
    }

    suspend fun save(creditCardDTO: CreditCardDTO): ResultData<CreditCardDTO>{
        return withContext(Dispatchers.IO){
            val creditCardExecute = creditCardService.save(creditCardDTO).execute()

            return@withContext if(creditCardExecute.isSuccessful){
                val creditCardResponse = creditCardExecute.body()?:CreditCardDTO()
                ResultData.Success(creditCardResponse)
            }else{
                ResultData.Error(Exception("ERROR SAVE ${creditCardExecute.errorBody()}"))
            }
        }
    }

    suspend fun findAllCreditCard(email: String): ResultData<List<CreditCardDTO>>{
        return withContext(Dispatchers.IO){
            val creditCardExecute = creditCardService.findAll(email).execute()

            return@withContext if(creditCardExecute.isSuccessful){
                val creditCardResponse = creditCardExecute.body()?: listOf()
                ResultData.Success(creditCardResponse)
            }else{
                ResultData.Error(Exception("ERROR FIND ALL ${creditCardExecute.errorBody()}"))
            }
        }
    }
}