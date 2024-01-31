package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.fieldViewModel.LocationAndDateViewModel
import com.example.myshoppinglist.model.LocationAndDate
import com.example.myshoppinglist.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogLocationAndDate(
    context: Context, visibility: Boolean, recoverLocation: String? = null,
    recoverDate: String? = null, callback: CallbackObject<LocationAndDate>
) {

    val locationAndDateViewModel: LocationAndDateViewModel = viewModel()
    var visibilityDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = recoverLocation){
        if(recoverLocation != null){
            locationAndDateViewModel.onChangeLocale(recoverLocation)
        }

        if(recoverDate != null){
            locationAndDateViewModel.onChangeDateCurrent(recoverDate)
        }
    }

    LaunchedEffect(key1 = visibility) {
        visibilityDialog = visibility
    }

    fun checkFields(): Boolean {
        if (locationAndDateViewModel.locale.value!!.isNotBlank()) {
            return true
        }

        locationAndDateViewModel.localeError.value = true

        return false
    }

    DialogCustom(visibilityDialog = visibilityDialog, percentHeight = 2.2f) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    Modifier
                        .height(25.dp)
                )

                Text(
                    "Salvar produtos",
                    fontFamily = LatoRegular,
                    color = primary_dark,
                    fontSize = 18.sp
                )

                Spacer(
                    Modifier
                        .height(25.dp)
                )

                Divider(
                    color = text_primary,
                    modifier = Modifier
                        .height(1.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(text_title_secondary)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                    tint = text_secondary,
                    modifier = Modifier.padding(start = 10.dp),
                )

                Text(
                    "Os produtos salvos serão adicionados\nautomaticamente nas compras.",
                    fontFamily = LatoRegular,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )

            }

            Column(Modifier.padding(horizontal = 6.dp)) {
                Spacer(
                    Modifier
                        .height(30.dp)
                )

                Text(
                    "Pagamento",
                    fontFamily = LatoRegular,
                    fontSize = 14.sp,
                )

                Spacer(
                    Modifier
                        .height(10.dp)
                )

                Divider(
                    color = divider,
                    modifier = Modifier
                        .height(1.dp)
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextInputComponent(
                        textColor = text_primary,
                        label = "Local",
                        value = locationAndDateViewModel.locale.observeAsState().value!!,
                        reset = false,
                        modifier = Modifier.fillMaxWidth(.58f),
                        maxChar = 30,
                        isCountChar = true,
                        error = locationAndDateViewModel.localeError.observeAsState().value,
                        customOnClick = object : CustomTextFieldOnClick {
                            override fun onChangeValue(newValue: String) {
                                locationAndDateViewModel.onChangeLocale(
                                    newValue
                                )
                            }

                        })
                    DatePickerCustom(
                        locationAndDateViewModel.dateCurrent.value!!,
                        backgroundColor = background_text_field,
                        isEnableClick = false,
                        context = context,
                        callback = object : Callback {
                            override fun onChangeValue(newValue: String) {
                                locationAndDateViewModel.onChangeDateCurrent(newValue)
                            }
                        }
                    )
                }

                Spacer(
                    Modifier
                        .height(35.dp)
                )

                ButtonsFooterContent(
                    isClickable = true,
                    btnTextCancel = "Cancelar",
                    btnTextAccept = "SALVAR",
                    onClickCancel = {
                        callback.onCancel()
                    },
                    onClickAccept = {
                        if (checkFields()) {
                            callback.onSuccess(
                                LocationAndDate(
                                    locationAndDateViewModel.locale.value!!,
                                    locationAndDateViewModel.dateCurrent.value!!
                                )
                            )
                        }
                    })
            }
        }
    }
}