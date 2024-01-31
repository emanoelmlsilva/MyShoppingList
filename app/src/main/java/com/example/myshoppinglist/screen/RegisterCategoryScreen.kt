package com.example.myshoppinglist.screen

//import androidx.compose.foundation.lazy.GridCells
//import androidx.compose.foundation.lazy.LazyVerticalGrid
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
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.fieldViewModel.RegisterCategoryFieldViewModel
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterCategoryScreen(
    navController: NavController,
    registerCategoryFieldViewModel: RegisterCategoryFieldViewModel,
    idCategory: Long
) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val isErrorName: Boolean by registerCategoryFieldViewModel.isErrorCategory.observeAsState(false)

    fun goBackNavigation() {
        navController.popBackStack()
    }

    var visibleWaiting by remember { mutableStateOf(false) }
    var visibleLoading by remember { mutableStateOf(true) }
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

    registerCategoryFieldViewModel.iconsCategories.observe(lifecycleOwner) {
        if (registerCategoryFieldViewModel.iconsCategories.value!!.isNotEmpty() && visibleLoading) {
            scope.launch {
                delay(1800)
                visibleLoading = false
            }
        }
    }

    TopAppBarScreen(onClickIcon = { goBackNavigation() }, onClickIconDone = {

        val newCategory = CategoryDTO(
            registerCategoryFieldViewModel.idMyShoppingApi.value!!,
            idCategory,
            registerCategoryFieldViewModel.nameCategory.value!!,
            registerCategoryFieldViewModel.idImage.value!!,
            registerCategoryFieldViewModel.color.value!!
        )

        if (newCategory.category.isNotBlank() && newCategory.idImage.isNotBlank()) {
            if (idCategory > 0) {
                newCategory.id = registerCategoryFieldViewModel.idMyShoppingApi.value!!
                updateCategory(newCategory, callback)
            } else {
                saveCategory(newCategory, callback)
            }
        }
    }, hasToolbar = true, hasDoneButton = true, hasBackButton = false, content = {

        LoadingComposable(visibleLoading)

        WaitingProcessComponent(visibleWaiting, messageError, callback)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 30.dp, end = 30.dp, top = 24.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                // remover esse codigo do bitmap e usar o verctor do proprio android icon
                IconCategoryComponent(
                    iconCategory = AssetsUtils.readIconImageBitmapById(context,
                        registerCategoryFieldViewModel.idImage.observeAsState().value!!.ifEmpty { "fastfood.png" })!!,
                    colorIcon = Color(registerCategoryFieldViewModel.color.observeAsState().value!!),
                    size = 56.dp,
                    enableClick = true
                )

                TextInputComponent(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
                    value = registerCategoryFieldViewModel.nameCategory.observeAsState().value!!,
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

            ColorPicker(
                colorCurrent = Color(registerCategoryFieldViewModel.color.value!!),
                callback = object : CallbackColor {
                    override fun onChangeValue(value: Color) {
                        registerCategoryFieldViewModel.onChangeColor(value.toArgb())
                    }
                },
                isVertically = true
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(.85f)
                    .padding(horizontal = 2.dp)
            ) {

                Text("Escolha um ícone", fontFamily = LatoBold, fontSize = 18.sp)

                if(registerCategoryFieldViewModel.iconsCategories.observeAsState().value!!.isNotEmpty()){
                    val iconsCategories = registerCategoryFieldViewModel.iconsCategories.observeAsState().value
                    val idImageChoice = registerCategoryFieldViewModel.idImage.observeAsState().value

                    LazyVerticalGrid(
                        state = scrollState,
                        modifier = Modifier
                            .padding(start = 13.dp, top = 8.dp),
                        cells = GridCells.Fixed(8)
                    ) {
                        iconsCategories!!.forEachIndexed { indexAnimation, iconCategoryAnimation ->

                            if (iconCategoryAnimation.idImage.lowercase() == (idImageChoice?.lowercase()
                                    ?: "")
                            ) {

                                scope.launch {
                                    //esse calculo deve ser feito porque é um grid
                                    scrollState.animateScrollToItem(((indexAnimation / 8)))
                                }

                            }
                        }

                        itemsIndexed(iconsCategories) { index, iconCategory ->
                            Row(horizontalArrangement = Arrangement.Center) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {
                                    // remover esse codigo do bitmap e usar o verctor do proprio android icon
                                    IconCategoryComponent(iconCategory = iconCategory.imageBitmap!!.asImageBitmap(),
                                        size = 36.dp,
                                        colorIcon = if (idImageChoice == iconCategory.idImage) Color(
                                            registerCategoryFieldViewModel.color.observeAsState().value!!
                                        ) else text_primary,
                                        enableClick = true,
                                        callback = object : Callback {
                                            override fun onClick() {
                                                registerCategoryFieldViewModel.onChangeIdImageCurrent(
                                                    iconCategory.idImage
                                                )
                                            }
                                        })
                                }
                            }
                        }
                    }

                }

            }
        }

    })

}