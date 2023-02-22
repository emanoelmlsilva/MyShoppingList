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

    fun getCategoryById(emailUser: String, idCategory: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResult.value = asyncFindById(emailUser, idCategory)
        }
    }

    fun getCategoryByCategory(emailUser: String, category: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResult.value = asyncFindByCategory(emailUser, category)
        }
    }

    fun getAll(emailUser: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchCollectionResult.value = asyncFindAll(emailUser)
        }
    }

    private suspend fun asyncFindById(email: String, idCategory: Long): Category =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getById(idCategory, email)
        }.await()

    private suspend fun asyncFindByCategory(email: String, category: String): Category =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getByCategory(category, email)
        }.await()

    private suspend fun asyncFindAll(email: String): List<Category> =
        coroutineScope.async(Dispatchers.IO) {
            return@async categoryDAO.getAll(email)
        }.await()
}