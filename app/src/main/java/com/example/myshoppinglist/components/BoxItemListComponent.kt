package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxItemListComponent(
    context: Context,
    itemListDTO: ItemListDTO,
    isMarket: Boolean,
    isCheck: Boolean,
    hasOptionEdit: Boolean,
    isRemoved: Boolean = false,
    idItem: Long,
    category: Category,
    product: String,
    price: Float?,
    discount: Float?,
    isCheckDiscount: Boolean?,
    amountOrKilo: String?,
    type: TypeProduct?,
    callback: CallbackItemList?,
    callbackPrice: CustomTextFieldOnClick?,
    callbackAmountOrKilo: CustomTextFieldOnClick?,
    callbackDiscount: CustomTextFieldOnClick?
) {

    var enabledDeleteDialog by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }
    var isInitial by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isCheck) {
        if (!isCheck && showOptions) {
            showOptions = false
        }
    }

    Column(
        modifier = Modifier
            .background(background_card_light)
            .fillMaxWidth()
    ) {

        DialogBackCustom(enabledDeleteDialog, {
            callback?.onDelete()
            enabledDeleteDialog = false
        }, {
            enabledDeleteDialog = false
        }, "Deletar", "Deseja remover o item $product da lista?")

        Row(
            modifier = Modifier.height(65.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(.75f)
            ) {

                IconCategoryComponent(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    iconCategory = AssetsUtils.readIconBitmapById(
                        context,
                        category.idImage
                    )!!
                        .asImageBitmap(),
                    colorIcon = Color(category.color),
                    size = 40.dp,
                    enabledBackground = true
                )

                Text(
                    product,
                    fontFamily = LatoBlack,
                    fontSize = 18.sp,
                    textDecoration = if (isMarket && isCheck) TextDecoration.LineThrough else TextDecoration.None
                )

                if (isRemoved) {
                    Spacer(
                        Modifier
                            .width(15.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.adjust),
                        contentDescription = null,
                        tint = text_money
                    )
                }


            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(modifier = Modifier
                    .size(if (isCheck && !isMarket && hasOptionEdit) 30.dp else 50.dp),
                    colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                    checked = isCheck,
                    onCheckedChange = {
                        if (isInitial) {
                            isInitial = false
                        }
                        callback?.onChangeValue(idItem)
                    }
                )

                if (isCheck && !isMarket && hasOptionEdit) {
                    IconButton(
                        modifier = Modifier.size(32.dp),
                        onClick = { showOptions = !showOptions }) {
                        Icon(
                            imageVector = if (showOptions) Icons.Filled.KeyboardArrowRight else Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "show screen purchase",
                            tint = secondary_dark,
                        )
                    }

                    Column(
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        if (showOptions) {

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        showOptions = false
                                        enabledDeleteDialog = true
                                    }) {
                                Icon(
                                    modifier = Modifier
                                        .background(text_primary)
                                        .padding(6.dp)
                                        .size(16.dp),
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = null,
                                    tint = text_secondary,
                                )
                            }

                            Spacer(
                                Modifier
                                    .height(2.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        callback?.onUpdate(itemListDTO)
                                        showOptions = false
                                    }) {
                                Icon(
                                    modifier = Modifier
                                        .background(text_primary)
                                        .padding(6.dp)
                                        .size(16.dp),
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null,
                                    tint = text_secondary,
                                )
                            }
                        }

                    }
                }

            }

        }

        BoxItemInfo(
            isInitial,
            isMarket && !isCheck,
            price,
            discount,
            isCheckDiscount!!,
            amountOrKilo,
            type,
            callbackPrice,
            callbackAmountOrKilo,
            callbackDiscount
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxItemInfo(
    isInitial: Boolean = false,
    isMarket: Boolean,
    newPrice: Float?,
    newDiscount: Float?,
    isCheckDiscount: Boolean,
    newQuantOrKilo: String?,
    newType: TypeProduct?,
    callbackPrice: CustomTextFieldOnClick?,
    callbackQuantOrKilo: CustomTextFieldOnClick?,
    callbackDiscount: CustomTextFieldOnClick?
) {
    var isCheck by remember { mutableStateOf(false) }

    if (isMarket) {
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(6.dp),
            backgroundColor = text_secondary,
            modifier = Modifier
                .fillMaxWidth(.98f)
                .padding(start = 16.dp, bottom = 8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    NumberInputComponent(
                        maxChar = 13,
                        keyboardType = KeyboardType.Number,
                        value = MaskUtils.convertValueDoubleToString(newPrice!!.toDouble()),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 16.dp),
                        label = "Preço",
                        error = !isInitial && (newPrice.toString() == "0.00" || newPrice.toString() == "0.0"),
                        customOnClick = callbackPrice!!
                    )

                    NumberInputComponent(
                        maxChar = 11,
                        hasIcon = true,
                        isKilogram = newType == TypeProduct.KILO,
                        value = newQuantOrKilo,
                        error = !isInitial && (newQuantOrKilo!!.isBlank() || newQuantOrKilo == "0" || newQuantOrKilo == "0.000"),
                        isMandatory = false,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .fillMaxWidth(.99f),
                        label = if (newType == TypeProduct.QUANTITY) "Quantidade" else "Quilograma",
                        customOnClick = callbackQuantOrKilo!!
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                            checked = isCheck,
                            onCheckedChange = {
                                isCheck = !isCheck

                                callbackDiscount!!.onChangeValue(isCheck)

                                if (!isCheck) {
                                    callbackDiscount.onChangeValue("0")
                                }
                            }
                        )

                        Text(text = "Desconto", fontFamily = LatoBlack)
                    }

                    if (isCheck) {
                        NumberInputComponent(
                            maxChar = 13,
                            keyboardType = KeyboardType.Number,
                            value = newDiscount.toString(),
                            modifier = Modifier
                                .fillMaxWidth(0.45f)
                                .padding(end = 16.dp),
                            label = "Desconto",
                            error = !isInitial && isCheckDiscount && newDiscount == 0F,
                            customOnClick = callbackDiscount!!
                        )
                    }

                    Spacer(Modifier.height(3.dp))

                    Divider(
                        color = divider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                }
            }
        }
    }
}