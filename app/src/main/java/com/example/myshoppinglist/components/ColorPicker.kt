package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myshoppinglist.callback.CallbackColor
import com.example.myshoppinglist.ui.theme.*
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

private val colorCollection: List<Color> = listOf(
    card_blue,
    card_purple,
    card_pink_dark,
    card_red_light,
    card_blue_dark,
    card_green_dark,
    card_green,
    card_pink,
    card_blue_light,
    card_ruby,
    card_red_dark,
    card_orange,
    secondary
)

@Composable
private fun ColorPickerGuidance(content: @Composable (() -> Unit), isVertically: Boolean) {
    if (isVertically) {
        Row(
        ) {
            content()
        }

    } else {
        Column(
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPicker(colorCurrent: Color? = card_red_light, callback: CallbackColor, isVertically: Boolean? = false) {
    var enableChoiceColor by remember { mutableStateOf(false) }
    var colorChoice by remember { mutableStateOf(secondary) }
    var chosenColor by remember { mutableStateOf(card_red_light) }

    LaunchedEffect(key1 = colorCurrent){
        chosenColor = colorCurrent!!
    }

    if (enableChoiceColor) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(background_opacity),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                            .padding()
                            .fillMaxWidth()
                            .fillMaxHeight(.7f)
                            .background(secondary)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Text("Escolha uma cor", fontFamily = LatoBold, fontSize = 18.sp)

                        Card(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = colorChoice
                        ) {}

                        Column(
                            modifier = Modifier
                                .width(250.dp)
                                .height(250.dp)
                        ) {
                            HarmonyColorPicker(
                                harmonyMode = ColorHarmonyMode.MONOCHROMATIC,
                                onColorChanged = { color: HsvColor ->
                                    colorChoice = color.toColor()
                                    callback.onChangeValue(colorChoice)
                                }
                            )
                        }

                        ButtonsFooterContent(
                            isClickable = true,
                            btnTextCancel = "CANCELAR",
                            btnTextAccept = "SALVAR",
                            onClickCancel = {
                                enableChoiceColor = false
                            },
                            onClickAccept = {
                                chosenColor = colorChoice
                                callback.onChangeValue(colorChoice)
                                enableChoiceColor = false
                            })
                    }
                }
            })
    }

    ColorPickerGuidance(content = {
        repeat(colorCollection.size) { index ->
            val color = colorCollection[index]
            CircleColor(color, index == colorCollection.size - 1, isVertically!!,
                chosenColor == color, object : CallbackColor {
                override fun onChangeValue(newValue: Boolean) {
                    enableChoiceColor = newValue
                }

                override fun onChangeValue(value: Color) {
                    colorChoice = value
                    chosenColor = colorChoice
                    callback.onChangeValue(value)
                }
            })
        }
    }, isVertically = isVertically!!)
}

@Composable
private fun CircleColor(color: Color, isEditable: Boolean, isVertically: Boolean, isChosen: Boolean, callback: CallbackColor) {
    val SPACE_PEDDING = 2.dp

    Card(
        modifier = Modifier
            .padding(start = if(isVertically) SPACE_PEDDING else 0.dp, top = if(!isVertically) SPACE_PEDDING else 0.dp),
        border = BorderStroke(1.dp, background_opacity),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
    ) {
        Card(
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = {
                    if (isEditable) {
                        callback.onChangeValue(true)
                    } else {
                        callback.onChangeValue(color)
                    }
                }),
            backgroundColor = color,
        ) {
            if (isEditable) {
                Icon(
                    modifier = Modifier.padding(3.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = text_primary
                )
            } else if(isChosen){
                Card(
                    modifier = Modifier.padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = circle_background_opacity
                ) {}
            }
        }

    }

}