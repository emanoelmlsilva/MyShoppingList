package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BaseAnimationComponent
import com.example.myshoppinglist.components.BoxCardCreditCustom
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_secondary
import com.example.myshoppinglist.utils.MountStructureCrediCard
import com.google.accompanist.pager.ExperimentalPagerApi
import java.lang.Math.abs

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(navController: NavController?) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val homeFieldViewModel = HomeFieldViewModel()
    val purchaseViewModel = PurchaseViewModel(context)
    val userViewModel = UserViewModel(context)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner.value)
    val purchaseCollection = remember { mutableStateListOf<PurchaseAndCategory>() }
    val visibleAnimation = remember { mutableStateOf(true) }
    val creditCardCollection = remember { mutableStateListOf<CreditCardDTO>() }
    var idAvatar by remember {
        mutableStateOf(R.drawable.default_avatar)
    }
    var nickName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        creditCardViewModel.getAllWithSum()
    }

    UserInstanceImpl.getUserViewModelCurrent().searchResult.observe(lifecycleOwner.value){
        idAvatar = it.idAvatar;
        nickName = it.nickName
    }

    purchaseViewModel.searchPurchaseAndCategory.observe(lifecycleOwner.value) { purchases ->
        purchaseCollection.removeAll(purchaseCollection)
        purchaseCollection.addAll(purchases.reversed())

    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner.value) { creditCollection ->
        if (creditCollection.isNotEmpty()) {
            purchaseViewModel.getPurchasesAndCategoryWeek()

            creditCardCollection.removeAll(creditCardCollection)
            creditCardCollection.add(CreditCardDTO())
            creditCardCollection.addAll(MountStructureCrediCard().mountSpedingDate(creditCollection))
        }
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HeaderComponent(userViewModel, idAvatar, nickName, visibleAnimation.value, object : Callback {
                override fun onClick() {
                    homeFieldViewModel.onChangeVisibleValue()
                }
            })

            Carousel(
                visibleAnimation = visibleAnimation.value,
                count = creditCardCollection.size,
                parentModifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f),
                contentHeight = 265.dp,
                content = { modifier, index ->
                    val creditCardDTO = creditCardCollection[index]

                    Column(modifier = modifier) {
                        BoxCardCreditCustom(creditCardDTO, navController!!)
                    }
                }
            )

            Spacer(Modifier.size(32.dp))

            Text(
                text = "Histórico Semanal",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                fontFamily = LatoBold,
                fontSize = 24.sp
            )

            Spacer(Modifier.size(24.dp))

            BoxPurchaseHistoryComponent(
                visibleAnimation.value,
                purchaseCollection,
                object : VisibleCallback() {
                    override fun onChangeVisible(visible: Boolean) {
                        if (visibleAnimation.value != visible) {
                            visibleAnimation.value = visible
                        }
                    }
                })
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Carousel(
    count: Int,
    visibleAnimation: Boolean = true,
    parentModifier: Modifier = Modifier.fillMaxWidth(),
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {

    val listState = rememberLazyListState(count / 2)

    LaunchedEffect(key1 = count) {
        listState.animateScrollToItem(count)
    }

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {
            BoxWithConstraints(
                modifier = parentModifier
            ) {
                val halfRowWidth = constraints.maxWidth / 2

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(),
                    verticalArrangement = Arrangement.spacedBy(-contentHeight / 3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = count,
                        itemContent = { globalIndex ->

                            val scale by remember {
                                derivedStateOf {
                                    val currentItem =
                                        listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == globalIndex }
                                            ?: return@derivedStateOf 0.85f

                                    (1f - minOf(
                                        1f,
                                        abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth).toFloat() / halfRowWidth
                                    ) * 0.3f)
                                }
                            }

                            content(
                                index = globalIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(contentHeight)
                                    .scale(scale)
                                    .zIndex(scale)
                            )
                        }
                    )
                }
            }
        })
}

class HomeFieldViewModel : BaseFieldViewModel() {
    var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)

    fun onChangeVisibleValue() {
        isVisibleValue.value = !isVisibleValue.value!!
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}