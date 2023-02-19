@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.myshoppinglist.screen

import DialogRegisterItemList
import android.content.Context
import android.widget.Toast
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
import com.example.myshoppinglist.database.dtos.ItemListAndCategoryDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.ItemListViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.ConversionUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListItemPurchaseScreen(navController: NavHostController, idCard: Long) {
    val context = LocalContext.current
    var checkAll by remember { mutableStateOf(false) }
    var visibleAnimation by remember { mutableStateOf(true) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val itemListCollection = remember { mutableStateListOf<ItemListAndCategory>() }
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)
    var enabledDialog by remember { mutableStateOf(false) }
    var itemListUpdate by remember { mutableStateOf<ItemListAndCategory?>(null) }
    val scope = rememberCoroutineScope()

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection.forEach {
            itemCheckCollection.add(it.itemList.id)
        }

    }

    LaunchedEffect(key1 = idCard) {
        itemListViewModel.getAll(idCard)
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
        hasDoneButton = false,
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
                            if(itemCheckCollection.isNotEmpty()){
                                navController.navigate("${Screen.MakingMarketScreen.name}?idCard=${idCard}?itemListCollection=${ConversionUtils<ItemListAndCategoryDTO>().toJson(ItemListAndCategoryDTO().toItemListAndCategoryDTO(itemListCollection.filter { itemCheckCollection.indexOf(it.itemList.id) != -1}))}")
                            }else{
                                Toast.makeText(context, "Selecione pelo menos um produto!", Toast.LENGTH_SHORT).show()
                            }
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

                DialogRegisterItemList(
                    context,
                    lifecycleOwner,
                    idCard,
                    enabledDialog,
                    itemListUpdate,
                    object : CallbackItemList {
                        override fun itemList(itemList: ItemList) {
                            itemListViewModel.insertItemList(itemList)
                            itemListViewModel.getAll(idCard)
                            scope.launch {
                                enabledDialog = false
                            }
                        }

                        override fun onUpdate(item: ItemListAndCategory) {
                            scope.launch {
                                itemListViewModel.updateItemList(item.itemList)
                                itemListViewModel.getAll(idCard)
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
                        SlidingItemListComponent(
                            context = context,
                            itemListAndCategory = itemListAndCategory,
                            isCheck = isCheck,
                            isMarket = false,
                            isRemoved = itemListCurrent.isRemoved,
                            sizeCheckCollection = itemCheckCollection.isNotEmpty(),
                            idItem = itemListCurrent.id,
                            category = itemListAndCategory.category,
                            product = itemListAndCategory.itemList.item,
                            backgroundColor = if (index % 2 == 0) background_card_gray_light else background_card_light,
                            callback = object : CallbackItemList {
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
                                        itemListViewModel.getAll(idCard)
                                    }
                                }

                                override fun onUpdate(item: ItemListAndCategory) {
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