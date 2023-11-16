package com.example.myshoppinglist.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface MyShoppingListService {

    companion object{
        private const val API_BASE_URL = "http://ec2-18-223-156-42.us-east-2.compute.amazonaws.com:8080/v1/api/"

        fun create() : Retrofit{
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder().addInterceptor(logger).build()

            return Retrofit.Builder().baseUrl(API_BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        }
    }

}