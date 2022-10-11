package com.example.myshoppinglist.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BaseAnimationComponent
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.Purchase
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.database.viewModels.PurchaseViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MountStructureCrediCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

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
    val purchaseCollection = remember { mutableStateListOf<Purchase>() }
    val price = remember { mutableStateOf<Double>(0.0) }
    val visibleAnimation = remember { mutableStateOf(true) }
    val creditCardColleciton = remember{ mutableStateListOf<List<CreditCardDTO>>()}

    LaunchedEffect(Unit) {
        userViewModel.getUserCurrent()
        creditCardViewModel.getAllWithSum()
        purchaseViewModel.sumPriceAllCard()
        purchaseViewModel.getPurchasesWeek()
    }
    purchaseViewModel.searchSumPriceResult.observe(lifecycleOwner.value) {
        price.value = it
    }

    purchaseViewModel.searchCollectionResults.observe(lifecycleOwner.value) { purchases ->
        purchaseCollection.removeAll(purchaseCollection)
        purchaseCollection.addAll(purchases.reversed())

    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner.value) { creditCollection ->
        if(!creditCollection.isEmpty()){
            creditCardColleciton.addAll(MountStructureCrediCard().mountSpedingDate(creditCollection))
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
            HeaderComponent(userViewModel, visibleAnimation.value, object : Callback {
                override fun onClick() {
                    homeFieldViewModel.onChangeVisibleValue()
                }
            })

            MySpending(
                visibleAnimation.value,
                price.value,
                creditCardColleciton,
                navController
            )
            Spacer(Modifier.size(32.dp))

            Text(
                text = "Histórico Semanal", modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), fontFamily = LatoBold, fontSize = 24.sp
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

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun MySpending(
    visibleAnimation: Boolean,
    priceTotal: Double,
    itemSpedingCollection: List<List<CreditCardDTO>>, navController: NavController?
) {
    val pagerState = rememberPagerState()
    val heightBoxCard = arrayOf(
        0.2f,0.35f,0.45f
    )
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, text_secondary),
        modifier = Modifier
            .padding(16.dp)
            .clickable(false, onClick = {
            })
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .fillMaxHeight(.1f)
                            .border(
                                width = 6.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = primary
                            )
                            .padding(start = 50.dp)
                    ) {}
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = "Saldo geral", fontFamily = LatoRegular)
                        Spacer(Modifier.size(12.dp))
                        Text(
                            text = "R$ ${MaskUtils.maskValue(priceTotal.toString())}",
                            fontFamily = LatoBold
                        )
                    }
                }
                Divider(
                    color = background_divider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
            BaseAnimationComponent(
                visibleAnimation = visibleAnimation,
                contentBase = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Meus gastos",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            fontFamily = LatoBold,
                            fontSize = 16.sp
                        )

                        if (itemSpedingCollection.isNotEmpty()) {
                            HorizontalPager(state = pagerState, count = itemSpedingCollection.size,  modifier = Modifier
                                .fillMaxHeight(heightBoxCard[itemSpedingCollection[0].size-1]), verticalAlignment = Alignment.Top) { page ->
                                val creditCardCollection = itemSpedingCollection[page]
                                Column(modifier = Modifier
                                    .fillMaxHeight(), verticalArrangement = Arrangement.Top) {
                                    repeat(creditCardCollection.size) { index ->
                                        var creditCardDTO: CreditCardDTO =
                                            creditCardCollection[index]

                                        ItemSpending(creditCardDTO, object : Callback {
                                            override fun onChangeIdCard(idCard: Long) {
                                                navController?.navigate("spending?idCard=${idCard}")
                                            }
                                        })
                                    }
                                }
                            }
                            Row {
                                repeat(itemSpedingCollection.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = 12.dp)
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .border(1.dp, text_secondary, CircleShape)
                                            .background(if (pagerState.currentPage == index) card_green_dark else secondary_light)
                                    )
                                }
                            }
                        }
                    }
                }
            )

        }
    }

}

@Composable
fun ItemSpending(creditCardDTO: CreditCardDTO, callBack: Callback) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { callBack.onChangeIdCard(creditCardDTO.idCard) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color(creditCardDTO.colorCard)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = creditCardDTO.flag),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                Modifier
                    .fillMaxWidth(.6f)
            ) {
                Text(
                    text = creditCardDTO.cardName,
                    fontFamily = LatoBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(text = creditCardDTO.holderName, fontFamily = LatoRegular)
            }
            Text(
                text = "R$ ${MaskUtils.maskValue(creditCardDTO.value.toString())}",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = LatoBold,
                fontSize = 18.sp,
                color = text_money
            )
        }

        Divider(
            color = background_divider,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
    }
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