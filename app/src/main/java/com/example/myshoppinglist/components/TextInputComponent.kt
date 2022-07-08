package com.example.myshoppinglist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.ui.theme.card_green
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary

@ExperimentalComposeUiApi
@Composable
fun TextInputComponent(modifier: Modifier? = null, maxChar: Int? = 250, keyboardType: KeyboardType? = KeyboardType.Text, label: String, icon: Int? = null, isMandatory: Boolean? = false, customOnClick: CustomOnClick){
    var value by remember { mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current
    var isErrorNickName by remember { mutableStateOf(false) }
    var customModifier = modifier?:Modifier.fillMaxWidth()

    Column{
        if(icon != null) {
            TextField(
                leadingIcon = {
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = text_primary,
                        )
                    }
                },
                value = value,
                onValueChange = {
                    if(it.length < maxChar!!){
                        value = it
                        customOnClick.onChangeValeu(it)
                        isErrorNickName = it.isBlank()
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
                value = value,
                onValueChange = {
                    if(it.length < maxChar!!){
                        value = it
                        customOnClick.onChangeValeu(it)
                        isErrorNickName = it.isBlank()
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
}