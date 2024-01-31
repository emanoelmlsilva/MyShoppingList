package com.example.myshoppinglist.database.repositories

import androidx.lifecycle.LiveData
import com.example.myshoppinglist.database.daos.CategoryDAO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoryRepository(private val categoryDAO: CategoryDAO) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val email = UserLoggedShared.getEmailUserCurrent()

    fun insertCategory(newCategory: Category) {
        coroutineScope.launch(Dispatchers.IO) {
            categoryDAO.insertCategory(newCategory)
        }
    }

    fun insertCategories(newCategories: List<Category>){
        coroutineScope.launch(Dispatchers.IO) {
            categoryDAO.insertCategories(newCategories)
        }
    }

    fun updateCategory(newCategory: Category) {
        coroutineScope.launch(Dispatchers.IO) {
            categoryDAO.updateCategory(newCategory)
        }
    }

    fun getCategoryById(idCategory: Long): LiveData<Category> {
        return categoryDAO.getById(idCategory, email)
    }
//
//    fun getCategoryByCategory(emailUser: String, category: String) {
//        coroutineScope.launch(Dispatchers.Main) {
//            searchResult.value = asyncFindByCategory(emailUser, category)
//        }
//    }

    fun getAll(): LiveData<List<Category>> {
       return categoryDAO.getAll(email)
    }

//    private suspend fun asyncFindByCategory(email: String, category: String): Category =
//        coroutineScope.async(Dispatchers.IO) {
//            return@async categoryDAO.getByCategory(category, email)
//        }.await()

}