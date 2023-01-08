@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCateogry
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.ItemListViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListItemPurchaseScreen(navController: NavHostController) {
    val context = LocalContext.current
    var checkAll by remember { mutableStateOf(false) }
    var visibleAnimation by remember { mutableStateOf(true) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val itemListCollection = remember { mutableStateListOf<ItemListAndCateogry>() }
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)
    var enabledDialog by remember { mutableStateOf(false) }
    var itemListUpdate by remember { mutableStateOf<ItemListAndCateogry?>(null) }
    val scope = rememberCoroutineScope()

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection.forEach {
            itemCheckCollection.add(it.itemList.id)
        }

    }

    LaunchedEffect(key1 = Unit) {
        itemListViewModel.getAll()
    }

    itemListViewModel.searchItemListResult.observe(lifecycleOwner) {
        itemListUpdate = null
        itemListCollection.removeAll(itemListCollection)
        itemListCollection.addAll(it)

        if(checkAll){
            selectedCheckAll()
        }
    }

    TopAppBarScreen(
        hasBackButton = true,
        hasToolbar = true,
        onClickIcon = { navController.popBackStack() },
        paddingFloatingButton = 12.dp,
        floatingActionButton = {
            if (visibleAnimation) {
                Column {
                    ExtendedFloatingActionButton(
                        icon = { Icon(Icons.Filled.ShoppingCart, null, tint = primary_dark) },
                        backgroundColor = text_secondary,
                        onClick = {

                        },
                        text = { Text("mercado".capitalize()) }
                    )

                    Spacer(
                        Modifier
                            .height(13.dp)
                    )

                    ExtendedFloatingActionButton(
                        backgroundColor = primary_dark,
                        onClick = {
                            enabledDialog = true
                        },
                        icon = {
                            Icon(Icons.Filled.Add, null, tint = background_card)
                        },
                        text = { Text("adicionar".capitalize(), color = text_primary) })
                }
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                RegisterItemList(
                    context,
                    lifecycleOwner,
                    enabledDialog,
                    itemListUpdate,
                    object : CallbackItemList {
                        override fun itemList(itemList: ItemList) {
                            itemListViewModel.insertItemList(itemList)
                            itemListViewModel.getAll()
                            scope.launch {
                                enabledDialog = false
                            }
                        }

                        override fun onUpdate(item: ItemListAndCateogry) {
                            itemListViewModel.updateItemList(item.itemList)
                            itemListViewModel.getAll()
                            scope.launch {
                                enabledDialog = false
                            }

                        }

                        override fun onClick() {
                            enabledDialog = false
                        }
                    })
                BaseAnimationComponent(
                    visibleAnimation = visibleAnimation,
                    contentBase = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Lista de compras do Mês",
                                fontFamily = LatoBlack,
                                color = primary_dark,
                                fontSize = 18.sp
                            )

                            Spacer(
                                Modifier
                                    .height(35.dp)
                            )

                            Divider(
                                color = secondary_dark,
                                modifier = Modifier
                                    .height(1.dp)
                            )
                        }
                    })

                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                        .clickable {
                            checkAll = !checkAll
                            if (checkAll) {
                                selectedCheckAll()
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                        checked = checkAll,
                        onCheckedChange = {
                            checkAll = it
                            if (checkAll) {
                                selectedCheckAll()
                            }
                        }
                    )
                    Text(
                        "Selecionar todos",
                        fontFamily = LatoBlack,
                        modifier = Modifier
                            .padding(end = 6.dp)
                    )
                }

                BaseLazyColumnScroll(modifier = Modifier
                    .fillMaxWidth(), callback = object : VisibleCallback() {
                    override fun onChangeVisible(visible: Boolean) {
                        visibleAnimation = visible
                    }
                }, content = {
                    itemsIndexed(itemListCollection) { index, itemListAndCategory ->
                        val itemListCurrent = itemListAndCategory.itemList
                        val isCheck = itemCheckCollection.indexOf(itemListCurrent.id) != -1
                        SwipeableItemList(
                            context,
                            itemListAndCategory,
                            isCheck,
                            itemListCurrent.id,
                            itemListAndCategory.category,
                            itemListAndCategory.itemList.item,
                            if (index % 2 == 0) divider_ligth else divider,
                            object : CallbackItemList {
                                override fun onChangeValue(idCard: Long) {
                                    val isChecked = itemCheckCollection.indexOf(idCard) != -1
                                    checkAll = if (isChecked) {
                                        itemCheckCollection.remove(idCard)
                                        false
                                    } else {
                                        itemCheckCollection.add(idCard)
                                        itemCheckCollection.size == itemListCollection.size

                                    }
                                }

                                override fun onDelete() {
                                    itemListViewModel.deleteItemList(itemListCurrent)
                                    scope.launch {
                                        itemListViewModel.getAll()
                                    }
                                }

                                override fun onUpdate(item: ItemListAndCateogry) {
                                    enabledDialog = true
                                    itemListUpdate = item

                                }
                            }
                        )
                    }
                })
            }
        })
}

