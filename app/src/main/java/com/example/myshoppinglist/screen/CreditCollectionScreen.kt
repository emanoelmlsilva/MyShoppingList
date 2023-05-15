package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.components.CardCreditComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreateCardCreditFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
@Composable
fun CreditCollectionScreen(navController: NavController?){
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val creditCardViewModel = CreditCardViewModel(context, lifecycleOwner.value)
    val creditCardCollectionFieldViewModel = CreditCollectionFieldViewModel()
    val creditCardDTOCollection = creditCardCollectionFieldViewModel.creditCardCollection.observeAsState(initial = listOf<CreditCardDTO>()).value
    val pagerState = rememberPagerState()

    LaunchedEffect(Unit){
        creditCardViewModel.getAllWithSum()
    }

    creditCardViewModel.searchCollectionResult.observe(lifecycleOwner.value) {
        creditCardCollectionFieldViewModel.onChangeCreditCardCollectin(generateCardCredit(it))
    }

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, content = {
        Column(modifier = Modifier.fillMaxSize()){
                HorizontalPager(state = pagerState, contentPadding = PaddingValues(start = 26.dp, end = 14.dp),
                    count = creditCardDTOCollection.size,  modifier = Modifier.fillMaxHeight(.25f)) { page ->
                    val card = creditCardDTOCollection[page]

                    CardCreditComponent(navController = navController,
                        isClicable = card.holderName.isBlank(),
                        isDefault = card.holderName.isBlank(),
                        typeCard = card.typeCard,
                        flagCardCredit = card.flag,
                        isChoiceColor = false,
                        cardCreditDTO = card,
                        createCardCreditViewModel = createCardCreditViewModel,
                        modifier = Modifier.fillMaxWidth(.95f),
                        callbackColor = null
                    )
                }
        }
    })
}

fun generateCardCredit(creditCardCollection: List<CreditCard>): List<CreditCardDTO>{
    var creditCardDTOCollection: MutableList<CreditCardDTO> =
        creditCardCollection.map { CreditCardDTO(idCard = it.id, cardName = it.cardName, holderName = it.holderName, value = it.value, colorCard = it.colorCard, typeCard = it.typeCard, flag = it.flag) }.toMutableList()
    creditCardDTOCollection.add(CreditCardDTO(idCard = 0L, cardName = "", holderName = "", value = 0F ))
    return creditCardDTOCollection;
}

class CreditCollectionFieldViewModel: BaseFieldViewModel(){
    val creditCardCollection: MutableLiveData<List<CreditCardDTO>> = MutableLiveData<List<CreditCardDTO>>()

    fun onChangeCreditCardCollectin(newCreditCardCollection: List<CreditCardDTO>){
        creditCardCollection.value = newCreditCardCollection
    }
    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}
