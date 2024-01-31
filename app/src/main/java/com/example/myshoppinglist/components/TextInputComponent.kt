package com.example.myshoppinglist.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
fun TextInputComponent(
    focusRequester: FocusRequester = FocusRequester(),
    textColor: Color? = text_primary,
    disabledTextColor: Color? = text_primary,
    backgroundColor: Color = background_text_field,
    modifier: Modifier? = Modifier,
    value: String = "",
    maxChar: Int? = 250,
    label: String,
    isMandatory: Boolean? = false,
    isEnableClick: Boolean? = true,
    isCountChar: Boolean? = false,
    reset: Boolean = false,
    error: Boolean? = false,
    customOnClick: CustomTextFieldOnClick,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions = KeyboardActions(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    var isErrorNickName by remember { mutableStateOf(false) }
    val customModifier = modifier ?: Modifier.fillMaxWidth()
    var textValue by remember { mutableStateOf("") }

    LaunchedEffect(key1 = value) {
        textValue = value
    }

    Column(modifier = customModifier) {
        TextField(
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor!!,
                backgroundColor = backgroundColor,
                disabledTextColor = disabledTextColor!!
            ),
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            value = textValue,
            onValueChange = {
                if (it.length <= maxChar!!) {
                    textValue = it
                    customOnClick.onChangeValue(it)
                    isErrorNickName = it.isBlank()
                }
            },
            enabled = isEnableClick!!,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    customOnClick.onClick()
                })
                .focusRequester(focusRequester),
            label = { Text(label) },
            singleLine = true,
            maxLines = 1,
            isError = error ?: isErrorNickName,
        )
        if (isCountChar!!) {
            Text(
                text = "${textValue.length} / $maxChar",
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
            )
        }
        if (isMandatory!!) Text(
            text = "Obrigatório",
            color = secondary_dark,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}
