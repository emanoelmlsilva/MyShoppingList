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
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.model.IconCategory
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.card_red_dark
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.AssetsUtils
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.godaddy.android.colorpicker.toColorInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterCategoryScreen(navController: NavController, idCategory: Long?) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val iconsCategories = remember { mutableListOf<IconCategory>() }
    val scope = rememberCoroutineScope()
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val registerCategoryFieldViewModel = RegisterCategoryFieldViewModel()
    val scrollState = rememberLazyListState()
    var idCurrent by remember {
        mutableStateOf("")
    }
    var colorCurrent by remember {
        mutableStateOf(0)
    }
    var categoryCurrent by remember {
        mutableStateOf("")
    }

    val isErrorName: Boolean by registerCategoryFieldViewModel.isErrorCategory.observeAsState(false)

    LaunchedEffect(Unit) {
        registerCategoryFieldViewModel.onChangeIconCategoryCollection(
            AssetsUtils.readIconCollections(
                context
            ) as List<IconCategory>
        )
    }

    LaunchedEffect(idCategory) {

        this.launch(context = Dispatchers.Main) {
            if (idCategory != 0L) {
                Thread.sleep(250)
                categoryViewModel.getCategoryById(idCategory!!)
            }
        }

    }

    registerCategoryFieldViewModel.iconCategoryCollection.observe(lifecycleOwner) {

        iconsCategories.addAll(it)

    }

    registerCategoryFieldViewModel.id.observe(lifecycleOwner) {
        idCurrent = it
    }

    registerCategoryFieldViewModel.color.observe(lifecycleOwner) {
        colorCurrent = it
    }

    registerCategoryFieldViewModel.categoryCurrent.observe(lifecycleOwner) {
        categoryCurrent = it
    }

    categoryViewModel.searchResult.observe(lifecycleOwner) { it ->
        if (it != null) {
            registerCategoryFieldViewModel.onChangeCategory(it.category)
            registerCategoryFieldViewModel.onChangeColor(it.color)
            registerCategoryFieldViewModel.onChangeIdCurrent(it.idImage)
        }
    }

    fun updateCategory(category: Category) {
        categoryViewModel.updateCategory(category)
    }

    fun saveCategory(category: Category) {
        categoryViewModel.insertCategory(category)
    }

    fun goBackNavigation() {
        navController.popBackStack()
    }

    TopAppBarScreen(onClickIcon = { goBackNavigation() }, onClickIconDone = {
        val newCategory = Category(categoryCurrent, idCurrent, colorCurrent)

        if(newCategory.category.isNotBlank()){
            if (idCategory != null && idCategory > 0) {
                newCategory.id = idCategory
                updateCategory(newCategory)
            }else{
                saveCategory(newCategory)
            }

            goBackNavigation()
        }
    }, hasToolbar = true, hasBackButton = false, content = {

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
                        idCurrent.ifEmpty { "fastfood.png" }
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
                    harmonyMode = ColorHarmonyMode.NONE,
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
                        if (iconCategory.idImage.lowercase() == idCurrent.lowercase()) {
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
                                    colorIcon = if (idCurrent == iconCategory.idImage) Color(
                                        colorCurrent
                                    ) else text_primary,
                                    enableClick = true,
                                    callback = object : Callback {
                                        override fun onClick() {
                                            registerCategoryFieldViewModel.onChangeIdCurrent(
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
    val iconCategoryCollection = MutableLiveData<List<IconCategory>>()
    val categoryCurrent = MutableLiveData("")
    val id = MutableLiveData("fastfood.png")
    val color = MutableLiveData(card_red_dark.toArgb())
    val category = MutableLiveData(Category())
    var isErrorCategory: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onChangeCategory(newCategory: Category) {
        this.category.value = newCategory
    }

    fun onChangeColor(newColor: Int) {
        this.color.value = newColor
    }

    fun onChangeIdCurrent(newId: String) {
        this.id.value = newId
    }

    fun onChangeCategory(newCategory: String) {
        onChangeErrorCategory(newCategory.isBlank())
        this.categoryCurrent.value = newCategory
    }

    fun onChangeErrorCategory(newError: Boolean) {
        this.isErrorCategory.value = newError
    }

    fun onChangeIconCategoryCollection(newIconCategoryCollection: List<IconCategory>) {
        this.iconCategoryCollection.value = newIconCategoryCollection
    }

    override fun checkFileds(): Boolean {

        return this.categoryCurrent.value!!.isNotBlank()

    }
}