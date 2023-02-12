package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.CallbackCreditCard
import com.example.myshoppinglist.database.dtos.CreditCardDTO
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxCardCreditCustom(
    creditCardDTO: CreditCardDTO,
    navController: NavController
) {

    Card(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color(creditCardDTO.colorCard),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (creditCardDTO.cardName.isBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(secondary_light),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    ButtonDotted(navController = navController)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.fillMaxHeight(.45f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            creditCardDTO.cardName,
                            fontFamily = LatoBlack,
                            color = text_title_secondary,
                            fontSize = 24.sp
                        )
                        Image(
                            painter = painterResource(id = creditCardDTO.flagBlack),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )

                    }
                    Text(
                        text = "R$ ${
                            MaskUtils.maskValue(
                                MaskUtils.convertValueDoubleToString(
                                    creditCardDTO.value.toDouble()
                                )
                            )
                        }",
                        fontFamily = LatoBold,
                        fontSize = 54.sp,
                        color = background_card
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.55f)
                ) {
                    CustomButtonRounded(isDotted = true,
                        top = .25f,
                        maxWidth = .3f,
                        icon = ImageVector.vectorResource(id = R.drawable.shopping_bag),
                        backgroundColor = text_primary.copy(alpha = 0.2f),
                        text = "Comprar",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.RegisterPurchase.name}?idCardCurrent=${creditCardDTO.idCard}")
                            }
                        })
                    CustomButtonRounded(
                        icon = ImageVector.vectorResource(id = R.drawable.fact_check),
                        backgroundColor = background_card,
                        text = "Lista",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.ListPurchase.name}?idCard=${creditCardDTO.idCard}")
                            }
                        })
                    CustomButtonRounded(
                        maxWidth = .64f,
                        top = .05f,
                        left = 3.8f,
                        icon = ImageVector.vectorResource(id = R.drawable.more_horiz),
                        backgroundColor = background_card,
                        text = "Mais",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.Spending.name}?idCard=${creditCardDTO.idCard}")
                            }
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonDotted(maxHeight: Float = .22f, navController: NavController) {
    val stroke = Stroke(
        width = 6f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 18f), 0f)
    )
    val painter = rememberVectorPainter(Icons.Rounded.Add)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                navController.navigate("${Screen.CreateCards.name}?hasToolbar=${true}?nameUser=${""}")
            },
            elevation = 0.dp,
            shape = RoundedCornerShape(28.dp),
            backgroundColor = text_primary.copy(alpha = 0.2f)
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth(.28f)
                    .fillMaxHeight(maxHeight)
                    .padding(1.dp)
            ) {
                drawRoundRect(
                    color = background_card,
                    cornerRadius = CornerRadius(x = 36.dp.toPx(), 36.dp.toPx()),
                    style = stroke
                )
                translate(
                    left = ((size.width / 2.7).toFloat()),
                    top = (size.height * 0.29).toFloat()
                ) {
                    with(painter) {
                        draw(painter.intrinsicSize)
                    }
                }
            }
        }
        Text(text = "Adicionar", fontFamily = LatoBold, modifier = Modifier.padding(top = 6.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomButtonRounded(
    isDotted: Boolean = false,
    maxWidth: Float = .4f,
    top: Float = 0.29F,
    left: Float = 2.7F,
    icon: ImageVector,
    backgroundColor: Color,
    text: String,
    callback: CallbackCreditCard
) {
    val stroke = Stroke(
        width = 6f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 18f), 0f)
    )
    val painter = rememberVectorPainter(icon)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                callback.onClick()
            },
            elevation = 0.dp,
            shape = RoundedCornerShape(28.dp),
            backgroundColor = backgroundColor
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth(maxWidth)
                    .fillMaxHeight(.62f)
                    .padding(1.dp)
            ) {
                if (isDotted) {
                    drawRoundRect(
                        color = background_card,
                        cornerRadius = CornerRadius(x = 36.dp.toPx(), 36.dp.toPx()),
                        style = stroke
                    )
                }
                translate(
                    left = ((size.width / left)),
                    top = (size.height * top)
                ) {
                    with(painter) {
                        draw(
                            size = painter.intrinsicSize, colorFilter = ColorFilter.tint(
                                text_primary
                            )
                        )
                    }
                }
            }
        }
        Text(text = text, fontFamily = LatoBold, modifier = Modifier.padding(top = 6.dp))
    }
}