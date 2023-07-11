package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.services.dtos.ItemListDTO
import com.example.myshoppinglist.ui.theme.*

@Composable
fun SlidingItemListComponent(
    context: Context,
    itemListDTO: ItemListDTO,
    isCheck: Boolean,
    isMarket: Boolean,
    sizeCheckCollection: Boolean = true,
    isRemoved: Boolean = false,
    idItem: Long,
    category: Category,
    product: String,
    price: Float? = 0F,
    discount: Float? = 0F,
    isCheckDiscount: Boolean? = false,
    amountOrKilo: String? = "",
    type: TypeProduct? = TypeProduct.QUANTITY,
    callback: CallbackItemList?,
    callbackPrice: CustomTextFieldOnClick? = null,
    callbackQuantOrKilo: CustomTextFieldOnClick? = null,
    callbackDiscount: CustomTextFieldOnClick? = null,
) {
    Card(
        Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        BoxItemListComponent(
            context,
            itemListDTO,
            isMarket,
            isCheck,
            isRemoved,
            idItem,
            category,
            product,
            price,
            discount,
            isCheckDiscount,
            amountOrKilo,
            type,
            callback,
            callbackPrice,
            callbackQuantOrKilo,
            callbackDiscount
        )
    }
}