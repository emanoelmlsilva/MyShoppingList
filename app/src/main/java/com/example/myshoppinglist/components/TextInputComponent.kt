package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.secondary_dark

@ExperimentalComposeUiApi
@Composable
fun TextInputComponent(modifier: Modifier? = null, value: String = "", maxChar: Int? = 250, label: String, isMandatory: Boolean? = false, customOnClick: CustomTextFieldOnClick){
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorNickName by remember { mutableStateOf(false) }
    var customModifier = modifier?:Modifier.fillMaxWidth()
    var textValeu by remember {mutableStateOf(value)}

    Column{

            OutlinedTextField(
                value = textValeu,
                onValueChange = {
                    if(it.length < maxChar!!){
                        textValeu = it
                        customOnClick.onChangeValeu(it)
                        isErrorNickName = it.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = customModifier,
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = isErrorNickName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark)
    }
}
