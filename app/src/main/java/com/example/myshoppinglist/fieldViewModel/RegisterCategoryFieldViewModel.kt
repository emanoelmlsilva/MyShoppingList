package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.model.IconCategory
import com.example.myshoppinglist.services.controller.CategoryController

class RegisterCategoryFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {
    val categoryCurrent = MutableLiveData("")
    val idImage = MutableLiveData("fastfood.png")
    val color = MutableLiveData(Color.Red.toArgb())
    var isErrorCategory: MutableLiveData<Boolean> = MutableLiveData(false)
    val categoryController = CategoryController.getData(context, lifecycleOwner)
    val iconsCategories: MutableLiveData<MutableList<IconCategory>> =
        MutableLiveData(mutableListOf())
    val idMyShoppingApi = MutableLiveData(0L)

    fun reset() {
        this.color.value = Color.Red.toArgb()
        this.categoryCurrent.value = ""
        this.idImage.value = "fastfood.png"
    }

    fun recoverCategory(idCategory: Long, lifecycleOwner: LifecycleOwner) {
        if (idCategory != 0L) {
            categoryController.getCategoryByIdDB(idCategory).observe(lifecycleOwner) { category ->
                if (category != null) {
                    onChangeIdMyShoppingApi(category.idMyShoppingApi)
                    onChangeCategory(category.category)
                    onChangeColor(category.color)
                    onChangeIdImageCurrent(category.idImage)
                }
            }

        }
    }

    fun onChangeIdMyShoppingApi(newIdMyShoppingApi: Long) {
        idMyShoppingApi.value = newIdMyShoppingApi
    }

    fun onChangeColor(newColor: Int) {
        this.color.value = newColor
    }

    fun onChangeIdImageCurrent(newId: String) {
        this.idImage.value = newId
    }

    fun onChangeCategory(newCategory: String) {
        onChangeErrorCategory(newCategory.isBlank())
        this.categoryCurrent.value = newCategory
    }

    fun onChangeIconsCategories(newIconsCategories: MutableList<IconCategory>) {
        this.iconsCategories.value = newIconsCategories
    }

    fun onChangeErrorCategory(newError: Boolean) {
        this.isErrorCategory.value = newError
    }

    override fun checkFields(): Boolean {

        return this.categoryCurrent.value!!.isNotBlank()

    }
}