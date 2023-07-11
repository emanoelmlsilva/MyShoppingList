package com.example.myshoppinglist.services

import com.example.myshoppinglist.database.dtos.UserDTO
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
    fun findUser(@Path("email") email: String, @Path("password") password: String) : Call<UserDTO>

    @POST("user")
    fun save(@Body user: UserDTO): Call<UserDTO>

    @PUT("user")
    fun update(@Body userDTO: UserDTO) : Call<UserDTO>
}