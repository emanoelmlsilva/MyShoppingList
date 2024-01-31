package com.example.myshoppinglist.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import com.example.myshoppinglist.components.BoxPurchaseItemsComponent
import com.example.myshoppinglist.components.BoxShowPriceProduct
import com.example.myshoppinglist.components.EmptyTextComponent
import com.example.myshoppinglist.components.SearchProductComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.fieldViewModel.ProductManagerFieldViewModel
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils
import com.example.myshoppinglist.utils.MaskUtils.convertValueDoubleToString

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(productManagerFieldViewModel: ProductManagerFieldViewModel) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val valueSum by productManagerFieldViewModel.getValueSum().collectAsState(0.0)
    val quantityPurchases by productManagerFieldViewModel.getQuantityPurchases().collectAsState(
        initial = ""
    )
    val purchaseInfoCollection by productManagerFieldViewModel.getPurchaseInfoCollection().collectAsState(
        initial = emptyList()
    )

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
                lifecycleOwner,
                productManagerFieldViewModel
            )

            BoxShowPriceProduct(
                String.format("%.2f", valueSum),
                quantityPurchases
            )

            if (purchaseInfoCollection.isNotEmpty()) {
                BoxPurchaseItemsComponent(
                    context,
                    purchaseInfoCollection
                )
            } else {
                EmptyTextComponent("Não foi realizada nenhuma compra nesse mês.")
            }

        }
    }
}