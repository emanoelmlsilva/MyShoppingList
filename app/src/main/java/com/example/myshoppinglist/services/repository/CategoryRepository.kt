package com.example.myshoppinglist.services.repository

import ResultData
import android.util.Log
import com.example.myshoppinglist.services.CategoryService
import com.example.myshoppinglist.services.dtos.CategoryDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val categoryService: CategoryService ) {

    private val TAG = "CategoryRepository"

    suspend fun findAndSaveCategories(email: String): ResultData<List<CategoryDTO>>{
        return withContext(Dispatchers.IO){
            val categoriesExceute = categoryService.findAll(email).execute()

            return@withContext if(categoriesExceute.isSuccessful){
                val categoriesResponse = categoriesExceute.body()?: listOf()

                Log.d(TAG, "FIND - SUCCESS")
                ResultData.Success(categoriesResponse)
            }else{
                ResultData.Error(Exception("ERROR FIND ${categoriesExceute.errorBody()}"))
            }
        }
    }

    suspend fun save(categoryDTO: CategoryDTO): ResultData<CategoryDTO>{
        return withContext(Dispatchers.IO){
            val categoryExecute = categoryService.save(categoryDTO).execute()

            return@withContext if(categoryExecute.isSuccessful){
                val categoryResponse = categoryExecute.body()?:CategoryDTO()
                Log.d(TAG, "SAVE - SUCCESS")
                ResultData.Success(categoryResponse)
            }else{
                ResultData.Error(Exception("ERROR SAVE ${categoryExecute.errorBody()}"))
            }
        }
    }

    suspend fun update(categoryDTO: CategoryDTO): ResultData<CategoryDTO>{
        return withContext(Dispatchers.IO){
            val categoryExecute = categoryService.update(categoryDTO).execute()

            return@withContext if(categoryExecute.isSuccessful){
                val categoryResponse = categoryExecute.body()?: CategoryDTO()
                Log.d(TAG, "update - SUCCESS")
                ResultData.Success(categoryResponse)
            }else{
                ResultData.Error(Exception("ERROR SAVE ${categoryExecute.errorBody()}"))
            }
        }
    }
}