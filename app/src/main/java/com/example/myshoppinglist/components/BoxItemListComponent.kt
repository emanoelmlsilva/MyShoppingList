package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils


@Composable
fun BoxItemListComponent(
    modifier: Modifier = Modifier,
    context: Context,
    isMarket: Boolean,
    isCheck: Boolean,
    isRemoved: Boolean = false,
    idItem: Long,
    category: Category,
    product: String,
    backgroundColor: Color,
    price: Float?,
    quantOrKilo: String?,
    type: TypeProduct?,
    callback: CallbackItemList?,
    callbackPrice: CustomTextFieldOnClick?,
    callbackQuantOrKilo: CustomTextFieldOnClick?
) {

    Column(
        modifier = modifier
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
                        .width(5.dp)
                )

                Text(
                    product,
                    fontFamily = LatoBlack,
                    fontSize = 18.sp,
                    textDecoration = if (isMarket && isCheck) TextDecoration.LineThrough else TextDecoration.None
                )

                if(isRemoved){
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
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                checked = isCheck,
                onCheckedChange = {
                    callback?.onChangeValue(idItem)
                }
            )
        }

        BoxItemInfo(isMarket && !isCheck, price, quantOrKilo, type, callbackPrice, callbackQuantOrKilo)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxItemInfo(isMarket: Boolean, newPrice: Float?, newQuantOrKilo: String?, newType: TypeProduct?, callbackPrice: CustomTextFieldOnClick?, callbackQuantOrKilo: CustomTextFieldOnClick?) {
    var isMoney by remember { mutableStateOf(true) }
    var price by remember { mutableStateOf(newPrice.toString()) }
    var quantOrKilo by remember { mutableStateOf(newQuantOrKilo) }
    var type by remember {
        mutableStateOf(newType)
    }
    var priceError by remember{ mutableStateOf(false) }
    var quantOrKiloError by remember{ mutableStateOf(false) }
    var isMarketCurrent by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = newPrice){
        price = newPrice.toString()
        priceError = price == "0.00" || price == "0.0"
    }

    LaunchedEffect(key1 = newQuantOrKilo){
        quantOrKilo = newQuantOrKilo!!
        quantOrKiloError = quantOrKilo!!.isBlank() || quantOrKilo == "0" || quantOrKilo == "0.000"
    }

    LaunchedEffect(key1 = newType){
        type = newType!!
    }

    LaunchedEffect(key1 = isMarket){
        isMarketCurrent = isMarket
    }
    if(isMarketCurrent){
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(6.dp),
            backgroundColor = text_secondary,
            modifier = Modifier
                .fillMaxWidth(.98f)
                .padding(start = 16.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                NumberInputComponent(maxChar = 13,
                    keyboardType = KeyboardType.Number,
                    value = price,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 16.dp),
                    label = "Preço",
                    error = priceError,
                    customOnClick = callbackPrice!!
                )

                NumberInputComponent(
                    maxChar = 11,
                    hasIcon = true,
                    isKilogram = type == TypeProduct.KILO,
                    value = quantOrKilo,
                    error = quantOrKiloError,
                    isMandatory = false,
                    modifier = Modifier
                        .padding(vertical = 1.dp)
                        .fillMaxWidth(.99f),
                    label = if (isMoney) "Quantidade" else "Quilograma",
                    customOnClick = callbackQuantOrKilo!!
                )

            }
        }
    }
}