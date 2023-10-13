package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.myshoppinglist.components.BoxPurchaseItemsComponent
import com.example.myshoppinglist.components.BoxShowPriceProduct
import com.example.myshoppinglist.components.SearchProductComponent
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.database.entities.relations.PurchaseAndCategory
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.model.ObjectFilter
import com.example.myshoppinglist.model.PurchaseInfo
import com.example.myshoppinglist.services.controller.CategoryController
import com.example.myshoppinglist.services.controller.CreditCardController
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.theme.text_secondary
import com.example.myshoppinglist.utils.MaskUtils.convertValueDoubleToString

val TAG = "ProductsManagerScreen"

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(navController: NavController?) {
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val productManagerFieldViewModel: ProductManagerFieldViewModel =
        ProductManagerFieldViewModel(context, lifecycleOwner)
    var valueSum by remember { mutableStateOf(0.0) }
    var quantityPurchases by remember { mutableStateOf("00") }
    var visibleAnimation by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        productManagerFieldViewModel.creditCardController.findAllDB().observe(lifecycleOwner) {

            productManagerFieldViewModel.onChangeCardCreditCollection(
                it.map { creditCard -> CreditCardDTODB().fromCreditCardDTODB(creditCard) }
            )
        }
        productManagerFieldViewModel.getSearchPurchase(ObjectFilter())
    }

    productManagerFieldViewModel.visibleAnimation.observe(lifecycleOwner) {
        visibleAnimation = it
    }

    productManagerFieldViewModel.valueSum.observe(lifecycleOwner) {
        valueSum = it
    }

    productManagerFieldViewModel.quantityPurchases.observe(lifecycleOwner) {
        quantityPurchases = it
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
            SearchProductComponent(context, visibleAnimation, lifecycleOwner, productManagerFieldViewModel)

            BoxShowPriceProduct(
                convertValueDoubleToString(valueSum),
                quantityPurchases
            )

            BoxPurchaseItemsComponent(
                context,
                lifecycleOwner,
                productManagerFieldViewModel.purchaseInfoCollection.observeAsState(initial = listOf()).value,
                productManagerFieldViewModel
            )

        }
    }
}

fun mountItemPurchase(purchaseCollection: List<PurchaseAndCategory>): List<PurchaseInfo> {
    val purchaseInfoCollection = mutableListOf<PurchaseInfo>()

    if (purchaseCollection.isEmpty()) return purchaseInfoCollection

    purchaseCollection.forEach { purchaseAndCategory ->

        val purchase = purchaseAndCategory.purchase
        val category = purchaseAndCategory.category

        val purchaseFilterCollection: List<PurchaseAndCategory> =
            purchaseCollection.filter { item -> purchase.name == item.purchase.name }

        val hasNotItemPurchase =
            purchaseInfoCollection.firstOrNull { item -> item.title == purchase.name } == null

        if (hasNotItemPurchase) {

            if (purchaseFilterCollection.isNotEmpty()) {
                val purchaseMultCollection: MutableList<PurchaseAndCategory> =
                    purchaseFilterCollection.map { it } as MutableList<PurchaseAndCategory>

                val valueSum =
                    purchaseMultCollection.sumOf { if (it.purchase.discount > 0) it.purchase.price - it.purchase.discount else it.purchase.price * if (it.purchase.typeProduct == TypeProduct.QUANTITY) it.purchase.quantiOrKilo.toInt() else 1 }

                val purchaseInfo = PurchaseInfo(
                    purchase.name, category.idImage, valueSum, Color(category.color),
                    purchaseMultCollection
                )

                purchaseInfoCollection.add(purchaseInfo)
            }
        }

    }

    return purchaseInfoCollection
}

