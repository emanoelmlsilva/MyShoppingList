package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.CategoryDAO
import com.example.myshoppinglist.database.entities.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CategoryRepository(private val categoryDAO: CategoryDAO) {

    val searchCollectionResult = MutableLiveData<List<Category>>()
    val searchResult = MutableLiveData<Category>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCateogry(newCategory: Category) {
        coroutineScope.launch(Dispatchers.IO) {
            categoryDAO.insertCategory(newCategory)
        }
    }

    fun updateCategory(newCategory: Category) {
        coroutineScope.launch(Dispatchers.IO) {
            categoryDAO.updateCategory(newCategory)
        }
    }

    fun getCategoryById(nameUser: String, idCategory: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResult.value = asyncFindById(nameUser, idCategory)
        }
    }

    fun getCategoryByCategory(nameUser: String, category: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResult.value = asyncFindByCategory(nameUser, category)
        }
    }

    fun getAll(nameUser: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchCollectionResult.value = asyncFindAll(nameUser)
        }
    }

    private suspend fun asyncFindById(name: String, idCategory: Long): Category =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getById(idCategory)
        }.await()

    private suspend fun asyncFindByCategory(name: String, category: String): Category =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getByCategory(category)
        }.await()

    private suspend fun asyncFindAll(name: String): List<Category> =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getAll()
        }.await()
}