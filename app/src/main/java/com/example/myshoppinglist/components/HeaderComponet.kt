package com.example.myshoppinglist.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.primary_dark
import com.example.myshoppinglist.ui.theme.primary_light
import com.example.myshoppinglist.ui.theme.text_primary
import com.example.myshoppinglist.ui.theme.text_secondary

@ExperimentalAnimationApi
@Composable
fun HeaderComponent(userViewModel: UserViewModel, visibleAnimation: Boolean, callBack: Callback) {

    val createHeaderFieldViewModel = CreateHeaderFieldViewModel()
    val isVisibleValue = createHeaderFieldViewModel.isVisibleValue.observeAsState(initial = true)
    var name by remember{mutableStateOf("")}
    var idAvatar by remember{mutableStateOf(  R.drawable.default_avatar)}
    val animatedProgress = remember { Animatable(1f) }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    userViewModel.searchResult.observe(lifecycleOwner){
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
                    .background(primary_light)
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 3.dp, end = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.9f), verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Image(
                            painterResource(idAvatar),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .border(1.dp, text_primary, CircleShape)

                        )
                        Text(
                            text = "Ola, $name",
                            Modifier.padding(0.dp, 4.dp),
                            color = text_primary
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_miscellaneous_services_24),
                            contentDescription = "help",
                            tint = text_primary,
                        )
                    }
                }
            }
        }
    )
}

class CreateHeaderFieldViewModel : BaseFieldViewModel() {
    var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)
    var nameUser: MutableLiveData<String> = MutableLiveData("")
    var idAvatar: MutableLiveData<Int> = MutableLiveData(R.drawable.default_avatar)

    fun onChangeIsVisibleValue() {
        isVisibleValue.value = !isVisibleValue.value!!
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }
}