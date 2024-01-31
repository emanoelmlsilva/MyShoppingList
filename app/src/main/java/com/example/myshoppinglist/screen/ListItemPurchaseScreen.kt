package com.example.myshoppinglist.screen

import DialogRegisterItemList
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CallbackSwipe
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.enums.FilterFabState
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.fieldViewModel.ListItemFieldViewModel
import com.example.myshoppinglist.services.controller.ItemListController
import com.example.myshoppinglist.services.dtos.CreditCardDTO
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MeasureTimeService
import kotlinx.coroutines.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListItemPurchaseScreen(
    navController: NavHostController,
    idCard: Long,
    listItemFieldViewModel: ListItemFieldViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var visibleWaiting by remember { mutableStateOf(false) }
    val visibleLoading by listItemFieldViewModel.visibleLoading.observeAsState(false)
    var enabledDeleteDialog by remember { mutableStateOf(false) }

    var messageError by remember { mutableStateOf(MeasureTimeService.messageWaitService) }

    var filterFabState by rememberSaveable() {
        mutableStateOf(FilterFabState.DEFAULT)
    }

    var checkAll by remember { mutableStateOf(false) }
    var enabledDialog by remember { mutableStateOf(false) }

    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    var itemListUpdate by remember { mutableStateOf<ItemListDTO?>(null) }

    val itemListCollection by listItemFieldViewModel.itemListCollection.observeAsState(emptyList())
    val creditCardDTO = listItemFieldViewModel.creditCardDTO.value
    val itemListController = ItemListController.getData(context, lifecycleOwner)

    var activeItem by remember { mutableStateOf(-1) }
    var currentDraggedItem by remember { mutableStateOf(-1) }

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection?.forEach {
            itemCheckCollection.add(it.myShoppingId)
        }

    }

    fun removeCheckAll() {
        itemCheckCollection.clear()
    }

    fun handleClickSelected() {
        if (itemListCollection.isNotEmpty()) {
            checkAll = !checkAll
            if (checkAll) {
                selectedCheckAll()
            } else {
                removeCheckAll()
            }
        }

    }

    val callback = object : CallbackObject<ItemListDTO> {

        override fun onSuccess() {
            this.onSuccess(ItemListDTO())
        }

        override fun onSuccess(itemListDTO: ItemListDTO) {

            listItemFieldViewModel.updateItemListAll(idCard)
            enabledDialog = false
            visibleWaiting = false
            messageError = MeasureTimeService.messageWaitService

        }

        override fun onCancel() {
            enabledDialog = false
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

    val callbackFabButton = object : CallbackObject<FilterFabState> {
        override fun onSuccess() {
            if (itemCheckCollection.isNotEmpty()) {

                filterFabState = FilterFabState.DEFAULT

                val itemListChoicesCollection =

                    if (checkAll) itemListCollection.map {
                        it.creditCardDTO =
                            creditCardDTO?.fromCreditCardDTO() ?: CreditCardDTO()
                        it
                    } else
                        itemListCollection?.filter { itemCheckCollection.indexOf(it.myShoppingId) != -1 }
                            ?.map {
                                it.creditCardDTO =
                                    creditCardDTO?.fromCreditCardDTO() ?: CreditCardDTO()

                                it
                            }

                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    "itemListChoiceCollection",
                    itemListChoicesCollection as ArrayList<out Parcelable>
                )
                bundle.putLong("idCard", idCard)

                navController.currentBackStackEntry!!.arguments!!.putAll(bundle)

                navController.navigate(
                    Screen.MakingMarketScreen.name
                )


            } else {
                CustomToastComponent(context, "Selecione pelo menos um produto!")
            }
        }

        override fun onClick() {
            filterFabState = FilterFabState.COLLAPSED
            enabledDialog = true
        }

        override fun onSuccess(newFilterFabState: FilterFabState) {
            filterFabState = newFilterFabState
        }
    }

    LaunchedEffect(key1 = itemListCollection.size) {

        listItemFieldViewModel.updateVisibleLoading(itemListCollection.isEmpty())
    }

    LaunchedEffect(key1 = idCard) {
        if (itemListCollection != null && itemListCollection.isEmpty()) {
            listItemFieldViewModel.updateItemListAll(idCard)

            listItemFieldViewModel.updateCreditCardDTO(idCard)
        }
    }

    TopAppBarScreen(
        hasBackButton = true,
        hasDoneButton = false,
        hasToolbar = true,
        onClickIcon = { navController.popBackStack() },
        paddingFloatingButton = 12.dp,
        floatingActionButton = {

            val animatableFloatingAction =

                listOf<@Composable (animatable: Animatable<Float, AnimationVector1D>, modifier: Modifier) -> Unit>(
                    { animatable, modifier ->
                        ExtendedFloatingActionButton(
                            modifier = modifier,
                            icon = { Icon(Icons.Filled.ShoppingCart, null, tint = primary_dark) },
                            backgroundColor = text_secondary,
                            onClick = {
                                callbackFabButton.onSuccess()
                            },
                            text = { Text("mercado  ".capitalize(), color = text_primary) }
                        )
                    },
                    { animatable, modifier ->
                        ExtendedFloatingActionButton(
                            modifier = modifier,
                            backgroundColor = text_secondary,
                            onClick = {
                                callbackFabButton.onClick()
                            },
                            icon = {
                                Icon(Icons.Filled.Add, null, tint = primary_dark)
                            },
                            text = { Text("adicionar".capitalize(), color = text_primary) })
                    })


            FilterMultipleFabMenuButton(
                filterFabState,
                callbackFabButton,
                animatableFloatingAction
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

                if (itemListUpdate != null) {
                    DialogBackCustom(enabledDeleteDialog, {
                        itemListController.deleteItemListDB(
                            itemListUpdate!!.toItemList(),
                            object : Callback {
                                override fun onSuccess() {
                                    listItemFieldViewModel.updateItemListAll(
                                        idCard
                                    )
                                }

                                override fun onFailed(messageError: String) {
                                }
                            })
                        itemListUpdate = null
                        enabledDeleteDialog = false
                    }, {
                        itemListUpdate = null
                        enabledDeleteDialog = false
                    }, "Deletar", "Deseja remover o item ${itemListUpdate!!.item} da lista?")
                }

                LoadingComposable(visibleLoading)

                WaitingProcessComponent(visibleWaiting, messageError, callback)

                DialogRegisterItemList(
                    context,
                    lifecycleOwner,
                    enabledDialog,
                    itemListUpdate,
                    object : CallbackItemList {
                        override fun onInsert(itemList: ItemListDTO) {
                            itemList.creditCardDTO =
                                creditCardDTO?.fromCreditCardDTO() ?: CreditCardDTO()

                            itemListController.saveItemList(itemList, callback)
                        }

                        override fun onUpdate(itemList: ItemListDTO) {
                            itemList.creditCardDTO =
                                creditCardDTO?.fromCreditCardDTO() ?: CreditCardDTO()

                            itemListController.updateItemList(itemList, callback)
                        }

                        override fun onClick() {
                            enabledDialog = false
                        }

                        override fun onChangeValue(newValue: Boolean) {
                            callback.onChangeValue(newValue)
                        }
                    })

                BaseAnimationComponent(
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

                Card(
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                        .alpha(if (itemListCollection.isNotEmpty()) 1f else 0.5f)
                        .align(Alignment.Start)
                        .clickable(enabled = itemListCollection.isNotEmpty()) {
                            handleClickSelected()
                        },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.background(secondary),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            enabled = itemListCollection.isNotEmpty(),
                            colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                            checked = checkAll,
                            onCheckedChange = {
                                handleClickSelected()
                            }
                        )
                        Text(
                            "Selecionar todos",
                            fontFamily = LatoBlack,
                            modifier = Modifier
                                .padding(end = 6.dp)
                        )
                    }
                }

                if (itemListCollection != null && itemListCollection.isNotEmpty() && !visibleLoading) {
                    BaseLazyColumnScroll(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        content = {
                            itemsIndexed(itemListCollection) { index, itemList ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(
                                            bottom = if (index == (itemListCollection.size - 1)) 56.dp else 0.dp
                                        )
                                ) {
                                    SwipeComponent(
                                        index = index,
                                        onSwipe = { activeItem = it },
                                        onDragStart = { currentDraggedItem = it },
                                        onDragEnd = { currentDraggedItem = -1 },
                                        colorBackground = secondary,
                                        callback = object : CallbackSwipe {
                                            override fun onHandlerLeftAction() {
                                                if (!enabledDeleteDialog) {
                                                    enabledDeleteDialog = true
                                                    itemListUpdate = itemList
                                                }
                                            }

                                            override fun onHandlerHighAction() {
                                                if (!enabledDialog) {
                                                    enabledDialog = true
                                                    itemListUpdate = itemList
                                                }
                                            }
                                        },
                                        dismissBackground = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                val isCheck =
                                                    itemCheckCollection.indexOf(itemList.myShoppingId) != -1

                                                SlidingItemListComponent(
                                                    context = context,
                                                    itemListDTO = itemList,
                                                    isCheck = isCheck,
                                                    isMarket = false,
                                                    hasOptionEdit = false,
                                                    isRemoved = itemList.isRemoved,
                                                    idItem = itemList.myShoppingId,
                                                    category = itemList.categoryDTO.toCategory(),
                                                    product = itemList.item,
                                                    callback = object : CallbackItemList {
                                                        override fun onChangeValue(idCard: Long) {
                                                            val isChecked =
                                                                itemCheckCollection.indexOf(idCard) != -1
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
                                                                        listItemFieldViewModel.updateItemListAll(
                                                                            idCard
                                                                        )
                                                                    }

                                                                    override fun onFailed(
                                                                        messageError: String
                                                                    ) {
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
                            }
                        })
                } else {
                    EmptyTextComponent("Não existe nenhum item cadastrado.")
                }
            }
        })
}