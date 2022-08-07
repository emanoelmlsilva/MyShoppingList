package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.MaskUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun NumberInputComponent(modifier: Modifier? = null, value: String? = "", maxChar: Int? = 250, error: Boolean? = false, focusRequester: FocusRequester? = FocusRequester(), keyboardType: KeyboardType? = KeyboardType.Text, hasIcon: Boolean? = false, label: String, isMandatory: Boolean? = false,  reset: Boolean = false, customOnClick: CustomTextFieldOnClick){
    var isKilogram by remember { mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorQuantOrKilo by remember { mutableStateOf(false) }
    val customModifier = modifier?:Modifier.fillMaxWidth()
    val icon = if(isKilogram) R.drawable.ic_baseline_1k_24 else R.drawable.ic_baseline_123_24
    val symbol = "R$"

    fun getTextFieldValue(textField: String? = value): TextFieldValue{
        val valueFormat = if(isKilogram) MaskUtils.maskKiloGram(textField!!) else MaskUtils.maskQuantity(textField!!)

        return TextFieldValue(
            text = if(hasIcon!!) {
                if(isKilogram) MaskUtils.maskKiloGram(textField) else MaskUtils.maskQuantity(
                    textField
                )
            }else {
                "$symbol ${MaskUtils.maskValue(textField)}"
            },
            selection = TextRange(valueFormat.length)

        )
    }

    var numberFieldValueState by remember { mutableStateOf(getTextFieldValue()) }

    if(hasIcon!!){
        numberFieldValueState = getTextFieldValue()
    }

    if(reset){
        numberFieldValueState = getTextFieldValue("")
    }

    LaunchedEffect(value){
        this.launch(Dispatchers.Main) {
            val textFieldIsZero = MaskUtils.replaceAll(numberFieldValueState.text).toInt() == 0
            val valueFieldIsZero = value!!.isNotBlank() && MaskUtils.replaceAll(value!!).toInt() > 0
            if(valueFieldIsZero && textFieldIsZero){
                numberFieldValueState = getTextFieldValue()
            }
        }

    }

    Column{
        if(hasIcon) {
            TextField(
                leadingIcon = {
                    IconButton(
                        onClick = {
                            isKilogram = !isKilogram
                            val textValue = if(isKilogram) MaskUtils.maskKiloGram(numberFieldValueState.text) else MaskUtils.maskQuantity(numberFieldValueState.text)
                            numberFieldValueState = TextFieldValue(
                                text = textValue,
                                selection = TextRange(textValue.length)
                            )
                            customOnClick.onChangeValue(textValue)
                            customOnClick.onChangeTypeProduct(if(isKilogram) TypeProduct.KILO else TypeProduct.QUANTITY)
                            customOnClick.onClick()
                            isErrorQuantOrKilo = textValue.isBlank()
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
                        val textValue = if(isKilogram) MaskUtils.maskKiloGram(it.text) else MaskUtils.maskQuantity(it.text)
                        numberFieldValueState = getTextFieldValue(it.text)
                        customOnClick.onChangeValue(numberFieldValueState.text)
                        isErrorQuantOrKilo = textValue.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = customModifier.focusRequester(focusRequester!!),
                maxLines = 1,
                isError = error?:isErrorQuantOrKilo,
                label = { Text(label) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }else{
            OutlinedTextField(
                value = numberFieldValueState,
                onValueChange = {
                    if(it.text.length < maxChar!!){
                        val textValue = MaskUtils.maskValue(it.text)
                        val textValueSimbol = "$symbol $textValue"

                            numberFieldValueState = TextFieldValue(
                                text = textValueSimbol,
                                selection = TextRange(textValueSimbol.length)
                            )

                        customOnClick.onChangeValue(textValue)
                        isErrorQuantOrKilo = textValue.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType!!),
                modifier = customModifier,
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = error ?: isErrorQuantOrKilo,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp))
    }
}