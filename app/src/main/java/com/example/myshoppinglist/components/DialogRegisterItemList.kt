import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.DialogCustom
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.dtos.CategoryDTO
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.viewModels.CategoryViewModelDB
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogRegisterItemList(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    enabledDialog: Boolean,
    itemListUpdate: ItemListDTO?,
    callback: CallbackItemList
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val categoryViewModel = CategoryViewModelDB(context, lifecycleOwner)
    val categoryCollections by categoryViewModel.getAll().observeAsState(initial = emptyList())
    var categoryChoice by remember {
        mutableStateOf(CategoryDTO())
    }
    var item by remember {
        mutableStateOf("")
    }
    var checkRemoved by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = enabledDialog){
        if(!enabledDialog){
            categoryChoice = CategoryDTO()
            item = ""
            checkRemoved = false
        }
    }

    LaunchedEffect(key1 = itemListUpdate) {
        if (itemListUpdate != null) {
            categoryChoice = itemListUpdate.categoryDTO
            item = itemListUpdate.item
        } else{
            categoryChoice = CategoryDTO()
            item = ""
        }
    }

    fun onClick(categoryDTO: CategoryDTO) {
        categoryChoice = categoryDTO
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
                        if (categoryChoice.myShoppingId == 0L) Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_error_24),
                            modifier = Modifier.size(16.dp),
                            contentDescription = null,
                            tint = message_error
                        )
                    }
                    LazyRow(state = scrollState,
                        modifier = Modifier.padding(start = 8.dp)) {
                        categoryCollections!!.forEachIndexed { indexAnimation, categoryAnimation ->

                            if (categoryAnimation.myShoppingId == categoryChoice.myShoppingId) {

                                scope.launch {
                                    //esse calculo deve ser feito porque é um grid
                                    scrollState.animateScrollToItem(((indexAnimation)))
                                }

                            }
                        }

                        items(categoryCollections) { category ->
                            Card(modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val categoryDTO = CategoryDTO()
                                    categoryDTO.toCategoryDTO(category)
                                    onClick(categoryDTO)
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .height(33.dp)
                                        .background(if (category.myShoppingId == categoryChoice.myShoppingId) background_card_light else background_card),
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
                                                val categoryDTO = CategoryDTO()
                                                categoryDTO.toCategoryDTO(category)
                                                onClick(categoryDTO)
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
                },
                onClickAccept = {
                    val itemListDTO = ItemListDTO()
                    itemListDTO.item = item
                    itemListDTO.isRemoved = checkRemoved
                    itemListDTO.categoryDTO = categoryChoice

                    if(itemListDTO.item.isNotBlank() && itemListDTO.categoryDTO.category.isNotBlank()){
                        if (itemListUpdate != null) {
                            itemListDTO.myShoppingId = itemListUpdate.myShoppingId
                            itemListDTO.id = itemListUpdate.id
                            callback.onUpdate(itemListDTO)
                        } else {
                            callback.onInsert(itemListDTO)
                        }
                    }

                })
        }
    }
}