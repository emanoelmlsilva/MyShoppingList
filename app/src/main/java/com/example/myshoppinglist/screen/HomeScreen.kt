package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BoxCardCreditCustom
import com.example.myshoppinglist.components.BoxPurchaseHistoryComponent
import com.example.myshoppinglist.components.CarouselComponent
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.entities.CreditCard
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.theme.LatoBold
import com.example.myshoppinglist.ui.theme.text_secondary
import com.example.myshoppinglist.utils.MountStructureCrediCard
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(navController: NavController?, homeFieldViewModel: HomeFieldViewModel) {
    var visibleAnimation by remember { mutableStateOf(true) }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HeaderComponent(
                navController!!,
                homeFieldViewModel.getIdAvatar(),
                homeFieldViewModel.getNickName(),
                visibleAnimation,
                object : Callback {
                    override fun onClick() {
                        homeFieldViewModel.onChangeVisibleValue()
                    }
                })

            CarouselComponent(
                list = homeFieldViewModel.getCreditCardCollection(),
                visibleAnimation = visibleAnimation,
                parentModifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f),
                contentHeight = 265.dp,
                navController = navController
            )

            Spacer(Modifier.size(32.dp))

            Text(
                text = "Histórico Semanal",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                fontFamily = LatoBold,
                fontSize = 24.sp
            )

            Spacer(Modifier.size(24.dp))

            BoxPurchaseHistoryComponent(
                homeFieldViewModel.getPurchaseCollection(),
                object : VisibleCallback {
                    override fun onChangeVisible(visible: Boolean) {
                        if (visibleAnimation != visible) {
                            visibleAnimation = visible
                        }
                    }
                })
        }
    }

}

class HomeFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) : BaseFieldViewModel() {

    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    private val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    private val purchaseCollection: MutableLiveData<MutableList<PurchaseAndCategory>> =
        MutableLiveData(mutableListOf())
    private val creditCardCollection: MutableLiveData<MutableList<CreditCardDTODB>> =
        MutableLiveData(mutableListOf())
    private val idAvatar = MutableLiveData(0)
    private val nickName = MutableLiveData("")
    private var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getPurchaseCollection(): List<PurchaseAndCategory> {
        return purchaseCollection.value!!
    }

    fun getCreditCardCollection(): List<CreditCardDTODB> {
        return creditCardCollection.value!!
    }

    fun getIdAvatar(): Int {
        return idAvatar.value!!
    }

    fun getNickName(): String {
        return nickName.value!!
    }

    fun onChangeIdAvatar(newIdAvatar: Int) {
        idAvatar.value = newIdAvatar
    }

    fun onChangeNickName(newNickName: String) {
        nickName.value = newNickName
    }

    fun onChangeVisibleValue() {
        isVisibleValue.value = !isVisibleValue.value!!
    }

    fun getPurchaseController(): PurchaseController {
        return purchaseController
    }

    fun getCreditCardController(): CreditCardController {
        return creditCardController
    }

    fun onChangePurchaseCollection(newPurchaseCollection: List<PurchaseAndCategory>) {
        purchaseCollection.value!!.removeAll(purchaseCollection.value!!)
        purchaseCollection.value!!.addAll(newPurchaseCollection.reversed())
    }

    fun onChangeCreditCardCollection(newCreditCardCollection: List<CreditCard>) {
        creditCardCollection.value!!.removeAll(creditCardCollection.value!!)
        creditCardCollection.value!!.addAll(
            MountStructureCrediCard().mountSpedingDate(
                newCreditCardCollection
            )
        )
        creditCardCollection.value!!.add(CreditCardDTODB())

    }

    fun isVisibleValue(): Boolean {
        return isVisibleValue.value!!
    }

    fun getUser(email: String): LiveData<UserDTO> {
        return UserInstanceImpl.getUserViewModelCurrent().findUserByName(email)
    }

    fun onUpdateUser(email: String, lifecycleOwner: LifecycleOwner){
        UserInstanceImpl.getUserViewModelCurrent().findUserByName(email).observe(lifecycleOwner){
            onChangeNickName(it.nickName)
            onChangeIdAvatar(it.idAvatar)
        }
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

}