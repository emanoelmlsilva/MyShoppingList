package com.example.myshoppinglist.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.callback.CallbackFilter
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.enums.PositionDialog
import com.example.myshoppinglist.fieldViewModel.ProductManagerFieldViewModel
import com.example.myshoppinglist.model.CardCreditFilter
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AlertDialogFilterComponent(
    keyboardController: SoftwareKeyboardController,
    enableDialog: Boolean,
    filter: ObjectFilter,
    productManagerFieldViewModel: ProductManagerFieldViewModel,
    lifecycleOwner: LifecycleOwner,
    callback: CallbackFilter
) {
    val context = LocalContext.current
    val priceMinDefault = 0F
    val priceMaxDefault = 100F
    val categoryChoices = remember { mutableStateListOf<Category>() }
    val categoryCollections = remember { mutableStateListOf<Category>() }
    var priceMin by remember { mutableStateOf(priceMinDefault) }
    var priceMax by remember { mutableStateOf(priceMaxDefault) }
    var month by remember { mutableStateOf("") }
    var idCardCredit by remember { mutableStateOf(0L) }
    var currentCardCreditFilter by remember { mutableStateOf(CardCreditFilter()) }
    val creditCardDTOCollection = remember { mutableListOf<CreditCardDTODB>() }

    val callbackChoiceData = object : Callback {
        override fun onChangeValue(newMonth: String) {
            month = newMonth
        }
    }

    LaunchedEffect(Unit) {
        keyboardController.hide()

        if(filter.cardFilter.id != 0L){
            idCardCredit = filter.cardFilter.id
            currentCardCreditFilter = filter.cardFilter
        }

        if(filter.priceMin != null || filter.priceMax != null){
            keyboardController.hide()
            priceMin = if (filter.priceMin == null) priceMinDefault else filter.priceMin!!
            priceMax = if (filter.priceMax == null) priceMaxDefault else filter.priceMax!!
        }

        if(filter.month.isNotBlank()){
            month = filter.month
        }
    }

    LaunchedEffect(key1 = categoryCollections.size){
        if(categoryCollections.isNotEmpty()){
            categoryChoices.removeAll(categoryChoices)
            filter.categoryCollection.forEach {
                val hasCollection = categoryCollections.find { category -> category.myShoppingId == it.myShoppingId }

                if (hasCollection != null) {
                    categoryChoices.add(it)
                }
            }
        }
    }

    productManagerFieldViewModel.getCategoryController().getAllDB().observe(lifecycleOwner) {
        categoryCollections.removeAll(categoryCollections)
        categoryCollections.addAll(it)
    }

    productManagerFieldViewModel.getCreditCardController().findAllDB()
        .observe(lifecycleOwner) {
            if (it.isNotEmpty()) {
                creditCardDTOCollection.removeAll(creditCardDTOCollection)
                val creditCardDBCollection = it.map { creditCard ->
                    CreditCardDTODB().fromCreditCardDTODB(
                        creditCard
                    )
                }
                creditCardDTOCollection.addAll(creditCardDBCollection)
            }
        }

    fun removeCategory(category: Category){
        val hasNotExist =
            categoryChoices.find { categoryChoice ->
                categoryChoice.myShoppingId == category.myShoppingId
            } == null
        if (hasNotExist) {
            categoryChoices.add(category)
        } else {
            val categoryRemoved = categoryChoices.find { categoryAtRemove -> category.myShoppingId == categoryAtRemove.myShoppingId }

            categoryChoices.remove(categoryRemoved)
        }
    }

    DialogCustom(
        visibilityDialog = enableDialog,
        position = PositionDialog.TOP,
        percentHeight = 1.4f,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(secondary)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Text(
                    text = "Filtros",
                    fontFamily = LatoBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 25.dp)
                )
                Divider(
                    color = divider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
                Spacer(Modifier.size(44.dp))
                Card(
                    modifier = Modifier
                        .fillMaxHeight(.2f)
                        .fillMaxWidth(),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxHeight(.4f)
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            backgroundColor = primary_dark,
                            shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Categorias:",
                                    fontFamily = LatoBlack,
                                    fontSize = 18.sp,
                                    color = text_primary_light,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                        LazyRow(
                            modifier = Modifier
                                .background(background_card_gray_light)
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(categoryCollections) { category ->
                                val isChoiceCurrent =
                                    categoryChoices.find { categoryChoice ->
                                        categoryChoice.myShoppingId == category.myShoppingId
                                    } != null
                                Card(backgroundColor = (if (isChoiceCurrent) background_card_light else background_card),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            removeCategory(category)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconCategoryComponent(
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            ),
                                            iconCategory = AssetsUtils.readIconBitmapById(
                                                context,
                                                category.idImage
                                            )!!
                                                .asImageBitmap(),
                                            colorIcon = Color(category.color),
                                            size = 23.dp,
                                            enableClick = true,
                                            enabledBackground = true,
                                            callback = object : Callback {
                                                override fun onClick() {
                                                    removeCategory(category)
                                                }
                                            }
                                        )

                                        Text(
                                            text = category.category,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = if (isChoiceCurrent) 8.dp else 18.dp)
                                        )

                                        if (isChoiceCurrent) {
                                            Icon(
                                                imageVector = Icons.Outlined.Done,
                                                contentDescription = null,
                                                tint = text_primary,
                                                modifier = Modifier
                                                    .size(23.dp)
                                                    .padding(end = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ChoicePriceComponent(
                    priceMin,
                    priceMax,
                    callbackMin = object : CustomTextFieldOnClick {
                        override fun onChangeValueFloat(newValue: Float) {
                            priceMin = newValue
                        }
                    },
                    callbackMax = object : CustomTextFieldOnClick {
                        override fun onChangeValueFloat(newValue: Float) {
                            priceMax = newValue
                        }
                    })

                ChoiceCardComponent(
                    cardCurrent = filter.cardFilter.id,
                    cardCreditCollection = creditCardDTOCollection,
                    callbackCard = object : CallbackCreditCard {
                        override fun onChangeFilterCreditCard(cardCreditFilter: CardCreditFilter) {
                            idCardCredit = cardCreditFilter.id
                            currentCardCreditFilter = cardCreditFilter
                        }
                    })

                ChoiceDataComponent(idCardCredit, filter.month, callbackChoiceData)
            }
            ButtonsFooterContent(
                modifier = Modifier
                    .fillMaxWidth(),
                btnTextCancel = "CANCELAR",
                onClickCancel = { callback.onClick() },
                btnTextAccept = "SALVAR",
                onClickAccept = {

                    val saveFilter = ObjectFilter(
                        categoryCollection = categoryChoices,
                        priceMin = priceMin,
                        priceMax = priceMax,
                        idCard = idCardCredit,
                        month = month,
                        cardFilter = currentCardCreditFilter,
                    )
                    callback.onChangeObjectFilter(saveFilter)
                    callback.onClick()
                })
        }
    }
}