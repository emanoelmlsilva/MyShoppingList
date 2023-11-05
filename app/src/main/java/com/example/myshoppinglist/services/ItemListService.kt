package com.example.myshoppinglist.services

import com.example.myshoppinglist.services.dtos.ItemListDTO
import retrofit2.Call
import retrofit2.http.*

interface ItemListService {

    companion object{
        private val ItemListService = MyShoppingListService.create().create(ItemListService::class.java)

        fun getItemPurchaseService() : ItemListService{
            return ItemListService
        }
    }

    @POST("item_list")
    fun save(@Body itemListDTO: ItemListDTO) : Call<ItemListDTO>

    @PUT("item_list")
    fun update(@Body itemListDTO: ItemListDTO) : Call<ItemListDTO>

    @GET("item_list/credit_card/{id}")
    fun findAllByCardId(@Path("id") id: Long) : Call<List<ItemListDTO>>
}