package com.example.myshoppinglist.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.CustomerChip
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.ui.theme.background_card_gray_light
import com.example.myshoppinglist.ui.theme.background_card_light
import com.example.myshoppinglist.ui.theme.text_primary

@ExperimentalComposeUiApi
@Composable
fun ProductsManagerScreen(navController: NavController?) {
    val context = LocalContext.current
    val productManagerFieldViewModel: ProductManagerFieldViewModel = viewModel()

    TopAppBarScreen(onClickIcon = { navController?.popBackStack() }, content = {

        Column(modifier = Modifier.padding(16.dp)){
            SearchProduct(context, productManagerFieldViewModel)
        }
    })
}

@ExperimentalComposeUiApi
@Composable
fun SearchProduct(context: Context, productManagerFieldViewModel :ProductManagerFieldViewModel) {
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val keyboardController = LocalSoftwareKeyboardController.current
    var product = remember { mutableStateOf("") }
    val listProductText = remember { mutableStateListOf<String>() }
    val reset = remember { mutableStateOf(false) }

    productManagerFieldViewModel.product.observe(lifecycleOwner.value){
        if(it.isNotBlank() && reset.value){
            reset.value = false
        }
        product.value = it
    }

    Card(backgroundColor = background_card_gray_light, modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(.13f),
        shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            Card(
                backgroundColor = background_card_light,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.53f),
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    TextInputComponent(modifier = Modifier
                        .fillMaxWidth(.85f)
                        .fillMaxHeight(),
                        value = product.value,
                        reset = reset.value,
                        label = "Produto",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = text_primary,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                listProductText.add(product.value)
                                reset.value = true
                                productManagerFieldViewModel.onChangeProduct("")
                            }
                        ),
                        error = false,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                productManagerFieldViewModel.onChangeProduct(newValue)
                            }
                        })

                    IconButton(
                        onClick = {
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_filter_alt_24),
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                }
            }

            LazyRow(modifier = Modifier.padding(top = 4.dp)){
                items(listProductText){ productItem ->
                    CustomerChip(label  = productItem, callback = object: Callback {
                        override fun onClick() {
                            listProductText.remove(productItem)
                        }
                    })
                }
            }
        }
    }
}

class ProductManagerFieldViewModel : BaseFieldViewModel() {
    val product: MutableLiveData<String> = MutableLiveData("")

    fun onChangeProduct(newProduct: String){
        product.value = newProduct
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}