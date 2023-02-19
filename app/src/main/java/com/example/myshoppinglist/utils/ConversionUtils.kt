package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.dtos.ItemListAndCategoryDTO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type


class ConversionUtils<T>(type: Type = ItemListAndCategoryDTO::class.java) {

    val type: Type = Types.newParameterizedType(List::class.java, type)



    fun toJson(itemListCollection: List<T>): String{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(type)

        return jsonAdapter.toJson(itemListCollection)
    }


    fun fromJson(itemListJson: String): List<T>? {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(type)
        return jsonAdapter.fromJson(itemListJson)
    }
}