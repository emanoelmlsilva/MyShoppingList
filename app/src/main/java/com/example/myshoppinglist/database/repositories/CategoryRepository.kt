package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.daos.CategoryDAO
import com.example.myshoppinglist.database.entities.Category
import kotlinx.coroutines.*

class CategoryRepository(private val categoryDAO: CategoryDAO){

    val searchCollectionResult = MutableLiveData<List<Category>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCateogry(newCategory: Category){
        coroutineScope.launch(Dispatchers.IO){
            categoryDAO.insertCategory(newCategory)
        }
    }

    fun updateCategory(newCategory: Category){
        coroutineScope.launch(Dispatchers.IO){
            categoryDAO.updateCategory(newCategory)
        }
    }

    fun getAll(nameUser: String){
        coroutineScope.launch(Dispatchers.Main){
            searchCollectionResult.value = asyncFindAll(nameUser)
        }
    }

    private suspend fun asyncFindAll(name: String): List<Category> = coroutineScope.async(Dispatchers.IO) {
        return@async categoryDAO.getAll()
    }.await()
}