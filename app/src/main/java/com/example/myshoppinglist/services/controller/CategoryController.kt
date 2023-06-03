package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.database.entities.relations.UserWithCategory
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.services.CategoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryController {

    val LOG = "CategoryController"

    companion object{
        private lateinit var categoryService: CategoryService
        private lateinit var categoryViewModel: CategoryViewModel

        fun getData(context: Context, lifecycleOwner: LifecycleOwner) : CategoryController{
            categoryViewModel = CategoryViewModel(context, lifecycleOwner)
            categoryService = CategoryService.getCategoryService()
            return CategoryController()
        }
    }

    fun getCategoryService() : CategoryService{
        return categoryService
    }

    fun saveCategory(userWithCategory: UserWithCategory, isRecover: Boolean = false, callback: com.example.myshoppinglist.callback.Callback){
        if(!isRecover){
            categoryService.save(userWithCategory).enqueue(object : Callback<UserWithCategory> {
                override fun onResponse(
                    call: Call<UserWithCategory>,
                    response: Response<UserWithCategory>
                ) {
                    categoryViewModel.insertCategory(userWithCategory.toCategoryId(), callback)
                }

                override fun onFailure(call: Call<UserWithCategory>, t: Throwable) {
                    Log.d(LOG, "error ${t.message}")
                }
            })
        }else{
            categoryViewModel.insertCategory(userWithCategory.toCategoryId(), callback)
        }
    }

    fun saveCategory(userWithCategory: UserWithCategory, isRecover: Boolean = false){
        saveCategory(userWithCategory, isRecover, object : com.example.myshoppinglist.callback.Callback{
            override fun onCancel() {
                super.onCancel()
            }

            override fun onSucess() {
                super.onSucess()
            }
        })
    }

    fun updateCategory(userWithCategory: UserWithCategory, callback: com.example.myshoppinglist.callback.Callback){
        categoryService.update(userWithCategory).enqueue(object : Callback<UserWithCategory> {
            override fun onResponse(
                call: Call<UserWithCategory>,
                response: Response<UserWithCategory>
            ) {
                userWithCategory.id = response.body()!!.id
                categoryViewModel.updateCategory(userWithCategory.toCategoryId(), callback)
            }

            override fun onFailure(call: Call<UserWithCategory>, t: Throwable) {
            }
        })
    }
}