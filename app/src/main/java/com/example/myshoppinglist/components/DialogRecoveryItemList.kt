import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.viewModels.ItemListViewModelDB
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DialogRecoveryItemList(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    idCard: Long,
    enabledDialog: Boolean,
    listItemChosen: List<ItemListDTO>,
    callback: CallbackItemList
) {
    val MESSAGE_EMPTY_LIST = "Selecione algum item da lista de mercado"
    var checkAll by remember { mutableStateOf(false) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val itemListViewModel = ItemListViewModelDB(context, lifecycleOwner)
    val itemListCollection = remember { mutableStateListOf<ItemListDTO>() }
    var showDialog by remember { mutableStateOf(enabledDialog) }
    val scope = rememberCoroutineScope()

    fun selectedCheckAll() {
        itemCheckCollection.clear()
        itemListCollection.forEach {
            itemCheckCollection.add(it.myShoppingId)
        }

    }

    fun cancel() {
        itemCheckCollection.clear()
        showDialog = false
        callback.onChangeValue(false)
        checkAll = false
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

    LaunchedEffect(key1 = enabledDialog) {
        showDialog = enabledDialog
    }

    LaunchedEffect(key1 = idCard, key2 = showDialog) {
        if (showDialog) {
            itemListViewModel.getAllWithCategory(idCard).observeForever { itemListAndCategory ->
                if (itemListAndCategory.isNotEmpty()) {
                    itemListCollection.removeAll(itemListCollection)
                    itemListCollection.addAll(itemListAndCategory.map {
                        ItemListDTO(
                            it.itemList,
                            it.category
                        )
                    })
                    val auxItemListCollection = ArrayList(itemListCollection)
                    itemListCollection.forEach { auxItem ->
                        if (listItemChosen.find { findItem -> findItem.myShoppingId == auxItem.myShoppingId } != null) {
                            auxItemListCollection.remove(auxItem)
                        }
                    }
                    itemListCollection.removeAll(itemListCollection)
                    itemListCollection.addAll(auxItemListCollection)
                }
            }
        }
    }

    DialogCustom(
        percentHeight = 1.2f,
        visibilityDialog = showDialog
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(text_secondary)
                .padding(top = 28.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                BaseAnimationComponent(
                    contentBase = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Adicionar na lista de mercado",
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
                            handleClickSelected()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
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

                BaseLazyColumnScroll(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.9f),
                    verticalArrangement = Arrangement.Top,
                    content = {
                        itemsIndexed(itemListCollection) { index, itemListService ->
                            val isCheck =
                                itemCheckCollection.indexOf(itemListService.myShoppingId) != -1
                            SlidingItemListComponent(
                                context = context,
                                itemListDTO = itemListService,
                                isCheck = isCheck,
                                hasOptionEdit = false,
                                isMarket = false,
                                isRemoved = itemListService.isRemoved,
                                idItem = itemListService.myShoppingId,
                                category = itemListService.categoryDTO.toCategory(),
                                product = itemListService.item,
                                callback = object : CallbackItemList {
                                    override fun onUpdate(itemList: ItemListDTO) {
                                        TODO("Not yet implemented")
                                    }

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

            Box() {
                ButtonsFooterContent(
                    modifierButton = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    btnTextAccept = "ADICIONAR",
                    onClickAccept = {
                        if (itemCheckCollection.isNotEmpty()) {
                            scope.launch {

                                val listUpdate = itemListCollection.filter { itemChoice ->
                                    itemCheckCollection.find { itemCheck ->
                                        return@find itemChoice.myShoppingId == itemCheck

                                    } != null
                                }
                                callback.onUpdate(listUpdate)
                            }.invokeOnCompletion {
                                cancel()
                            }
                        } else {
                            CustomToastComponent(
                                context,
                                MESSAGE_EMPTY_LIST
                            )
                        }
                    },
                    btnTextCancel = "CANCELAR",
                    onClickCancel = {
                        cancel()
                    })

            }

        }
    }
}