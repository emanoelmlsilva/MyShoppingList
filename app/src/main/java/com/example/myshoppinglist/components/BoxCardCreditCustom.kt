package com.example.myshoppinglist.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
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
import com.example.myshoppinglist.database.dtos.CreditCardDTODB
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.ui.theme.*
import com.example.myshoppinglist.utils.MaskUtils

@Composable
fun BoxCardCreditCustom(
    creditCardDTO: CreditCardDTODB,
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
                        text = "R$ ${MaskUtils.maskValue(String.format("%.2f", creditCardDTO.value))}",
                        fontFamily = LatoBold,
                        fontSize = 45.sp,
                        color = background_card
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.65f)
                ) {
                    CustomButtonRounded(
                        icon = ImageVector.vectorResource(id = R.drawable.shopping_bag),
                        backgroundColor = text_primary.copy(alpha = 0.2f),
                        text = "Comprar",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.RegisterPurchase.name}?idCardCurrent=${creditCardDTO.myShoppingId}?isEditable=${false}?purchaseEdit=${""}")
                            }
                        })
                    CustomButtonRounded(
                        icon = ImageVector.vectorResource(id = R.drawable.fact_check),
                        backgroundColor = background_card,
                        text = "Lista",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.ListPurchase.name}?idCard=${creditCardDTO.myShoppingId}")
                            }
                        })
                    CustomButtonRounded(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_more_horiz_24),
                        backgroundColor = background_card,
                        text = "Mais",
                        callback = object : CallbackCreditCard {
                            override fun onClick() {
                                navController.navigate("${Screen.Spending.name}?idCard=${creditCardDTO.myShoppingId}")
                            }
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonDotted(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                navController.navigate(
                    "${Screen.CreateCards.name}?hasToolbar=${true}?holderName=${""}?isUpdate=${false}?creditCardDTO=${""}"
                )
            },
            elevation = 0.dp,
            shape = RoundedCornerShape(28.dp),
            backgroundColor = text_primary.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, secondary),
        ) {
            Text(
                text = "Criar novo cartão",
                fontFamily = LatoBold,
                modifier = Modifier.padding(all = 18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomButtonRounded(
    icon: ImageVector,
    backgroundColor: Color,
    text: String,
    callback: CallbackCreditCard
) {
    val painter = rememberVectorPainter(icon)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.defaultMinSize(90.dp, 50.dp),
            onClick = {
                callback.onClick()
            },
            elevation = 0.dp,
            shape = RoundedCornerShape(28.dp),
            backgroundColor = backgroundColor,
            border = BorderStroke(1.dp, secondary),
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(all = 14.dp),
                tint = text_primary
            )
        }
        Text(text = text, fontFamily = LatoBold, modifier = Modifier.padding(top = 6.dp))
    }
}