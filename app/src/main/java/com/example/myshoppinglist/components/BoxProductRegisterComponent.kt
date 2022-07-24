package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.enums.TypeCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.screen.PurchaseInfo
import com.example.myshoppinglist.ui.theme.divider
import com.example.myshoppinglist.ui.theme.secondary_light
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.MaskUtils
import java.util.*

@ExperimentalAnimationApi
@Composable
fun BoxProductRegisterComponent(purchaseInfoCollection: MutableList<PurchaseInfo>) {
    val expandeds = remember { mutableStateListOf<Int>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        purchaseInfoCollection.mapIndexed { index, purchaseInfo ->
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(modifier = Modifier
                            .padding(4.dp)
                            , onClick = {
                            val auxExpandeds = expandeds.toMutableList()
                            expandeds.removeAll(expandeds)
                            expandeds.addAll(changeVisibility(index, auxExpandeds))
                        }) {
                            Icon(
                                imageVector = if (isExpanded(
                                        index,
                                        expandeds
                                    )
                                ) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = text_primary,
                            )
                        }
                        Text(text = purchaseInfo.title, modifier = Modifier.padding(start = 8.dp))
                    }
                    Divider(
                        color = secondary_light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }
            }
            if (isExpanded(index, expandeds)) items(purchaseInfo.purchaseCollection) { purchase ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = purchase.category.imageCircle),
                            contentDescription = null,
                            Modifier
                                .size(46.dp)
                                .padding(top = 3.dp)
                        )
                        Column {
                            Row(horizontalArrangement = Arrangement.End) {
                                Text(
                                    text = purchase.name, modifier = Modifier
                                        .padding(start = 12.dp)
                                        .fillMaxWidth(.6f)
                                )
                                Text(text = "${purchase.quantiOrKilo} ${if (purchase.typeProduct == TypeProduct.QUANTITY) "UN" else "Kg"}")
                                Text(
                                    text = "R$ ${MaskUtils.maskValue(purchase.price.toString())}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 3.dp),
                                    textAlign = TextAlign.End
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp), horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {})
                                    {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            tint = text_primary,
                                        )
                                    }
                                IconButton(onClick = {})
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = null,
                                        tint = text_primary,
                                    )
                                }

                            }
                        }
                    }
                    Divider(
                        color = divider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }


            } else null
        }
    }
}

fun changeVisibility(index: Int, visibilityCollection: MutableList<Int>): MutableList<Int> {
    if (visibilityCollection.indexOf(index) != -1) {
        visibilityCollection.remove(index)
    } else {
        visibilityCollection.add(index)
    }

    return visibilityCollection
}

fun isExpanded(index: Int, visibilityCollection: MutableList<Int>): Boolean {
    return visibilityCollection.indexOf(index) != -1
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun PreviewBoxProductRegisterComponent() {

    BoxProductRegisterComponent(
        mutableListOf(
            PurchaseInfo(
                "Lula Mercadinho",
                mutableListOf(
                    Purchase(
                        "Biscoito",
                        "Lula Mercadinho",
                        0,
                        "1",
                        TypeProduct.QUANTITY,
                        "13-06-2022",
                        "0.0".toDouble(),
                        TypeCategory.DRINKS
                    ),
                    Purchase(
                        "Biscoito",
                        "Lula Mercadinho",
                        0,
                        "1",
                        TypeProduct.QUANTITY,
                        "13-06-2022",
                        "0.0".toDouble(),
                        TypeCategory.DRINKS
                    )
                )
            )
        )
    )
}