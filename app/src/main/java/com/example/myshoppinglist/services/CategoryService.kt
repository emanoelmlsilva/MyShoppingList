package com.example.myshoppinglist.services

import com.example.myshoppinglist.database.entities.relations.UserWithCategory
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
    fun findAll(@Path("email") email: String) : Call<List<UserWithCategory>>

    @POST("category")
    fun save(@Body userWithCategory: UserWithCategory) : Call<UserWithCategory>

    @PUT("category")
    fun update(@Body userWithCategory: UserWithCategory) : Call<UserWithCategory>
}