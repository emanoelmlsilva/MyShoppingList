import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.DialogCustom
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.ItemList
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogRegisterItemList(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    idCard: Long,
    enabledDialog: Boolean,
    itemListUpdate: ItemListAndCategory?,
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
    var checkRemoved by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        categoryViewModel.getAll()
    }

    LaunchedEffect(key1 = itemListUpdate) {
        if (itemListUpdate != null) {
            categoryChoice = itemListUpdate.category.id!!
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
        checkRemoved = false
    }

    DialogCustom(visibilityDialog = enabledDialog) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(text_secondary)
            , verticalArrangement = Arrangement.SpaceAround
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
                        .height(20.dp)
                )

                Divider(
                    color = secondary_dark,
                    modifier = Modifier
                        .height(1.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, bottom = 18.dp)
                    .fillMaxHeight(.65f)
            ) {
                TextInputComponent(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .padding(0.dp),
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

                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable {
                            checkRemoved = !checkRemoved
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                        checked = checkRemoved,
                        onCheckedChange = {
                            checkRemoved = it
                        }
                    )
                    Text(
                        "Remover ao salvar",
                        fontFamily = LatoRegular,
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Categorias",
                            modifier = Modifier.padding(
                                top = 0.dp,
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
                                    onClick(category.id!!)
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
                                                onClick(category.id!!)
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
                    val itemList = ItemList(item, checkRemoved, categoryChoice, idCard)
                    if (itemListUpdate != null) {
                        itemList.id = itemListUpdate.itemList.id
                        callback.onUpdate(ItemListAndCategory(itemList, Category()))
                    } else {
                        callback.itemList(itemList)
                    }

                    reset()
                })
        }
    }
}