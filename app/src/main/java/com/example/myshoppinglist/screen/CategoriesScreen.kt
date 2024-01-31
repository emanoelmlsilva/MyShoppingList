package com.example.myshoppinglist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackSwipe
import com.example.myshoppinglist.components.BaseLazyColumnScroll
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.components.SwipeComponent
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.fieldViewModel.CategoryFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils

@Composable
fun CategoriesScreen(
    navController: NavHostController?,
    categoryFieldViewModel: CategoryFieldViewModel
) {
    val context = LocalContext.current
    val categoryCollection by categoryFieldViewModel.categoryCollection.observeAsState(emptyList())

    var activeItem by remember { mutableStateOf(-1) }
    var currentDraggedItem by remember { mutableStateOf(-1) }

    fun onClick(idCategory: Long? = null) {
        if ((currentDraggedItem != -1 && idCategory != null) || idCategory == null ) {
            navController!!.navigate("${Screen.RegisterCategory.name}?idCategory=${idCategory ?: 0}")
        }
    }

    TopAppBarScreen(
        onClickIcon = { navController?.popBackStack() },
        paddingFloatingButton = 65.dp,
        floatingActionButton = {
            FloatingActionButton(backgroundColor = primary_dark,
                onClick = {
                    onClick()
                }) {
                Icon(Icons.Filled.Add, null, tint = background_card)
            }
        },
        content = {
            Column(modifier = Modifier.padding(start = 26.dp, end = 26.dp, top = 26.dp)) {
                val size by categoryFieldViewModel.categorySize.observeAsState(0)

                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            vertical = 12.dp,
                            horizontal = 18.dp
                        )
                    ) {
                        Text("Categorias")
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(.24f)
                        ) {
                            Text(
                                "${if (size > 100) size else "0$size"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_category_24),
                                contentDescription = null,
                                tint = primary_dark,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.24f)
                                .height(5.dp)
                                .border(
                                    width = 6.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = primary
                                )
                                .padding(start = 50.dp)
                        ) {}
                    }
                }
                if (categoryCollection.isNotEmpty()) {
                    BaseLazyColumnScroll(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        itemsIndexed(categoryCollection) { index, category ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(secondary)
                                    .padding(
                                        bottom = if (index == (size - 1)) 56.dp else 0.dp
                                    )
                            ) {
                                SwipeComponent(
                                    index = index,
                                    onSwipe = { activeItem = it },
                                    onDragStart = { currentDraggedItem = it },
                                    onDragEnd = { currentDraggedItem = -1 },
                                    colorBackground = secondary,
                                    callback = object : CallbackSwipe {
                                        override fun onHandlerLeftAction() {

                                        }

                                        override fun onHandlerHighAction() {
                                            onClick(category.myShoppingId)
                                        }
                                    },
                                    dismissBackground = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(secondary)
                                                .padding(
                                                    top = 16.dp
                                                )
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(.8f),
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
                                                        size = 36.dp,
                                                        enableClick = true
                                                    )

                                                    Text(
                                                        text = category.category,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }
                                            Divider(
                                                color = divider,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        })

}

