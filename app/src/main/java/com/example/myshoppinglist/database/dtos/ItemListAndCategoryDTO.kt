package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
class ItemListAndCategoryDTO() {
    lateinit var itemListDTO: ItemListDTO
    lateinit var categoryDTO: CategoryDTO

    constructor(itemListDTO: ItemListDTO, categoryDTO: CategoryDTO) : this() {
        this.itemListDTO = itemListDTO
        this.categoryDTO = categoryDTO
    }

    fun toItemListAndCategoryDTO(itemListAndCategoryCollection: List<ItemListAndCategory>): List<ItemListAndCategoryDTO> {
        val itemListAndCategoryListDTO: MutableList<ItemListAndCategoryDTO> = mutableListOf()

        itemListAndCategoryCollection.forEach {
            val itemListAndCategoryDTO = ItemListAndCategoryDTO()
            val categoryDTO = CategoryDTO()
            val itemListDTO = ItemListDTO()

            itemListDTO.toItemListDTO(it.itemList)
            categoryDTO.toCategoryDTO(it.category)

            itemListAndCategoryDTO.itemListDTO = itemListDTO
            itemListAndCategoryDTO.categoryDTO = categoryDTO

            itemListAndCategoryListDTO.add(itemListAndCategoryDTO)
        }

        return itemListAndCategoryListDTO
    }

    fun toItemListAndCategory(): ItemListAndCategory {
        return ItemListAndCategory(this.itemListDTO.toItemList(), this.categoryDTO.toCategory())
    }
}