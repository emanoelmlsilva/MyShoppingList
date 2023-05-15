package com.example.myshoppinglist.services

import com.example.myshoppinglist.database.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    companion object{
        private val userService = MyShoppingListService.create().create(UserService::class.java)

        fun getUserService() : UserService{
            return userService
        }
    }

    @GET("user/{email}/{password}")
    fun findUser(@Path("email") email: String, @Path("password") password: String) : Call<User>

    @POST("user")
    fun save(@Body user: User): Call<User>

    @PUT("user")
    fun update(@Body user: User) : Call<User>
}