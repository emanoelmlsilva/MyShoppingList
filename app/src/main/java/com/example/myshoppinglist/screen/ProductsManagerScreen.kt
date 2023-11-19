package com.example.myshoppinglist.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.VisibleCallback
import com.example.myshoppinglist.components.BoxPurchaseItemsComponent
import com.example.myshoppinglist.components.BoxShowPriceProduct
import com.example.myshoppinglist.components.SearchProductComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.fieldViewModel.ProductManagerFieldViewModel
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.LatoBlack
import com.example.myshoppinglist.ui.theme.text_secondary
import com.example.myshoppinglist.ui.theme.text_title_secondary
import com.example.myshoppinglist.utils.MaskUtils.convertValueDoubleToString

val TAG = "ProductsManagerScreen"

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(productManagerFieldViewModel: ProductManagerFieldViewModel) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    var valueSum by remember { mutableStateOf(0.0) }
    var quantityPurchases by remember { mutableStateOf("00") }
    var visibleAnimation by remember { mutableStateOf(true) }
    var purchaseInfoCollection = remember { mutableListOf<PurchaseInfo>() }

    LaunchedEffect(Unit) {
        productManagerFieldViewModel.getCreditCardController().findAllDB().observe(lifecycleOwner) {

            productManagerFieldViewModel.onChangeCardCreditCollection(
                it.map { creditCard -> CreditCardDTODB().fromCreditCardDTODB(creditCard) }
            )
        }
        productManagerFieldViewModel.searchPurchases(ObjectFilter())
    }

    productManagerFieldViewModel.getValueSum().observe(lifecycleOwner) {
        valueSum = it
    }

    productManagerFieldViewModel.getQuantityPurchases().observe(lifecycleOwner) {
        quantityPurchases = it
    }

    productManagerFieldViewModel.getPurchaseInfoCollection().observe(lifecycleOwner){
        purchaseInfoCollection.clear()
        purchaseInfoCollection.addAll(it)
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
            SearchProductComponent(
                context,
                visibleAnimation,
                lifecycleOwner,
                productManagerFieldViewModel
            )

            BoxShowPriceProduct(
                convertValueDoubleToString(valueSum),
                quantityPurchases
            )

            if (purchaseInfoCollection.isNotEmpty()) {
                BoxPurchaseItemsComponent(
                    context,
                    purchaseInfoCollection,
                    object : VisibleCallback{
                        override fun onChangeVisible(visible: Boolean) {
                            visibleAnimation = visible
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Não foi realizada nenhuma compra.",
                        fontFamily = LatoBlack,
                        color = text_title_secondary,
                        fontSize = 12.sp
                    )
                }
            }

        }
    }
}