@Composable
fun RegisterItemList(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    enabledDialog: Boolean,
    itemListUpdate: ItemListAndCateogry?,
    callback: CallbackItemList
) {
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val categoryCollections = remember { mutableStateListOf<Category>() }
    var categoryChoice by remember {
        mutableStateOf<Long>(-1)
    }
    var item by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        categoryViewModel.getAll()
    }

    LaunchedEffect(key1 = itemListUpdate) {
        if (itemListUpdate != null) {
            categoryChoice = itemListUpdate.category.id
            item = itemListUpdate.itemList.item
        }
    }

    categoryViewModel.searchCollectionResult.observe(lifecycleOwner) {
        categoryCollections.removeAll(categoryCollections)
        categoryCollections.addAll(it)
    }

    fun onClick(id: Long) {
        categoryChoice = id
    }

    fun reset() {
        categoryChoice = -1L
        item = ""
    }

    if (enabledDialog) {
        Dialog(
            onDismissRequest = { },
            content = {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = card_blue,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.7f)
                            .background(text_secondary)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                Modifier
                                    .height(25.dp)
                            )

                            Text(
                                "Adicionar na lista",
                                fontFamily = LatoBlack,
                                color = primary_dark,
                                fontSize = 18.sp
                            )

                            Spacer(
                                Modifier
                                    .height(25.dp)
                            )

                            Divider(
                                color = secondary_dark,
                                modifier = Modifier
                                    .height(1.dp)
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.SpaceAround, modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .fillMaxHeight()
                        ) {
                            TextInputComponent(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                label = "Produto",
                                value = item,
                                reset = false,
                                maxChar = 45,
                                isMandatory = true,
                                error = item.isBlank(),
                                customOnClick = object : CustomTextFieldOnClick {
                                    override fun onChangeValue(newValue: String) {
                                        item = newValue
                                    }
                                })

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Categorias",
                                        modifier = Modifier.padding(
                                            top = 16.dp,
                                            bottom = 8.dp,
                                            end = 4.dp
                                        ),
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (categoryChoice == -1L) Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_error_24),
                                        modifier = Modifier.size(16.dp),
                                        contentDescription = null,
                                        tint = message_error
                                    )
                                }
                                LazyRow(modifier = Modifier.padding(start = 8.dp)) {
                                    items(categoryCollections) { category ->
                                        Card(modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                onClick(category.id)
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .height(33.dp)
                                                    .background(if (category.id == categoryChoice) background_card_light else background_card),
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconCategoryComponent(
                                                    iconCategory = AssetsUtils.readIconBitmapById(
                                                        context,
                                                        category.idImage
                                                    )!!
                                                        .asImageBitmap(),
                                                    colorIcon = Color(category.color),
                                                    size = 40.dp,
                                                    enableClick = true,
                                                    enabledBackground = false,
                                                    callback = object : Callback {
                                                        override fun onClick() {
                                                            onClick(category.id)
                                                        }
                                                    }
                                                )

                                                Text(
                                                    text = category.category,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(start = 0.dp, end = 16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            ButtonsFooterContent(
                                isClickable = true,
                                btnTextCancel = "CANCELAR",
                                btnTextAccept = "SALVAR",
                                onClickCancel = {
                                    callback.onClick()
                                    reset()
                                },
                                onClickAccept = {
                                    val itemList = ItemList(item, categoryChoice)
                                    if (itemListUpdate != null) {
                                        itemList.id = itemListUpdate.itemList.id
                                        callback.onUpdate(ItemListAndCateogry(itemList, Category()))
                                    } else {
                                        callback.itemList(itemList)
                                    }

                                    reset()
                                })
                        }

                    }
                }
            })
    }
}

enum class SwipeDirection(val raw: Int) {
    Left(0),
    Initial(1),
    Right(2),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableItemList(
    context: Context,
    itemListAndCategory: ItemListAndCateogry,
    isCheck: Boolean,
    idItem: Long,
    category: Category,
    product: String,
    backgroundColor: Color,
    callback: CallbackItemList
) {
    val squareSize = 100f
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)
    val scope = rememberCoroutineScope()
    var enabledDeleteDialog by remember { mutableStateOf(false) }

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Right -> {
                        callback.onUpdate(itemListAndCategory)
                    }
                    SwipeDirection.Left -> {
                        enabledDeleteDialog = true
                    }
                    else -> {
                        return@onDispose
                    }
                }
                scope.launch {
                    swipeableState.animateTo(SwipeDirection.Initial)
                }
            }
        }
    }

    fun doToPix(dpValue: Float): Float {
        return dpValue * context.resources.displayMetrics.density
    }

    val anchors = mapOf(
        0f to SwipeDirection.Initial,
        -doToPix(squareSize) to SwipeDirection.Left,
        doToPix(squareSize) to SwipeDirection.Right
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(text_secondary)
    ) {
        Box() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .background(background_text_field_dardk)
                        .fillMaxWidth(.6f)
                        .fillMaxHeight()
                        .padding(start = 4.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("EDITAR", fontFamily = LatoRegular)
                }
                Column(
                    modifier = Modifier
                        .background(message_error)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 4.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("REMOVER", fontFamily = LatoRegular)
                }

            }

        }
        if(enabledDeleteDialog){
            Dialog(
                onDismissRequest = { },
                content = {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = text_secondary,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth().padding(top = 16.dp)
                                .background(text_secondary)

                        ) {

                            Row(modifier = Modifier.background(text_title_secondary).fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    imageVector = Icons.Rounded.Warning,
                                    contentDescription = null,
                                    tint = text_secondary)

                                Text("Deseja remover o item $product da lista?", fontFamily = LatoRegular)

                            }

                            ButtonsFooterContent(
                                isClickable = true,
                                btnTextCancel = "CANCELAR",
                                btnTextAccept = "SALVAR",
                                onClickCancel = {
                                    enabledDeleteDialog = false
                                },
                                onClickAccept = {
                                    callback.onDelete()
                                    enabledDeleteDialog = false
                                })
                        }
                    }
                })
        }
        BoxItemListComponent(
            modifier = Modifier.offset {
                IntOffset(
                    swipeableState.offset.value.roundToInt(),
                    0
                )
            },
            context,
            isCheck,
            idItem,
            category,
            product,
            backgroundColor,
            callback
        )
    }
}