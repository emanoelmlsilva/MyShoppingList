package com.example.myshoppinglist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ChoicePriceComponent(
    priceMin: Float? = 0F,
    priceMax: Float? = 100F,
    maxStepDefault: Float = 100.0f,
    callbackMin: CustomTextFieldOnClick,
    callbackMax: CustomTextFieldOnClick
) {
    val focusRequester = remember { FocusRequester() }
    var maxStepCurrent by remember { mutableStateOf(priceMax ?: maxStepDefault) }
    var sliderPosition by remember { mutableStateOf(priceMin!!..if (priceMax!! > 0f) priceMax else maxStepDefault) }
    var valueMin by remember { mutableStateOf(priceMin) }
    var valueMax by remember { mutableStateOf(if (priceMax!! > 0f) priceMax else maxStepDefault) }
    var enableEditMaxStep by remember { mutableStateOf(false) }
    var minStep by remember {
        mutableStateOf(
            MaskUtils.maskValue(
                String.format(
                    "%.2f",
                    priceMin
                )
            )
        )
    }
    var maxStep by remember {
        mutableStateOf(
            MaskUtils.maskValue(
                String.format(
                    "%.2f",
                    if (priceMax!! > 0f) priceMax else maxStepDefault
                )
            )
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = priceMin, key2 = priceMax) {
        sliderPosition = priceMin!!..(if (priceMax!! > 0f) priceMax else maxStepDefault)
        valueMin = priceMin
        valueMax = if (priceMax > 0f) priceMax else maxStepDefault
        minStep =  MaskUtils.maskValue(
            String.format(
                "%.2f",
                priceMin
            )
        )
        maxStep =  MaskUtils.maskValue(
            String.format(
                "%.2f",
                priceMax
            )
        )

    }

    LaunchedEffect(key1 = enableEditMaxStep){
        if(enableEditMaxStep){
            focusRequester.requestFocus()
        }
    }

    Column {
        Spacer(Modifier.size(24.dp))

        Text(text = "Preço:", fontFamily = LatoBlack)

        Column(
            modifier = Modifier
                .fillMaxHeight(.2f)
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            RangeSlider(
                enabled = !enableEditMaxStep,
                values = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    valueMin = sliderPosition.start
                    valueMax = sliderPosition.endInclusive

                    callbackMin.onChangeValueFloat(valueMin!!)
                    callbackMax.onChangeValueFloat(valueMax)

                },
                valueRange = 0f..maxStepCurrent,
                onValueChangeFinished = {

                },
                colors = SliderDefaults.colors(
                    thumbColor = card_orange,
                    activeTrackColor = card_green
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (enableEditMaxStep) {
                    Column(modifier = Modifier.fillMaxWidth(.2f)) {
                        BasicTextField(
                            cursorBrush = SolidColor(primary),
                            value = TextFieldValue(
                                text = minStep,
                                selection = TextRange(minStep.length)
                            ),
                            onValueChange = {
                                val text = it.text
                                if (text.length < 10) {
                                    val value = if (text.isNotBlank()) {
                                        text
                                    } else {
                                        "0"
                                    }
                                    minStep = MaskUtils.maskValue(value)
                                }
                            },
                            textStyle = TextStyle(color = text_primary, textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            decorationBox = { innerTextField ->
                                innerTextField()
                            },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val maskValueToNumber =
                                        maxStep.replace(".", "").replace(",", ".").toFloat()

                                    val minMaskValueToNumber =
                                        minStep.replace(".", "").replace(",", ".").toFloat()

                                    sliderPosition = minMaskValueToNumber..maskValueToNumber
                                    valueMin = sliderPosition.start
                                    valueMax = sliderPosition.endInclusive

                                    callbackMin.onChangeValueFloat(valueMin!!)
                                    callbackMax.onChangeValueFloat(valueMax)

                                    enableEditMaxStep = false
                                    keyboardController?.hide()
                                }
                            ),
                        )

                        Divider(
                            color = primary_dark,
                            modifier = Modifier
                                .height(2.dp)
                        )
                    }

                    Column(modifier = Modifier.fillMaxWidth(.2f)) {
                        BasicTextField(
                            modifier = Modifier.focusRequester(focusRequester),
                            cursorBrush = SolidColor(primary),
                            value = TextFieldValue(
                                text = maxStep,
                                selection = TextRange(maxStep.length)
                            ),
                            onValueChange = {
                                var text = it.text
                                if (text.length < 10) {
                                    val value = if (text.isNotBlank()) {
                                        text
                                    } else {
                                        "0"
                                    }
                                    maxStep = MaskUtils.maskValue(value)
                                }
                            },
                            textStyle = TextStyle(color = text_primary, textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            decorationBox = { innerTextField ->
                                innerTextField()
                            },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val maskValueToNumber =
                                        maxStep.replace(".", "").replace(",", ".").toFloat()

                                    val minMaskValueToNumber =
                                        minStep.replace(".", "").replace(",", ".").toFloat()

                                    maxStepCurrent = maskValueToNumber
                                    sliderPosition = minMaskValueToNumber..maskValueToNumber
                                    valueMin = sliderPosition.start
                                    valueMax = sliderPosition.endInclusive

                                    callbackMin.onChangeValueFloat(valueMin!!)
                                    callbackMax.onChangeValueFloat(valueMax)

                                    enableEditMaxStep = false
                                    keyboardController?.hide()
                                }
                            ),
                        )

                        Divider(
                            color = primary_dark,
                            modifier = Modifier
                                .height(2.dp)
                        )
                    }
                }

                if (!enableEditMaxStep) {
                    Text(text = MaskUtils.maskValue(MaskUtils.convertValueDoubleToString(valueMin!!.toDouble())))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    valueMax.toDouble()
                                )
                            )
                        )

                        IconButton(
                            onClick = {
                                enableEditMaxStep = true
                                keyboardController?.show()

                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = null,
                                tint = primary_light,
                            )
                        }
                    }
                }
            }
        }
    }
}