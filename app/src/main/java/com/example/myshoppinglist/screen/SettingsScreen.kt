@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BaseAnimationComponent
import com.example.myshoppinglist.components.BaseLazyColumnScroll
import com.example.myshoppinglist.components.DialogBackCustom
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.ConversionUtils
import org.burnoutcrew.reorderable.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(navController: NavHostController, idAvatar: Int, nickName: String) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
    var visibilityBackHandler by remember { mutableStateOf(false) }
    val creditCardCollection = remember {
        mutableStateListOf<CreditCardDTO>()
    }
    var visibleAnimation by remember {
        mutableStateOf(true)
    }
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val state = rememberReorderState() // 1.
    val tasks by createCardCreditViewModel.creditCardCollection.observeAsState(creditCardCollection)

    LaunchedEffect(key1 = idAvatar) {
        creditCardViewModel.getAll()
    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner) {
        creditCardCollection.addAll(it.map { creditCard ->
            CreditCardDTO().fromCreditCardDTO(
                creditCard
            )
        })

        createCardCreditViewModel.updateCreditCardCollection(it.map { creditCard ->
            CreditCardDTO().fromCreditCardDTO(
                creditCard
            )
        } as MutableList<CreditCardDTO>)
    }

    DialogBackCustom(visibilityBackHandler, {
        visibilityBackHandler = false
        navController.popBackStack()
    }, {
        visibilityBackHandler = false
    }, "Sair", "Deseja sair do aplicativo?")

    TopAppBarScreen(
        hasToolbar = true,
        hasDoneButton = true,
        colorDoneButton = text_primary_light,
        iconDone = R.drawable.ic_baseline_logout_24,
        onClickIcon = {
            navController.popBackStack()
        },
        onClickIconDone = { visibilityBackHandler = true },
        content = {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                BaseAnimationComponent(
                    visibleAnimation = visibleAnimation,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.padding(top = 24.dp, start = 24.dp)) {
                                Image(
                                    painter = painterResource(id = idAvatar),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(135.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, text_primary, CircleShape)
                                )
                            }

                            IconButton(onClick = {}) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_outline_brightness_6_24),
                                    contentDescription = null,
                                    tint = text_primary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = nickName,
                                Modifier.padding(start = 34.dp),
                                color = text_primary,
                                fontFamily = LatoBlack
                            )

                            IconButton(onClick = {
                                navController.navigate("${Screen.CreateUser.name}?isUpdate=${true}")
                            }, modifier = Modifier.padding(start = 16.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = text_primary
                                )
                            }
                        }
                    }
                }

                Column() {
                    Divider(
                        color = divider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                    Text(
                        text = "Cartões:",
                        fontFamily = LatoBlack,
                        modifier = Modifier.padding(start = 18.dp, top = 18.dp, bottom = 18.dp)
                    )

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        BaseLazyColumnScroll(
                            modifier = Modifier
                                .fillMaxWidth(.9f)
                                .reorderable(state, { fromPos, toPos ->
                                    createCardCreditViewModel.onTaskReordered(tasks, fromPos, toPos)
                                })
                                .detectReorderAfterLongPress(state),
                            listState = state.listState, callback = object : VisibleCallback() {
                                override fun onChangeVisible(visible: Boolean) {
                                    visibleAnimation = visible
                                }
                            }) {
                            items(tasks, key = { task -> task.idCard }) { creditCardDTO ->
                                BoxItemCard(creditCardDTO, state, object : Callback {
                                    override fun onClick() {
                                        navController.navigate(
                                            "${Screen.CreateCards.name}?hasToolbar=${true}?holderName=${creditCardDTO.holderName}?isUpdate=${true}?creditCardDTO=${
                                                ConversionUtils<CreditCardDTO>(CreditCardDTO::class.java).toJson(
                                                    listOf(creditCardDTO)
                                                )
                                            }"
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

@Composable
fun BoxItemCard(creditCardDTO: CreditCardDTO, state: ReorderableState, callBack: Callback) {
    Card(
        backgroundColor = Color(creditCardDTO.colorCard),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(.95f)
            .padding(vertical = 4.dp)
            .draggedItem(state.offsetByKey(creditCardDTO.idCard))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Image(
                    painter = painterResource(id = creditCardDTO.flagBlack),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                creditCardDTO.cardName,
                fontFamily = LatoBlack,
                color = text_secondary,
                fontSize = 24.sp,
            )
            IconButton(onClick = {
                callBack.onClick()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = text_primary
                )
            }
        }
    }
}