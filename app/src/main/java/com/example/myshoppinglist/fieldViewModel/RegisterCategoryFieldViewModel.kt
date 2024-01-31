package com.example.myshoppinglist.fieldViewModel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.model.IconCategory
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.utils.AssetsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterCategoryFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {
    var nameCategory = MutableLiveData("")
    var idImage = MutableLiveData("fastfood.png")
    var color = MutableLiveData(Color.Red.toArgb())
    var isErrorCategory = MutableLiveData(false)
    val idMyShoppingApi = MutableLiveData(0L)
    val iconsCategories = MutableLiveData(emptyList<IconCategory>())

    val categoryController = CategoryController.getData(context, lifecycleOwner)

    init{
        viewModelScope.launch(Dispatchers.Main) {
            onChangeIconsCategories(AssetsUtils.readIconCollections(context))
        }

    }

    fun reset() {
        this.color.value = Color.Red.toArgb()
        onChangeCategory("")
        this.idImage.value = "fastfood.png"
    }

    fun updateCategory(idCategory: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            categoryController.getCategoryByIdDB(idCategory).observeForever {
                onChangeCategory(it.category)
                onChangeIdImageCurrent(it.idImage)
                onChangeColor(it.color)
                onChangeIdMyShoppingApi(it.idMyShoppingApi)
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
        nameCategory.value = newCategory
        onChangeErrorCategory(newCategory.isBlank())
    }

    fun onChangeIconsCategories(newIconsCategories: List<IconCategory>) {
        this.iconsCategories.value = emptyList()
        iconsCategories.value = newIconsCategories
    }

    fun onChangeErrorCategory(newError: Boolean) {
        this.isErrorCategory.value = newError
    }

    override fun checkFields(): Boolean {
        return this.nameCategory.value?.isNotBlank() ?: false
    }
}