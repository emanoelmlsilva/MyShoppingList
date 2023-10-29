package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModelDB
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.ConversionUtils
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import org.burnoutcrew.reorderable.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(navController: NavHostController, idAvatar: Int, nickName: String) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    var visibilityBackHandler by remember { mutableStateOf(false) }
    var visibleAnimation by remember {
        mutableStateOf(true)
    }
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val state = rememberReorderState()
    val creditCardCollection by createCardCreditViewModel.creditCardCollection.observeAsState(
        mutableListOf()
    )
    val userViewModel: UserViewModelDB = UserViewModelDB(context)

    fun updatePositionCreditCard() {
        creditCardCollection.toObservable().subscribeBy(onNext = {
            creditCardController.updateCreditCardDB(it.toCreditCard())
        }, onError = { throwable -> {} }, onComplete = { })
    }

    LaunchedEffect(key1 = idAvatar) {
        creditCardController.findAllDB().observe(lifecycleOwner) {
            creditCardCollection?.addAll(it.map { creditCard ->
                CreditCardDTODB().fromCreditCardDTODB(
                    creditCard
                )
            })

            createCardCreditViewModel.updateCreditCardCollection(it.map { creditCard ->
                CreditCardDTODB().fromCreditCardDTODB(
                    creditCard
                )
            } as MutableList<CreditCardDTODB>)
        }
    }

    LaunchedEffect(key1 = state.draggedIndex) {
        if (state.draggedIndex == null) {
            updatePositionCreditCard()
        }
    }

    DialogBackCustom(visibilityBackHandler, {
        UserLoggedShared.logout()
        userViewModel.deleteUser()
        navController.navigate(Screen.ChoiceLogin.name) {
            popUpTo(0) { inclusive = false }
        }
    }, {
        visibilityBackHandler = false
    }, "Sair", "Deseja sair do aplicativo?")

    TopAppBarScreen(hasToolbar = true,
        hasDoneButton = true,
        colorDoneButton = text_primary_light,
        iconDone = R.drawable.ic_baseline_logout_24,
        onClickIcon = {
            navController.popBackStack()
        },
        onClickIconDone = {
            visibilityBackHandler = true
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BaseAnimationComponent(
                    visibleAnimation = visibleAnimation,
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 65.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = idAvatar),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(135.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, text_primary, CircleShape)
                            )

                            Text(
                                text = nickName,
                                Modifier.padding(vertical = 12.dp),
                                color = text_primary,
                                fontFamily = LatoBlack
                            )
                        }

                        IconButton(onClick = {
                            navController.navigate("${Screen.CreateUser.name}?isUpdate=${true}?hasToolbar=${true}")
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = text_primary
                            )
                        }
                    }
                }

                Column() {
                    Divider(
                        color = divider, modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                    Text(
                        text = "Cartões:",
                        fontFamily = LatoBlack,
                        modifier = Modifier.padding(start = 18.dp, top = 18.dp, bottom = 18.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BaseLazyColumnScroll(modifier = Modifier
                            .fillMaxWidth(.9f)
                            .fillMaxHeight(.9f)
                            .reorderable(state, { fromPos, toPos ->
                                createCardCreditViewModel.onTaskReordered(
                                    creditCardCollection, fromPos, toPos
                                )
                            })
                            .detectReorderAfterLongPress(state),
                            listState = state.listState,
                            callback = object : VisibleCallback {
                                override fun onChangeVisible(visible: Boolean) {
                                    visibleAnimation = visible
                                }
                            }) {
                            items(creditCardCollection,
                                key = { creditCard -> creditCard.myShoppingId }) { creditCardDTO ->
                                BoxItemCard(creditCardDTO, state, object : Callback {
                                    override fun onClick() {
                                        navController.navigate(
                                            "${Screen.CreateCards.name}?hasToolbar=${true}?holderName=${creditCardDTO.holderName}?isUpdate=${true}?creditCardDTO=${
                                                ConversionUtils<CreditCardDTODB>(CreditCardDTODB::class.java).toJsonList(
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
fun BoxItemCard(creditCardDTO: CreditCardDTODB, state: ReorderableState, callBack: Callback) {
    Card(
        backgroundColor = Color(creditCardDTO.colorCard),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(.95f)
            .padding(vertical = 4.dp)
            .draggedItem(state.offsetByKey(creditCardDTO.myShoppingId))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
                Image(
                    painter = painterResource(id = creditCardDTO.fromFlagBlack()),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                creditCardDTO.cardName,
                fontFamily = LatoBlack,
                color = text_secondary,
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(.7f)
            )

            Row {
                IconButton(onClick = {
                    callBack.onClick()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = text_primary
                    )
                }

                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null,
                        tint = text_primary
                    )
                }
            }
        }
    }
}