package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BaseAnimationComponent
import com.example.myshoppinglist.components.BaseLazyColumnScroll
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCateogry
import com.example.myshoppinglist.database.viewModels.ItemListViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils

@Composable
fun BoxListPurchase(
    context: Context,
    isCheck: Boolean,
    idItem: Long,
    category: Category,
    product: String,
    backgroundColor: Color,
    callback: Callback
) {
    Column(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    Modifier
                        .width(5.dp)
                )

                IconCategoryComponent(
                    modifier = Modifier.padding(start = 6.dp),
                    iconCategory = AssetsUtils.readIconBitmapById(
                        context,
                        category.idImage
                    )!!
                        .asImageBitmap(),
                    colorIcon = Color(category.color),
                    size = 40.dp,
                    enabledBackground = true
                )

                Spacer(
                    Modifier
                        .width(15.dp)
                )

                Text(product, fontFamily = LatoBlack)
            }
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                checked = isCheck,
                onCheckedChange = {
                    callback.onChangeValue(idItem)
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListPurchaseScreen(navController: NavHostController) {
    val context = LocalContext.current
    var checkAll by remember { mutableStateOf(false) }
    var visibleAnimation by remember { mutableStateOf(true) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var itemListCollection = remember { mutableStateListOf<ItemListAndCateogry>() }
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)


    LaunchedEffect(key1 = Unit) {
        itemListViewModel.getAll()
    }

    itemListViewModel.searchItemListResult.observe(lifecycleOwner) {
        itemListCollection.removeAll(itemListCollection)
        itemListCollection.addAll(it)
    }

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection.forEach {
            itemCheckCollection.add(it.itemList.id)
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
                    FloatingActionButton(
                        backgroundColor = text_secondary,
                        onClick = {

                        }) {
                        BaseAnimationComponent(
                            visibleAnimation = visibleAnimation,
                            contentBase = {
                                Icon(Icons.Filled.ShoppingCart, null, tint = primary_dark)
                            })
                    }

                    Spacer(
                        Modifier
                            .height(13.dp)
                    )

                    FloatingActionButton(backgroundColor = primary_dark,
                        onClick = {
//                            itemListViewModel.insertItemList(ItemList("Feijão", 1))
//                            itemListViewModel.getAll()
                        }) {
                        Icon(Icons.Filled.Add, null, tint = background_card)
                    }
                }
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                BaseAnimationComponent(
                    visibleAnimation = visibleAnimation,
                    contentBase = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Compras do Mês",
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
                        .align(Alignment.Start).clickable {
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
                        BoxListPurchase(
                            context,
                            isCheck,
                            itemListCurrent.id,
                            itemListAndCategory.category,
                            itemListAndCategory.itemList.item,
                            if (index % 2 == 0) divider_ligth else text_secondary_light,
                            object : Callback {
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
                            }
                        )
                    }
                })
            }
        })
}