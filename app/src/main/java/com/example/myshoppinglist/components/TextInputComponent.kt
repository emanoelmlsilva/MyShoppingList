package com.example.myshoppinglist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.background_text_field
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.ui.theme.text_secondary_light

@ExperimentalComposeUiApi
@Composable
    fun TextInputComponent(backgroundColor: Color = background_text_field, modifier: Modifier? = null, value: String = "", maxChar: Int? = 250, label: String, isMandatory: Boolean? = false, isEnableClick: Boolean? = false, isCountChar: Boolean? = false, reset: Boolean = false, error: Boolean? = false, customOnClick: CustomTextFieldOnClick, keyboardOptions: KeyboardOptions = KeyboardOptions.Default, keyboardActions: KeyboardActions = KeyboardActions()){
    var isErrorNickName by remember { mutableStateOf(false) }
    var customModifier = modifier ?: Modifier.fillMaxWidth()
    var textValue by remember {mutableStateOf(value)}

    if(reset){
        textValue = ""
    }

    Column(modifier = customModifier){
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = backgroundColor,
                ),
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                value = if(value.isBlank()) textValue else value,
                onValueChange = {
                    if(it.length <= maxChar!!){
                        textValue = it
                        customOnClick.onChangeValue(it)
                        isErrorNickName = it.isBlank()
                    }
                },
                enabled = !isEnableClick!!,
                modifier = Modifier.fillMaxWidth().clickable(enabled = isEnableClick, onClick = {
                    customOnClick.onClick()
                }),
                label = { Text(label) },
                singleLine = true,
                maxLines = 1,
                isError = error ?: isErrorNickName,
            )
        if(isCountChar!!) {
            Text(
                text = "${textValue.length} / $maxChar",
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
        }
        if(isMandatory!!) Text(text = "Obrigatório", color = secondary_dark, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp))
    }
}
