package com.example.myshoppinglist.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackFilter
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.fieldViewModel.ProductManagerFieldViewModel
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.ui.theme.background_card_gray_light
import com.example.myshoppinglist.ui.theme.background_card_light
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.FormatDateUtils
import com.example.myshoppinglist.utils.MaskUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchProductComponent(
    context: Context,
    visibleAnimation: Boolean,
    lifecycleOwner: LifecycleOwner,
    productManagerFieldViewModel: ProductManagerFieldViewModel
) {
    var product by remember { mutableStateOf("") }
    val listProductText = remember { mutableStateListOf<String>() }
    var reset by remember { mutableStateOf(false) }
    var enableDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf(ObjectFilter()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    fun removeFilter(productItem: String) {
        listProductText.remove(productItem)

        var formarValueMin: String? = null
        var formarValueMax: String? = null

        if (filter.priceMin != null && filter.priceMax != null) {
            formarValueMin = MaskUtils.maskValue(
                MaskUtils.convertValueDoubleToString(
                    filter.priceMin!!.toDouble()
                )
            )
            formarValueMax = MaskUtils.maskValue(
                MaskUtils.convertValueDoubleToString(
                    filter.priceMax!!.toDouble()
                )
            )
        }

        if (filter.textCollection.indexOf(productItem) != -1) {
            val indexProduct =
                filter.textCollection.indexOf(productItem)
            if (indexProduct != -1) {
                filter.textCollection.removeAt(indexProduct)
            }

        } else if (productItem.startsWith("%month%")) {
            filter.month = ""
        } else if (productItem == "$formarValueMin á $formarValueMax") {
            filter.priceMin = null
            filter.priceMax = null
        } else if (productItem.startsWith("%card%")) {
            filter.cardFilter = CardCreditFilter()
            filter.idCard = 0
        } else {
            var typeCategory =
                filter.categoryCollection.find { productItem.contains(it.category) }
            if (typeCategory != null) {
                filter.categoryCollection.remove(typeCategory)
            }
        }

        productManagerFieldViewModel.searchPurchases(filter)
    }

    productManagerFieldViewModel.getProduct().observe(lifecycleOwner) {

        if (it.isNotBlank() && reset) {
            reset = false
        }
        product = it
    }

    LaunchedEffect(key1 = enableDialog) {
        if (enableDialog) {
            keyboardController!!.hide()
        }
    }

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {

            Card(
                backgroundColor = background_card_gray_light, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.13f),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (enableDialog) {
                    AlertDialogFilterComponent(
                        keyboardController!!,
                        enableDialog,
                        filter,
                        productManagerFieldViewModel,
                        lifecycleOwner,
                        object : CallbackFilter {
                            override fun onClick() {
                                enableDialog = false
                            }

                            override fun onChangeObjectFilter(value: ObjectFilter) {

                                value.textCollection = filter.textCollection

                                listProductText.removeAll(listProductText)

                                filter.textCollection.forEach {
                                    listProductText.add(it)
                                }

                                value.categoryCollection.forEach { category ->
                                    listProductText.add("%category%, ${category.myShoppingId}, ${category.category}, ${category.idImage}, ${category.color}")
                                }

                                if (value.priceMin != null && value.priceMax != null) {
                                    val formatValueMin =
                                        MaskUtils.maskValue(
                                            MaskUtils.convertValueDoubleToString(
                                                value.priceMin!!.toDouble()
                                            )
                                        )
                                    val formatValueMax =
                                        MaskUtils.maskValue(
                                            MaskUtils.convertValueDoubleToString(
                                                value.priceMax!!.toDouble()
                                            )
                                        )

                                    listProductText.add("$formatValueMin á $formatValueMax")
                                }

                                if (value.month.isNotBlank()) {
                                    listProductText.add("%month%, ${value.month}")
                                }

                                if (value.idCard != 0L) {
                                    listProductText.add("%card%, ${value.idCard}, ${value.cardFilter.avatar}, ${value.cardFilter.nickName}")
                                }

                                filter = value

                                productManagerFieldViewModel.searchPurchases(filter)
                            }
                        })
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Card(
                        backgroundColor = background_card_light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.53f),
                        shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextInputComponent(
                                focusRequester = focusRequester,
                                modifier = Modifier
                                    .fillMaxWidth(.85f)
                                    .fillMaxHeight(),
                                value = product,
                                reset = reset,
                                label = "Produto",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = null,
                                        tint = text_primary,
                                    )
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {

                                        if (product.isNotBlank()) {
                                            filter.textCollection.add(product.trim())

                                            productManagerFieldViewModel.searchPurchases(
                                                filter
                                            )
                                            listProductText.add(product.trim())
                                            reset = true
                                            productManagerFieldViewModel.onChangeProduct("")
                                        }

                                    }
                                ),
                                error = false,
                                customOnClick = object : CustomTextFieldOnClick {
                                    override fun onChangeValue(newValue: String) {
                                        productManagerFieldViewModel.onChangeProduct(newValue)
                                    }
                                })

                            IconButton(
                                onClick = {
                                    keyboardController!!.hide()
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.Main) {
                                            enableDialog = true
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_outline_filter_alt_24),
                                    contentDescription = null,
                                    tint = text_primary,
                                )
                            }
                        }
                    }

                    LazyRow(modifier = Modifier.padding(top = 4.dp)) {
                        items(listProductText) { productItem ->

                            var label: String? = null
                            var icon: Int? = null
                            var category: Category? = null

                            if (productItem.startsWith("%card%")) {
                                val splitValueText = productItem.split(",")
                                label = splitValueText[3]
                                icon = splitValueText[2].trim().toInt()
                            } else if (productItem.startsWith("%category%")) {
                                val splitValuesCategory = productItem.split(",")
                                val idSplit = splitValuesCategory[1].trim().toLong()
                                val categorySplit = splitValuesCategory[2].trim()
                                val idImageSplit = splitValuesCategory[3].trim()
                                val color = splitValuesCategory[4].trim().toFloat().toInt()

                                category = Category(categorySplit, idImageSplit, color)
                                category.myShoppingId = idSplit
                            } else if (productItem.startsWith("%month%")) {
                                val splitValueText = productItem.split(",")
                                val separatedMonthOfYear = splitValueText[1].split("-")
                                if (separatedMonthOfYear.size > 1 && separatedMonthOfYear[1].isNotEmpty()) {
                                    val month = separatedMonthOfYear[1]

                                    label = FormatDateUtils().getNameMonth(month)
                                        .replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        }
                                }
                            }

                            CustomerChip(
                                context = context,
                                category = category,
                                label = label ?: productItem,
                                iconId = icon,
                                callback = object : Callback {
                                    override fun onClick() {
                                        removeFilter(productItem)
                                    }
                                })
                        }
                    }
                }
            }
        })
}