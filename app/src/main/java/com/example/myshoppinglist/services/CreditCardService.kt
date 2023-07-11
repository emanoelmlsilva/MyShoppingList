package com.example.myshoppinglist.services

import com.example.myshoppinglist.services.dtos.CreditCardDTO
import retrofit2.Call
import retrofit2.http.*

interface CreditCardService {

    companion object{
        private val creditCardService = MyShoppingListService.create().create(CreditCardService::class.java)

        fun getCreditCardService() : CreditCardService{
            return creditCardService
        }
    }

    @GET("credit_card/email/{email}")
    fun findAll(@Path("email") email: String) : Call<List<CreditCardDTO>>

    @POST("credit_card")
    fun save(@Body creditCardDTO: CreditCardDTO) : Call<CreditCardDTO>

    @PUT("credit_card")
    fun update(@Body creditCardDTO: CreditCardDTO) : Call<CreditCardDTO>
}