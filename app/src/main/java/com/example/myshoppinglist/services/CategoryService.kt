package com.example.myshoppinglist.services

import com.example.myshoppinglist.services.dtos.CategoryDTO
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    companion object{
        private val categoryService = MyShoppingListService.create().create(CategoryService::class.java)

        fun getCategoryService() : CategoryService{
            return categoryService
        }
    }

    @GET("category/email/{email}")
    fun findAll(@Path("email") email: String) : Call<List<CategoryDTO>>

    @POST("category")
    fun save(@Body categoryDTO: CategoryDTO) : Call<CategoryDTO>

    @PUT("category")
    fun update(@Body categoryDTO: CategoryDTO) : Call<CategoryDTO>
}