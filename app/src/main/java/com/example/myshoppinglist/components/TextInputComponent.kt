package com.example.myshoppinglist.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.secondary_dark

@ExperimentalComposeUiApi
@Composable
fun TextInputComponent(modifier: Modifier? = null, value: String = "", maxChar: Int? = 250, label: String, isMandatory: Boolean? = false, reset: Boolean = false, error: Boolean? = false, customOnClick: CustomTextFieldOnClick){
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorNickName by remember { mutableStateOf(false) }
    var customModifier = modifier?:Modifier.fillMaxWidth()
    var textValue by remember {mutableStateOf(value)}

    if(reset){
        textValue = ""
    }

    Column{
            OutlinedTextField(
                value = textValue,
                onValueChange = {
                    if(it.length < maxChar!!){
                        textValue = it
                        customOnClick.onChangeValue(it)
                        isErrorNickName = it.isBlank()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = customModifier,
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = error ?: isErrorNickName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp))
    }
}
