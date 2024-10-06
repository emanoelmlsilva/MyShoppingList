package com.example.myshoppinglist.services

import com.example.myshoppinglist.services.dtos.PurchaseDTOService
import retrofit2.Call
import retrofit2.http.*

interface PurchaseService {

    companion object{
        private val purchaseService = MyShoppingListService.create().create(PurchaseService::class.java)

        fun getPurchaseService(): PurchaseService{
            return purchaseService
        }
    }

    @DELETE("purchase/{id}")
    fun delete(@Path("id") id: Long): Call<PurchaseDTOService>
    
    @POST("purchase")
    fun save(@Body purchaseDTOService: PurchaseDTOService) : Call<PurchaseDTOService>

    @POST("purchase/{id_original}")
    fun save(@Path("id_original") id_original: Long, @Body purchaseDTOService: PurchaseDTOService) : Call<PurchaseDTOService>

    @PUT("purchase")
    fun update(@Body purchaseDTOService: PurchaseDTOService): Call<PurchaseDTOService>

    @GET("purchase/credit_card/{id}")
    fun findAllByCardId(@Path("id") id: Long) : Call<List<PurchaseDTOService>>

    @GET("purchase/repeat_date")
    fun findAllPurchaseRepeatDate() : Call<List<PurchaseDTOService>>

    @DELETE("purchase/repeat_purchase/{idPurchase}")
    fun removeRepeatPurchase(@Path("idPurchase") idPurchase: Long): Call<PurchaseDTOService>
}