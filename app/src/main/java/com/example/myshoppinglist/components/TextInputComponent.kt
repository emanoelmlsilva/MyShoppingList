package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.R
import com.example.myshoppinglist.ui.theme.card_blue
import com.example.myshoppinglist.ui.theme.card_green
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalComposeUiApi
@Composable
fun TextInputComponent(modifier: Modifier? = null, value: String = "", maxChar: Int? = 250, keyboardType: KeyboardType? = KeyboardType.Text, hasIcon: Boolean? = false, label: String, isMandatory: Boolean? = false, customOnClick: CustomOnClick){
    var isKilogram by remember { mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorNickName by remember { mutableStateOf(false) }
    var customModifier = modifier?:Modifier.fillMaxWidth()
    var icon = if(isKilogram) R.drawable.ic_baseline_1k_24 else R.drawable.ic_baseline_123_24
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = if(keyboardType!! == KeyboardType.Number) MaskUtils.maskValue(value) else value
            )
        )
    }
    var textFieldValueStateKiloGram by remember {
        mutableStateOf(
            TextFieldValue(
                text = if(isKilogram) MaskUtils.maskKiloGram(value) else MaskUtils.maskQuantity(
                    value
                ),
                selection = TextRange(value.length)
            )
        )
    }
    textFieldValueStateKiloGram = TextFieldValue(
        text = if(isKilogram) MaskUtils.maskKiloGram(value) else MaskUtils.maskQuantity(
            value
        ),
        selection = TextRange(value.length)
    )
    Column{
        if(hasIcon!!) {
            TextField(
                leadingIcon = {
                    IconButton(
                        onClick = {
                            isKilogram = !isKilogram
                            val textValue = if(isKilogram) MaskUtils.maskKiloGram(textFieldValueStateKiloGram.text) else MaskUtils.maskQuantity(textFieldValueStateKiloGram.text)
                            textFieldValueStateKiloGram = TextFieldValue(
                                text = textValue,
                                selection = TextRange(textValue.length)
                            )
                            customOnClick.onChangeValeu(textValue)
                            customOnClick.onClick()
                            isErrorNickName = textValue.isBlank()
                        },
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                },
                value = textFieldValueStateKiloGram,
                onValueChange = {
                    if(it.text.length < maxChar!!){
                        val textValue = if(isKilogram) MaskUtils.maskKiloGram(it.text) else MaskUtils.maskQuantity(it.text)
                        textFieldValueStateKiloGram = TextFieldValue(
                            text = textValue,
                            selection = TextRange(textValue.length)
                        )
                        customOnClick.onChangeValeu(textValue)
                        isErrorNickName = textValue.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = customModifier,
                maxLines = 1,
                label = { Text(label) },
                singleLine = true,
                isError = isErrorNickName,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }else{
            OutlinedTextField(
                value = textFieldValueState,
                onValueChange = {
                    if(it.text.length < maxChar!!){
                        val textValue = if(keyboardType!! == KeyboardType.Number) MaskUtils.maskValue(it.text) else it.text
                        textFieldValueState = TextFieldValue(
                            text = textValue,
                            selection = TextRange(textValue.length)
                        )
                        customOnClick.onChangeValeu(textValue)
                        isErrorNickName = textValue.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType!!),
                modifier = customModifier,
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = isErrorNickName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark)
    }
}

interface CustomOnClick{
    fun onChangeValeu(newValue: String)
    fun onClick(){

    }
}