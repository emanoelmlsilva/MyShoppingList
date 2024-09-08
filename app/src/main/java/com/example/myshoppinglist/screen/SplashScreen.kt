package com.example.myshoppinglist.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.CreateLoginStatus
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.services.controller.PurchaseController
import com.example.myshoppinglist.ui.activity.MainActivity
import com.example.myshoppinglist.ui.activity.ROUTE_INITIAL
import com.example.myshoppinglist.ui.activity.SplashScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun SplashScreen(contextActivity: SplashScreenActivity, finish: ((intent: Intent) -> Unit)) {

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val context = LocalContext.current

    LaunchedEffect(Unit) {

        lifecycleOwner.lifecycleScope.launch {

            UserLoggedShared.getInstance(context)

            val email = UserLoggedShared.getEmailUserCurrent()

            UserLoggedShared.getInstance(context)

            delay(3000)

            UserInstanceImpl.getInstance(context).getUserViewModelCurrent()
                .findUserByName(email).observe(lifecycleOwner) { userDTO ->
                    val route =
                        if (userDTO != null && userDTO.email.isNotBlank()) {

                            if(userDTO.status == CreateLoginStatus.INCOMPLETE){
                                Screen.PagerCreated
                            }else{
                                Screen.Home
                            }
                        } else {
                            Screen.ChoiceLogin
                        }

                    val intent = Intent(contextActivity, MainActivity::class.java)

                    intent.putExtra(
                        ROUTE_INITIAL,
                        route.name
                    )

                    finish(intent)

                }
        }

    }
}