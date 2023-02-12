package com.example.myshoppinglist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.text_primary

@ExperimentalAnimationApi
@Composable
fun HeaderComponent(userViewModel: UserViewModel, visibleAnimation: Boolean, callBack: Callback) {
    var name by remember { mutableStateOf("") }
    var idAvatar by remember { mutableStateOf(R.drawable.resource_default) }
    val animatedProgress = remember { Animatable(1f) }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    userViewModel.searchResult.observe(lifecycleOwner) {
        idAvatar = it.idAvatar;
        name = it.name
    }

    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(
            0.5f,
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = 300
            )
        )
    }

    BaseAnimationComponent(
        visibleAnimation = visibleAnimation,
        contentBase = {
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 3.dp, end = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            imageVector = ImageVector.vectorResource(id = idAvatar),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .border(1.dp, text_primary, CircleShape)
                        )

                        Text(
                            text = "Ola, $name",
                            Modifier.padding(start = 12.dp),
                            color = text_primary
                        )
                    }
//                    IconButton(onClick = { }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_baseline_miscellaneous_services_24),
//                            contentDescription = "help",
//                            tint = text_primary,
//                        )
//                    }
                }
            }
        }
    )
}