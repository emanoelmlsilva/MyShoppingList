package com.example.myshoppinglist.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.background_text_field
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.MaskUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun NumberInputComponent(modifier: Modifier? = null, isKilogram: Boolean = false, value: String? = "", maxChar: Int? = 250, error: Boolean? = false, focusRequester: FocusRequester? = FocusRequester(), keyboardType: KeyboardType? = KeyboardType.Text, hasIcon: Boolean? = false, label: String, isMandatory: Boolean? = false,  reset: Boolean = false, customOnClick: CustomTextFieldOnClick){
    var isKilogramCurrent by remember { mutableStateOf(isKilogram)}
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorNumber by remember { mutableStateOf(false) }
    val customModifier = modifier?:Modifier.fillMaxWidth()
    val icon = if(isKilogram) R.drawable.ic_baseline_1k_24 else R.drawable.ic_baseline_123_24
    val symbol = "R$"

    fun getTextFieldValue(textField: String? = value): TextFieldValue{
        val valueField = textField.toString()
        val valueFormat = if(hasIcon!!) {
            if(isKilogramCurrent) MaskUtils.maskKiloGram(valueField) else MaskUtils.maskQuantity(
                valueField
            )
        }else {
            "$symbol ${MaskUtils.maskValue(valueField)}"
        }

        return TextFieldValue(
            text = valueFormat,
            selection = TextRange(valueFormat.length)

        )
    }

    var numberFieldValueState by remember { mutableStateOf(getTextFieldValue()) }

    LaunchedEffect(key1 = reset){
        if(reset){
            numberFieldValueState = getTextFieldValue("")
        }
    }

    LaunchedEffect(value){
        this.launch(Dispatchers.Main) {

            val textFieldIsZero = MaskUtils.replaceAll(numberFieldValueState.text).toInt() > 0
            val valueFieldIsZero = value!!.isNotBlank() && MaskUtils.replaceAll(value!!).toInt() > 0
            if(valueFieldIsZero || textFieldIsZero){
                numberFieldValueState = getTextFieldValue()
            }
        }

    }

    Column{
        if(hasIcon!!) {
                TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = background_text_field
                ),
                leadingIcon = {
                    IconButton(
                        onClick = {
                            isKilogramCurrent = !isKilogramCurrent
                            val textValue = if(isKilogramCurrent) MaskUtils.maskKiloGram(numberFieldValueState.text) else MaskUtils.maskQuantity(numberFieldValueState.text)
                            numberFieldValueState = TextFieldValue(
                                text = textValue,
                                selection = TextRange(textValue.length)
                            )
                            customOnClick.onChangeValue(textValue)
                            customOnClick.onChangeTypeProduct(if(isKilogramCurrent) TypeProduct.KILO else TypeProduct.QUANTITY)
                            customOnClick.onClick()
                            isErrorNumber = textValue.isBlank()
                        },
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                },
                value = numberFieldValueState,
                onValueChange = {
                    if(it.text.length < maxChar!!){
                        numberFieldValueState = getTextFieldValue(it.text)
                        customOnClick.onChangeValue(numberFieldValueState.text)
                        isErrorNumber = numberFieldValueState.text.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = customModifier.focusRequester(focusRequester!!),
                maxLines = 1,
                isError = error?:isErrorNumber,
                label = { Text(label) },
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }else{
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = background_text_field
                ),
                value = numberFieldValueState,
                onValueChange = {
                    if(it.text.length < maxChar!!){
                        numberFieldValueState = getTextFieldValue(it.text)
                        customOnClick.onChangeValue(numberFieldValueState.text)
                        isErrorNumber = numberFieldValueState.text.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = customModifier,
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = error ?: isErrorNumber,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp))
    }
}