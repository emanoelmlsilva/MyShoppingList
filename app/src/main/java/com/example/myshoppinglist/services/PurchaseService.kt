package com.example.myshoppinglist.services

import com.example.myshoppinglist.services.dtos.PurchaseDTO
import retrofit2.Call
import retrofit2.http.*

interface PurchaseService {

    companion object{
        private val purchaseService = MyShoppingListService.create().create(PurchaseService::class.java)

        fun getPurchaseService(): PurchaseService{
            return purchaseService
        }
    }

    @POST("purchase")
    fun save(@Body purchaseDTO: PurchaseDTO) : Call<PurchaseDTO>

    @PUT("purchase")
    fun update(@Body purchaseDTO: PurchaseDTO): Call<PurchaseDTO>

    @GET("purchase/credit_card/{id}")
    fun findAllByCardId(@Path("id") id: Long) : Call<List<PurchaseDTO>>
}