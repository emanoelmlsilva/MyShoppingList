package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.dtos.ItemListAndCategoryDTO
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type


object ConversionUtils {

    val type: Type = Types.newParameterizedType(List::class.java, ItemListAndCategoryDTO::class.java)

    @JvmStatic
    fun toJson(itemListCategoryCollection: List<ItemListAndCategoryDTO>): String{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<ItemListAndCategoryDTO>> = moshi.adapter(type)

        return jsonAdapter.toJson(itemListCategoryCollection)
    }

    @JvmStatic
    fun fromJson(itemListCategoryJson: String): List<ItemListAndCategoryDTO>? {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<ItemListAndCategoryDTO>> = moshi.adapter(type)
        return jsonAdapter.fromJson(itemListCategoryJson)
    }
}