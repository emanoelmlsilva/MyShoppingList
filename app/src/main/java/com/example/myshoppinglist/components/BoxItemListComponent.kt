package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.primary_dark
import com.example.myshoppinglist.utils.AssetsUtils


@Composable
fun BoxItemListComponent(
    modifier: Modifier = Modifier,
    context: Context,
    isCheck: Boolean,
    idItem: Long,
    category: Category,
    product: String,
    backgroundColor: Color,
    callback: Callback
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
                        .width(15.dp)
                )

                Text(product, fontFamily = LatoBlack)
            }
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = primary_dark),
                checked = isCheck,
                onCheckedChange = {
                    callback.onChangeValue(idItem)
                }
            )
        }

        BoxItemInfo()
    }
}

@Composable
fun BoxItemInfo(){
    Row{

    }
}