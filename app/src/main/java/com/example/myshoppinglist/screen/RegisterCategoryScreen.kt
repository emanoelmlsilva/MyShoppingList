package com.example.myshoppinglist.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.ColorPicker
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.components.WaitingProcessComponent
import com.example.myshoppinglist.fieldViewModel.RegisterCategoryFieldViewModel
import com.example.myshoppinglist.model.IconCategory
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterCategoryScreen(
    navController: NavController,
    registerCategoryFieldViewModel: RegisterCategoryFieldViewModel,
    idCategory: Long?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val iconsCategories =
        registerCategoryFieldViewModel.iconsCategories.observeAsState(listOf<IconCategory>()).value

    val isErrorName: Boolean by registerCategoryFieldViewModel.isErrorCategory.observeAsState(false)

    val categoryCurrent = registerCategoryFieldViewModel.categoryCurrent.observeAsState().value!!

    val idImageCurrent =
        registerCategoryFieldViewModel.idImage.observeAsState().value!!

    val colorCurrent = registerCategoryFieldViewModel.color.observeAsState().value!!

    val idMyShoppingApi by remember { mutableStateOf(0L) }

    fun goBackNavigation() {
        scope.launch(context = Dispatchers.Main) {
            registerCategoryFieldViewModel.reset()
            navController.popBackStack()
        }
    }

    var visibleWaiting by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    val callback = object : CallbackObject<CategoryDTO> {
        override fun onSuccess() {
            goBackNavigation()
            visibleWaiting = false
        }

        override fun onFailed(messageError: String) {

        }

        override fun onClick() {
            visibleWaiting = false
        }

        override fun onChangeValue(newValue: Boolean) {
            visibleWaiting = true
        }

        override fun onChangeValue(newValue: String) {
            messageError = newValue
        }
    }

    fun updateCategory(category: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        registerCategoryFieldViewModel.categoryController.updateCategory(category, callback)
    }

    fun saveCategory(category: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        registerCategoryFieldViewModel.categoryController.saveCategory(
            category, callback
        )
    }

    TopAppBarScreen(onClickIcon = { goBackNavigation() }, onClickIconDone = {

        val newCategory = CategoryDTO(
            idMyShoppingApi,
            idCategory ?: 0,
            categoryCurrent,
            idImageCurrent,
            colorCurrent
        )

        if (newCategory.category.isNotBlank() && newCategory.idImage.isNotBlank()) {
            if (idCategory != null && idCategory > 0) {
                newCategory.id = idMyShoppingApi
                updateCategory(newCategory, callback)
            } else {
                saveCategory(newCategory, callback)
            }
        }
    }, hasToolbar = true, hasDoneButton = true, hasBackButton = false, content = {

        WaitingProcessComponent(visibleWaiting, messageError, callback)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 30.dp, end = 30.dp, top = 24.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                IconCategoryComponent(
                    iconCategory = AssetsUtils.readIconImageBitmapById(
                        context,
                        idImageCurrent.ifEmpty { "fastfood.png" }
                    )!!,
                    colorIcon = Color(colorCurrent),
                    size = 56.dp,
                    enableClick = true
                )


                TextInputComponent(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
                    value = categoryCurrent,
                    label = "Categoria",
                    isMandatory = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    error = isErrorName,
                    customOnClick = object : CustomTextFieldOnClick {
                        override fun onChangeValue(newValue: String) {
                            registerCategoryFieldViewModel.onChangeCategory(newValue)
                        }
                    })
            }

            ColorPicker(colorCurrent = Color(colorCurrent), callback = object : CallbackColor {
                override fun onChangeValue(value: Color) {
                    registerCategoryFieldViewModel.onChangeColor(value.toArgb())
                }
            }, isVertically = true)

            Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                Text("Escolha um ícone", fontFamily = LatoBold, fontSize = 18.sp)
                LazyVerticalGrid(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxHeight(.7f)
                        .padding(start = 13.dp, top = 8.dp),
                    cells = GridCells.Fixed(8)
                ) {
                    iconsCategories.forEachIndexed { index, iconCategory ->
                        if (iconCategory.idImage.lowercase() == idImageCurrent.lowercase()) {
                            //esse calculo deve ser feito porque é um grid
                            scope.launch { scrollState.animateScrollToItem(((index / 8))) }
                        }
                    }
                    itemsIndexed(iconsCategories) { index, iconCategory ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            ) {
                                IconCategoryComponent(
                                    iconCategory = iconCategory.imageBitmap!!.asImageBitmap(),
                                    size = 36.dp,
                                    colorIcon = if (idImageCurrent == iconCategory.idImage) Color(
                                        colorCurrent
                                    ) else text_primary,
                                    enableClick = true,
                                    callback = object : Callback {
                                        override fun onClick() {
                                            registerCategoryFieldViewModel.onChangeIdImageCurrent(
                                                iconCategory.idImage
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    })
}