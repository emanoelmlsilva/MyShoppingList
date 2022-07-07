package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.components.CardCreditComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.enums.TypeCard
import com.example.myshoppinglist.ui.theme.*

@Composable
fun CreditCollectionScreen(navController: NavController?){
    val createCardCreditViewModel: CreateCardCreditFieldViewModel = viewModel()
    var context = LocalContext.current
    val creditCardViewModel = CreditCardViewModel(context)
    val listState = rememberLazyListState()
    val creditCardCollectionFieldViewModel = CreditCollectionFieldViewModel()
    val creditCardDTOCollection = creditCardCollectionFieldViewModel.creditCardCollection.observeAsState(initial = listOf<CreditCardDTO>()).value

    creditCardViewModel.getAll()

    creditCardViewModel.searchCollectionResult.observeForever {
        creditCardCollectionFieldViewModel.onChangeCreditCardCollectin(generateCardCredit(it))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {navController?.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Menu Btn",
                            tint = secondary_dark
                        )
                    }
                },
                backgroundColor = text_secondary,
                elevation = 0.dp
            )
        }
    ) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                itemsIndexed(creditCardDTOCollection) { index, card ->
                    var cardCurrent = creditCardDTOCollection[listState.firstVisibleItemIndex]
                    CardCreditComponent(navController = navController,
                        isClicable = card.holderName.isBlank(),
                        isDefault = card.holderName.isBlank(),
                        typeCard = card.typeCard,
                        isChoiceColor = false,
                        cardCreditDTO = card,
                        createCardCreditViewModel = createCardCreditViewModel,
                        modifier = Modifier
                            .width(300.dp)
                            .height(400.dp),
                        callbackColor = null
                    )
                }
            }
    }
}

fun generateCardCredit(creditCardCollection: List<CreditCard>): List<CreditCardDTO>{
    var creditCardDTOCollection: MutableList<CreditCardDTO> =
        creditCardCollection.map { CreditCardDTO(it.cardName, it.holderName, it.value, it.colorCard, it.typeCard) }.toMutableList()
    creditCardDTOCollection.add(0, CreditCardDTO("", "", 0F ))
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
