package com.example.myshoppinglist.utils

import com.example.myshoppinglist.database.dtos.ItemListAndCategoryDTO
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ConversionUtils<T>(type: Type = ItemListAndCategoryDTO::class.java) {

    private val typeCurrent: Type = Types.newParameterizedType(PurchaseDTO::class.java, type)
    private val typeList: Type = Types.newParameterizedType(List::class.java, type)

    fun toJson(item: T): String{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(typeCurrent)

        return jsonAdapter.toJson(item)
    }

    fun fromJson(itemJson: String): T? {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(typeCurrent)
        return jsonAdapter.fromJson(itemJson)
    }

    fun toJsonList(itemListCollection: List<T>): String{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(typeList)

        return jsonAdapter.toJson(itemListCollection)
    }


    fun fromJsonList(itemListJson: String): List<T>? {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(typeList)
        return jsonAdapter.fromJson(itemListJson)
    }

    fun getGenericTypeClass(): Class<T> {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val typeArgs = type.actualTypeArguments
            if (typeArgs.isNotEmpty()) {
                return typeArgs[0] as Class<T>
            }
        }
        throw IllegalStateException("Unable to determine the generic type.")
    }
}