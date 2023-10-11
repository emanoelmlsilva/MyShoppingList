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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.components.WaitingProcessComponent
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.model.IconCategory
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.dtos.CategoryDTO
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MeasureTimeService
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.godaddy.android.colorpicker.toColorInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val registerCategoryFieldViewModel = RegisterCategoryFieldViewModel()

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterCategoryScreen(navController: NavController, idCategory: Long?) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val iconsCategories = remember { mutableListOf<IconCategory>() }

    val isErrorName: Boolean by registerCategoryFieldViewModel.isErrorCategory.observeAsState(false)

    val categoryController = CategoryController.getData(context, lifecycleOwner)

    val categoryCurrent = registerCategoryFieldViewModel.categoryCurrent.observeAsState().value!!

    val idImageCurrent = registerCategoryFieldViewModel.idImage.observeAsState("fastfood.png").value!!

    val colorCurrent = registerCategoryFieldViewModel.color.observeAsState().value!!

    var idMyShoppingApi by remember { mutableStateOf(0L) }

    fun goBackNavigation() {
        scope.launch(context = Dispatchers.Main) {
            registerCategoryFieldViewModel.reset()
            navController.popBackStack()
        }
    }

    var visibleWaiting by remember{mutableStateOf(false)}
    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    val callback = object : CallbackObject<CategoryDTO> {
        override fun onSuccess() {

            if(visibleWaiting){
                MeasureTimeService.resetMeasureTime(object : Callback {
                    override fun onChangeValue(newValue: Boolean) {
                        goBackNavigation()
                        visibleWaiting = newValue
                    }
                })
            }else{
                goBackNavigation()
            }

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

    LaunchedEffect(Unit) {
        iconsCategories.addAll(
            AssetsUtils.readIconCollections(
                context
            ) as List<IconCategory>
        )
    }

    LaunchedEffect(idCategory) {
        if (idCategory != 0L) {
            categoryController.getCategoryByIdDB(idCategory!!).observe(lifecycleOwner) {
                if (it != null) {
                    idMyShoppingApi = it.idMyShoppingApi
                    registerCategoryFieldViewModel.onChangeCategory(it.category)
                    registerCategoryFieldViewModel.onChangeColor(it.color)
                    registerCategoryFieldViewModel.onChangeIdImageCurrent(it.idImage)
                }
            }
        }
    }

    fun updateCategory(category: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        categoryController.updateCategory(category, callback)
    }

    fun saveCategory(category: CategoryDTO, callback: CallbackObject<CategoryDTO>) {
        categoryController.saveCategory(
            category, callback)
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
            MeasureTimeService.startMeasureTime(callback)

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

            Column(
                modifier = Modifier
                    .fillMaxHeight(.3f)
                    .fillMaxWidth(.7f)
            ) {
                Text("Escolha uma cor", fontFamily = LatoBold, fontSize = 18.sp)

                HarmonyColorPicker(
                    harmonyMode = ColorHarmonyMode.MONOCHROMATIC,
                    onColorChanged = { color: HsvColor ->
                        registerCategoryFieldViewModel.onChangeColor(color.toColorInt())
                    }
                )
            }

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

class RegisterCategoryFieldViewModel : BaseFieldViewModel() {
    val categoryCurrent = MutableLiveData("")
    val idImage = MutableLiveData("fastfood.png")
    val color = MutableLiveData(Color.Red.toArgb())
    var isErrorCategory: MutableLiveData<Boolean> = MutableLiveData(false)

    fun reset() {
        this.color.value = Color.Red.toArgb()
        this.categoryCurrent.value = ""
        this.idImage.value = "fastfood.png"
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

    fun onChangeErrorCategory(newError: Boolean) {
        this.isErrorCategory.value = newError
    }

    override fun checkFields(): Boolean {

        return this.categoryCurrent.value!!.isNotBlank()

    }
}