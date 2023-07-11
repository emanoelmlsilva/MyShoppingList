package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.CategoryViewModelDB
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.screen.TAG
import com.example.myshoppinglist.services.CategoryService
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.services.repository.CategoryRepository
import com.example.myshoppinglist.ui.viewModel.CategoryViewModel

class CategoryController {

    val TAG = "CategoryController"

    companion object {
        private lateinit var categoryViewModel: CategoryViewModel
        private lateinit var lifecycleOwner: LifecycleOwner
        private val email = UserLoggedShared.getEmailUserCurrent()
        private lateinit var userDTO: UserDTO

        fun getData(context: Context, newLifecycleOwner: LifecycleOwner): CategoryController {
            lifecycleOwner = newLifecycleOwner;

            categoryViewModel = CategoryViewModel(
                CategoryRepository(CategoryService.getCategoryService()),
                CategoryViewModelDB(context, lifecycleOwner)
            )

            UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(
                lifecycleOwner
            ) {

                try{
                    userDTO = it
                }catch (nullPoint: NullPointerException){
                    Log.d(TAG, "getData "+nullPoint.message)
                }
            }

            return CategoryController()
        }
    }

//    fun getCategoryService() : CategoryService{
//        return categoryService
//    }

    fun getAllDB(): LiveData<List<Category>> {
        return categoryViewModel.getAllDB()
    }

    fun getCategoryByIdDB(idCategory: Long): LiveData<Category> {
        return categoryViewModel.getCategoryByIdDB(idCategory)
    }

    fun findAndSaveCategories(email: String, callback: Callback){
        categoryViewModel.findAndSaveAllCategory(email, callback)
    }

    fun saveCategory(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {

        categoryDTO.userDTO = userDTO

        categoryViewModel.save(categoryDTO, callback)


//        if(!isRecover){
//            categoryService.save(userWithCategory).enqueue(object : Callback<UserWithCategory> {
//                override fun onResponse(
//                    call: Call<UserWithCategory>,
//                    response: Response<UserWithCategory>
//                ) {
//                    categoryViewModel.insertCategory(userWithCategory.toCategoryId(), callback)
//                }
//
//                override fun onFailure(call: Call<UserWithCategory>, t: Throwable) {
//                    Log.d(LOG, "error ${t.message}")
//                    callback.onCancel()
//                }
//            })
//        }else{
//            categoryViewModel.insertCategory(userWithCategory.toCategoryId(), callback)
//        }
    }

    //
//    fun saveCategory(userWithCategory: UserWithCategory, isRecover: Boolean = false){
//        saveCategory(userWithCategory, isRecover, object : com.example.myshoppinglist.callback.Callback{
//            override fun onCancel() {
//                super.onCancel()
//            }
//
//            override fun onSuccess() {
//                super.onSuccess()
//            }
//        })
//    }
//
    fun updateCategory(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {

        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(
            lifecycleOwner
        ) {
            categoryDTO.userDTO = it

            categoryViewModel.update(categoryDTO, callback)
        }

    }
}