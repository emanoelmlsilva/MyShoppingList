package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BaseAnimationComponent
import com.example.myshoppinglist.components.BaseLazyColumnScroll
import com.example.myshoppinglist.components.IconCategoryComponent
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.AssetsUtils

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CategoriesScreen(navController: NavHostController?) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var visibleAnimation by remember { mutableStateOf(true) }
    val categoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val categoryCollection = remember { mutableListOf<Category>() }
    var categorySize by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = Unit) {
        categoryViewModel.getAll()
    }

    categoryViewModel.searchCollectionResult.observe(lifecycleOwner) {
        categoryCollection.removeAll(categoryCollection)
        categoryCollection.addAll(it)
        categorySize = it.size
    }

    fun onClick(idCategory: Long? = null){
        navController!!.navigate("${Screen.RegisterCategory.name}?idCategory=${idCategory ?: 0}")
    }

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, floatingActionButton = {
        if(visibleAnimation) FloatingActionButton(backgroundColor = primary_dark,
            onClick = {
                onClick()
            }) {
                Icon(Icons.Filled.Add,null, tint = background_card)
        }
    }, content = {
        Column(modifier = Modifier.padding(start = 26.dp, end = 26.dp, top = 26.dp)) {
            BaseAnimationComponent(
                visibleAnimation = visibleAnimation,
                contentBase = {
                    Card(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp)) {
                            Text("Categorias")
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(.24f)
                            ) {
                                Text(
                                    "${if (categorySize > 100) categorySize else "0$categorySize"}",
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
                })
            BaseLazyColumnScroll(
                visibleAnimation = visibleAnimation,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                callback = object : VisibleCallback() {
                    override fun onChangeVisible(visible: Boolean) {
                        if (visibleAnimation != visible) {
                            visibleAnimation = visible
                        }
                    }
                }
            ) {
                itemsIndexed(categoryCollection) { index, category ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                bottom = if (index == (categoryCollection.size - 1)) 56.dp else 0.dp
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
                                    iconCategory = AssetsUtils.readIconBitmapById(context, category.idImage)!!
                                        .asImageBitmap(),
                                    colorIcon = Color(category.color),
                                    size = 36.dp,
                                    enableClick = true
                                )

                                Text(text = category.category, modifier = Modifier.fillMaxWidth())
                            }
                            Row() {
                                IconButton(
                                    onClick = {
                                        onClick(category.id)
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_outline_mode_edit_outline_24),
                                        contentDescription = null,
                                        tint = text_primary,
                                    )
                                }
                                IconButton(
                                    enabled = false,
                                    onClick = {
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_outline_delete_outline_24),
                                        contentDescription = null,
                                        tint = text_title_secondary//text_primary,
                                    )
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
                }
            }
        }
    })

}