package com.example.myshoppinglist.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.components.BaseLazyColumnScroll
import com.example.myshoppinglist.components.DialogBackCustom
import com.example.myshoppinglist.components.EmptyTextComponent
import com.example.myshoppinglist.components.StatusSaveDataComponent
import com.example.myshoppinglist.database.dtos.PurchaseDTO
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.enums.StatusSaveData
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.enums.TypeStatus
import com.example.myshoppinglist.fieldViewModel.PurchaseViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils
import org.burnoutcrew.reorderable.*

@Composable
fun RepeatPurchaseScreen(
    navController: NavController,
    idCard: Long,
    purchaseViewModel: PurchaseViewModel
) {

    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val state = rememberReorderState()
    var visibilityRemove by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<StatusSaveData?>(null) }

    val purchaseList = remember { mutableStateListOf<PurchaseDTO>() }
    var purchaseCurrent by remember { mutableStateOf(PurchaseDTO()) }

    fun popBackStack() {
        navController.popBackStack()
        navController.navigate("${Screen.Spending.name}?idCard=${idCard}")
    }

    purchaseViewModel.getPurchaseList().observe(lifecycleOwner) {
        purchaseList.clear()
        purchaseList.addAll(it)
    }

    BackHandler {
        popBackStack()
    }

    TopAppBarScreen(hasToolbar = true,
        colorDoneButton = text_primary_light,
        iconDone = R.drawable.ic_baseline_logout_24,
        onClickIcon = {
            popBackStack()
        },
        onClickIconDone = {
        },
        content = {
            DialogBackCustom(
                visibilityRemove,
                {
                    purchaseViewModel.removeRepeatPurchase(purchaseCurrent, object : Callback {
                        override fun onSuccess() {
                            Log.d("TESTANDO", "COMPLETADO")
                            purchaseViewModel.getPurchaseAllWithRepeatByIdCard(purchaseCurrent.purchaseCardId)
                            status = null
                        }

                        override fun onChangeStatus(newStatus: StatusSaveData) {
                            status = newStatus
                        }

                        override fun onFailed(messageError: String) {
                            status = null
                        }

                    })
                    visibilityRemove = false
                },
                {
                    visibilityRemove = false
                },
                "Deseja remover pagamento da compra automatica?",
                context.getString(R.string.remove_purchase_message, purchaseCurrent.name)
            )

            status?.let {
                StatusSaveDataComponent(
                    isFlowDelete = true,
                    visibility = true,
                    status = it,
                    statusMain = R.raw.delete_full
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Lista de Compras Repetidas",
                        fontFamily = LatoBlack,
                        color = primary_dark,
                        fontSize = 18.sp
                    )

                    Spacer(
                        Modifier
                            .height(35.dp)
                    )

                    Divider(
                        color = secondary_dark,
                        modifier = Modifier
                            .height(1.dp)
                    )

                    Spacer(
                        Modifier
                            .height(35.dp)
                    )

                }

                if (purchaseList.isNotEmpty()) {
                    BaseLazyColumnScroll(
                        listState = state.listState,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                    ) {
                        items(purchaseList,
                            key = { purchase -> purchase.myShoppingId }) { purchaseDTO ->
                            BoxItemPurchase(purchaseDTO,
                                object : Callback {
                                    override fun onClick() {
                                        purchaseCurrent = purchaseDTO
                                        visibilityRemove = true
                                    }
                                })
                        }
                    }
                } else {
                    EmptyTextComponent("Não foi habilitado nenhuma repetição de comprar.")
                }

            }
        }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxItemPurchase(purchaseDTO: PurchaseDTO, callBack: Callback) {
    Card(
        backgroundColor = background_text_field_dark,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(.95f)
            .padding(vertical = 4.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth(.85f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(
                    "R$ ${
                        MaskUtils.maskValue(
                            MaskUtils.convertValueDoubleToString(
                                (if (purchaseDTO.typeProduct == TypeProduct.QUANTITY) (purchaseDTO.amountOrKilo.toInt() * purchaseDTO.price) else purchaseDTO.price)
                            )
                        )
                    }",
                    fontFamily = LatoBlack,
                    color = primary_dark,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(.3f)
                )

                Text(
                    "${purchaseDTO.date}",
                    fontFamily = LatoBlack,
                    color = text_primary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(start = 8.dp)
                )

                Text(
                    "${purchaseDTO.frequencyRepeat.title}",
                    fontFamily = LatoBlack,
                    color = text_primary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(.65f)
                )

            }

            Card(modifier = Modifier
                .clip(CircleShape),
                backgroundColor = secondary,
                onClick = {
//                scope.launch {
//                    delay(50L)
//                    navController!!.navigate("${Screen.RegisterPurchase.name}?idCardCurrent=${currentCreditCard?.myShoppingId}?isEditable=${false}?purchaseEdit=${""}")
//                }
                    callBack.onClick()
                }) {
                IconButton(onClick = {
                    callBack.onClick()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = text_primary,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }
    }
}