class ProductManagerFieldViewModel(context: Context, lifecycleOwner: LifecycleOwner) :
    BaseFieldViewModel() {
    val product: MutableLiveData<String> = MutableLiveData("")
    var cardCreditCollection: MutableLiveData<List<CreditCardDTODB>> =
        MutableLiveData(mutableListOf())
    var visibleAnimation: MutableLiveData<Boolean> = MutableLiveData(true)
    var purchaseInfoCollection: MutableLiveData<List<PurchaseInfo>> =
        MutableLiveData(mutableListOf())
    val creditCardController = CreditCardController.getData(context, lifecycleOwner)
    val categoryController = CategoryController.getData(context, lifecycleOwner)
    private val purchaseController = PurchaseController.getData(context, lifecycleOwner)
    val valueSum: MutableLiveData<Double> = MutableLiveData(0.0)
    val quantityPurchases: MutableLiveData<String> = MutableLiveData("00")

    fun onChangePurchaseInfoCollection(newPurchaseInfoCollection: List<PurchaseInfo>) {
        purchaseInfoCollection.value = newPurchaseInfoCollection
    }

    fun onChangeVisibleAnimation(newVisibleAnimation: Boolean) {
        this.visibleAnimation.value = newVisibleAnimation
    }

    fun onChangeProduct(newProduct: String) {
        product.value = newProduct
    }

    fun onChangeCardCreditCollection(newCardCreditCollection: List<CreditCardDTODB>) {
        cardCreditCollection.value = newCardCreditCollection
    }

    override fun checkFields(): Boolean {
        TODO("Not yet implemented")
    }

    private fun mountObjectSearchDatabase(objectFilter: ObjectFilter): Result {

        var nameFields: String = ""
        var collectionSearch: MutableList<Any> = mutableListOf()

        if (objectFilter.textCollection.isNotEmpty()) {

            if (collectionSearch.isNotEmpty()) {
                nameFields += " AND "
            }

            objectFilter.textCollection.forEachIndexed { index, product ->
                nameFields += "name LIKE '%' || ? || '%'"
                collectionSearch.add(product.trim())

                if (objectFilter.textCollection.size > 1 && index < objectFilter.textCollection.size - 1) {
                    nameFields += " OR "
                }
            }
        }

        if (objectFilter.priceMin != null && objectFilter.priceMin!! >= 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price >= ?"
            collectionSearch.add(objectFilter.priceMin!!)
        }

        if (objectFilter.priceMax != null && objectFilter.priceMax!! >= 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "price <= ?"
            collectionSearch.add(objectFilter.priceMax!!)
        }

        if (objectFilter.idCard > 0) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "purchaseCardId =?"
            collectionSearch.add(objectFilter.idCard)
        }

        if (objectFilter.month.isNotBlank()) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }
            nameFields += "date LIKE '%' || ? || '%'"
            collectionSearch.add(objectFilter.month)
        }

        if (objectFilter.categoryCollection.size > 0) {

            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }

            objectFilter.categoryCollection.forEachIndexed { index, category ->
                nameFields += "categoryOwnerId = ?"
                collectionSearch.add(category.myShoppingId)

                if (objectFilter.categoryCollection.size > 1 && index < objectFilter.categoryCollection.size - 1) {
                    nameFields += " OR "
                }
            }
        }

        if (objectFilter.cardFilter.nickName.isNotBlank()) {
            if (collectionSearch.size > 0) {
                nameFields += " AND "
            }

            nameFields += "purchaseCardId = ?"
            collectionSearch.add(objectFilter.cardFilter.id)
        }

        return Result(nameFields, collectionSearch)
    }

    fun getSearchPurchase(objectFilter: ObjectFilter): List<PurchaseAndCategory> {

        val result = mountObjectSearchDatabase(objectFilter)

        val searchPurchases = purchaseController.getPurchasesOfSearchDB(
            result.collectionSearch,
            result.nameFields,
            if (objectFilter.textCollection.size > 0) "GROUP BY purchases.myShoppingId" else ""
        )

        onChangePurchaseInfoCollection(mountItemPurchase(searchPurchases))

        quantityPurchases.value =
            if (searchPurchases.size > 100) searchPurchases.size.toString() else if (searchPurchases.size < 10) "00${searchPurchases.size}" else "0${searchPurchases.size}"


        getSumOfSearchPurchase(result)

        return searchPurchases
    }

    private fun getSumOfSearchPurchase(result: Result) {
        valueSum.value =
            purchaseController.getPurchasesSumOfSearchDB(result.collectionSearch, result.nameFields)
    }

    data class Result(val nameFields: String, val collectionSearch: MutableList<Any>)

}