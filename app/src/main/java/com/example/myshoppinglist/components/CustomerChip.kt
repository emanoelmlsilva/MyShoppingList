package com.example.myshoppinglist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.enums.CardCreditFlag
import com.example.myshoppinglist.ui.theme.*

@Composable
fun CustomerChip(label: String, iconId: Int? = null, color: Color = primary_dark, callback: Callback? = null){
    Surface(
        modifier = Modifier.padding(start = 4.dp),
        color = color,
        shape = CircleShape
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
            if(iconId != null) {
                Column(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(card_blue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.5.dp),
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
                                painter = painterResource(id = CardCreditFlag.MASTER.flag),
                                contentDescription = null,
                                modifier = Modifier
                                    .shadow(2.dp, CircleShape)
                                    .size(22.dp)
                            )
                        }
                    }

                }
            }
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )
            Card(modifier = Modifier.then(
                Modifier
                    .size(22.dp)
                    .clip(
                        CircleShape
                    )), backgroundColor = secondary_dark){
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