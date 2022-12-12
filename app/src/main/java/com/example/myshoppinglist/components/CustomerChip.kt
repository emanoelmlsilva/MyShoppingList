package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun CustomerChip(label: String, iconId: Int? = null, color: Color = primary_dark, isBackgroundCircle: Boolean = false, isEnabled: Boolean = false, isChoice: Boolean = false, paddingVertical: Dp = 4.dp, paddingHorinzotal: Dp = 8.dp, callback: Callback? = null){
    Card(
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(start = 4.dp)
            .clip(CircleShape)
            .clickable(enabled = isEnabled, onClick = { callback?.onClick() }),
        backgroundColor = if(isEnabled) {
            if (isChoice) {
                color
            } else {
                secondary_dark
            }
        } else {
            color
        },
        elevation = 0.dp,
    ) {
        Row(modifier = Modifier.padding(horizontal = paddingHorinzotal, vertical = paddingVertical), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
            if(iconId != null && iconId > 0) {
                if (!isEnabled || isBackgroundCircle) {
                    Column(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(card_blue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(CardCreditFlag.MONEY.flag == iconId) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = iconId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .shadow(0.1.dp, CircleShape)
                                        .size(15.dp)
                                )
                            }
                        } else {
                            Card(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(2.5.dp),
                                backgroundColor = background_card,
                                border = BorderStroke(1.dp, border),
                                modifier = Modifier
                                    .fillMaxWidth(.7f)
                                    .fillMaxHeight(.5f)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = iconId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(11.dp)
                                    )
                                }
                            }
                        }

                    }
                } else if (isEnabled) {
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                    )
                }
            }
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )

            if(isEnabled) {
                if(isChoice) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            } else {
                Card(
                    modifier = Modifier.then(
                        Modifier
                            .size(22.dp)
                            .clip(
                                CircleShape
                            )
                    ), backgroundColor = secondary_dark
                ) {
                    IconButton(
                        onClick = {
                            callback?.onClick()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                }
            }
        }

    }
}