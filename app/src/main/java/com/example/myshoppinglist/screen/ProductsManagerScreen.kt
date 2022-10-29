package com.example.myshoppinglist.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.ui.theme.card_red_dark

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
    val keyboardController = LocalSoftwareKeyboardController.current
    val product: String = productManagerFieldViewModel.product.observeAsState(initial = "").value

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
                        value = product,
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
                                keyboardController?.hide()
                                focusManager.clearFocus(true)
                                Toast.makeText(context, "Clicado para pesquisar: $product", Toast.LENGTH_LONG).show()
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