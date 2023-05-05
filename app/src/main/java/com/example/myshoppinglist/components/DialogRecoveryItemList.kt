import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.*
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.ItemListViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DialogRecoveryItemList(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    idCard: Long,
    enabledDialog: Boolean,
    listItemChosen: List<ItemListAndCategory>,
    callback: CallbackItemList
) {

    var checkAll by remember { mutableStateOf(false) }
    var visibleAnimation by remember { mutableStateOf(true) }
    val itemCheckCollection = remember { mutableStateListOf<Long>() }
    val itemListViewModel = ItemListViewModel(context, lifecycleOwner)
    val itemListCollection = remember { mutableStateListOf<ItemListAndCategory>() }
    var showDialog by remember { mutableStateOf(enabledDialog) }
    val scope = rememberCoroutineScope()

    fun selectedCheckAll() {
        itemCheckCollection.removeAll(itemCheckCollection)
        itemListCollection.forEach {
            itemCheckCollection.add(it.itemList.id)
        }

    }

    fun cancel(){
        itemCheckCollection.removeAll(itemCheckCollection)
        showDialog = false
        callback.onChangeValue(false)
        checkAll = false
    }

    LaunchedEffect(key1 = enabledDialog) {
        showDialog = enabledDialog
    }

    LaunchedEffect(key1 = idCard, key2 = showDialog) {
        if (showDialog) {
            itemListViewModel.getAll(idCard)
        }
    }

    itemListViewModel.searchItemListResult.observe(lifecycleOwner) {
        if (it.isNotEmpty()) {
            itemListCollection.removeAll(itemListCollection)
            itemListCollection.addAll(it)
            val auxItemListCollection = it
            auxItemListCollection.forEach { auxItem ->

                if (listItemChosen.find { findItem -> findItem.itemList.id == auxItem.itemList.id } != null) {
                    itemListCollection.remove(auxItem)
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
                    visibleAnimation = visibleAnimation,
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
                            }
                        )
                    }
                })

            }

            ButtonsFooterContent(
                modifierButton = Modifier.padding(start = 16.dp, end = 16.dp),
                btnTextAccept = "ADICIONAR",
                onClickAccept = {
                    if(itemCheckCollection.isNotEmpty()){
                        scope.launch {
                            callback.onUpdateListAndCategory(itemListCollection.filter { itemChoice -> itemCheckCollection.find { itemCheck -> itemChoice.itemList.id == itemCheck } != null })
                        }.invokeOnCompletion {
                            cancel()
                        }
                    }
                },
                btnTextCancel = "CANCELAR",
                onClickCancel = {
                    cancel()
                })

        }
    }
}