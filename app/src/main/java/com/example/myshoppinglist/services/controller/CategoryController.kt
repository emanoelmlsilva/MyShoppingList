package com.example.myshoppinglist.services.controller

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.CategoryViewModelDB
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.CategoryService
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.services.repository.CategoryRepository
import com.example.myshoppinglist.services.viewModel.CategoryViewModel
import kotlinx.coroutines.launch

class CategoryController {

    companion object {
        private lateinit var categoryViewModel: CategoryViewModel
        private lateinit var lifecycleOwner: LifecycleOwner
        private lateinit var userDTO: UserDTO
        private val email = UserLoggedShared.getEmailUserCurrent()
        val TAG = "CategoryController"

        fun getData(context: Context, newLifecycleOwner: LifecycleOwner): CategoryController {
            lifecycleOwner = newLifecycleOwner;

            categoryViewModel = CategoryViewModel(
                CategoryRepository(CategoryService.getCategoryService()),
                CategoryViewModelDB(context, lifecycleOwner)
            )

            lifecycleOwner.lifecycleScope.launch {
                UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observeForever {
                    try {
                        userDTO = it
                    } catch (nullPoint: NullPointerException) {
                        Log.d(TAG, "getData " + nullPoint.message)
                    }
                }
            }

            return CategoryController()
        }
    }

    fun getAllDB(): LiveData<List<Category>> {
        return categoryViewModel.getAllDB()
    }

    fun getCategoryByIdDB(idCategory: Long): LiveData<Category> {
        return categoryViewModel.getCategoryByIdDB(idCategory)
    }

    fun findAndSaveCategories(email: String, callback: Callback) {
        categoryViewModel.findAndSaveAllCategory(email, callback)
    }

    fun saveCategory(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {

        categoryDTO.userDTO = userDTO

        categoryViewModel.save(categoryDTO, callback)

    }

    fun updateCategory(categoryDTO: CategoryDTO, callback: CallbackObject<CategoryDTO>) {

        categoryDTO.userDTO = userDTO

        categoryViewModel.update(categoryDTO, callback)

    }
}