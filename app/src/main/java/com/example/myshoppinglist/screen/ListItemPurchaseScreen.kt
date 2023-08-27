package com.example.myshoppinglist.screen

import DialogRegisterItemList
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.ItemListController
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.ConversionUtils

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListItemPurchaseScreen(navController: NavHostController, idCard: Long) {
    val context = LocalContext.current
    var checkAll by remember { mutableStateOf(false) }
    var visibleAnimation by remember { mutableStateOf(true) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val itemListCollection = remember { mutableStateListOf<ItemListDTO>() }
    var enabledDialog by remember { mutableStateOf(false) }
    var itemListUpdate by remember { mutableStateOf<ItemListDTO?>(null) }
    var creditCardDTO by remember { mutableStateOf(CreditCardDTODB()) }
    val itemListController = ItemListController.getData(context, lifecycleOwner)
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection.forEach {
            itemCheckCollection.add(it.myShoppingId)
        }

    }

    fun getItemListAll() {
        itemListController.getAllWithCategoryDB(idCard)
            .observe(lifecycleOwner) { itemListAndCategory ->
                itemListUpdate = null
                itemListCollection.removeAll(itemListCollection)
                itemListCollection.addAll(itemListAndCategory.map {
                    ItemListDTO(
                        it.itemList,
                        it.category
                    )
                })

                if (checkAll) {
                    selectedCheckAll()
                }
            }
    }

    LaunchedEffect(key1 = idCard) {
        getItemListAll()

        creditCardController.findCreditCardByIdDB(idCard).observe(lifecycleOwner) {
            creditCardDTO = CreditCardDTODB().fromCreditCardDTODB(it)
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
                            if (itemCheckCollection.isNotEmpty()) {
                                val convertList = itemListCollection.map { itemListDTO ->

                                        itemListDTO.creditCardDTO =
                                            creditCardDTO.fromCreditCardDTO()

                                        itemListDTO

                                }.filter {
                                    itemCheckCollection.indexOf(
                                        it.myShoppingId
                                    ) != -1
                                }

                                navController.navigate(
                                    "${Screen.MakingMarketScreen.name}?idCard=${idCard}?itemListCollection=${
                                        ConversionUtils<ItemListDTO>(ItemListDTO::class.java).toJsonList(
                                            convertList
                                        )
                                    }"
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Selecione pelo menos um produto!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                    enabledDialog,
                    itemListUpdate,
                    object : CallbackItemList {
                        override fun onInsert(itemList: ItemListDTO) {
                            itemList.creditCardDTO = creditCardDTO.fromCreditCardDTO()

                            itemListController.saveItemList(itemList, object : Callback {
                                override fun onSuccess() {
                                    getItemListAll()
                                    enabledDialog = false
                                }

                                override fun onCancel() {
                                    super.onCancel()
                                }
                            })
                        }

                        override fun onUpdate(itemList: ItemListDTO) {
                            itemList.creditCardDTO = creditCardDTO.fromCreditCardDTO()

                            itemListController.updateItemList(itemList, object : Callback {
                                override fun onSuccess() {
                                    getItemListAll()
                                    enabledDialog = false
                                }

                                override fun onCancel() {
                                    enabledDialog = false
                                }
                            })
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
                    .fillMaxHeight()
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    callback = object : VisibleCallback {
                        override fun onChangeVisible(visible: Boolean) {
                            visibleAnimation = visible
                        }
                    }, content = {
                        itemsIndexed(itemListCollection) { index, itemList ->
                            val isCheck = itemCheckCollection.indexOf(itemList.myShoppingId) != -1
                            SlidingItemListComponent(
                                context = context,
                                itemListDTO = itemList,
                                isCheck = isCheck,
                                isMarket = false,
                                isRemoved = itemList.isRemoved,
                                sizeCheckCollection = itemCheckCollection.isNotEmpty(),
                                idItem = itemList.myShoppingId,
                                category = itemList.categoryDTO.toCategory(),
                                product = itemList.item,
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
                                        itemListController.deleteItemListDB(
                                            itemList.toItemList(),
                                            object : Callback {
                                                override fun onSuccess() {
                                                    getItemListAll()
                                                }

                                                override fun onFailed(messageError: String) {
                                                }
                                            })
                                    }

                                    override fun onUpdate(itemListDTO: ItemListDTO) {
                                        enabledDialog = true
                                        itemListUpdate = itemListDTO

                                    }
                                }
                            )
                        }
                    })
            }
        })
}