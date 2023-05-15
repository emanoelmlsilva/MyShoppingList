package com.example.myshoppinglist.services

import com.example.myshoppinglist.database.entities.relations.UserWithCreditCard
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
    fun findAll(@Path("email") email: String) : Call<List<UserWithCreditCard>>

    @POST("credit_card")
    fun save(@Body userWithCreditCard: UserWithCreditCard) : Call<UserWithCreditCard>

    @PUT("credit_card")
    fun update(@Body userWithCreditCard: UserWithCreditCard) : Call<UserWithCreditCard>
}