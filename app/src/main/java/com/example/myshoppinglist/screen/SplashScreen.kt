package com.example.myshoppinglist.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.database.viewModels.CategoryViewModelDB
import com.example.myshoppinglist.database.viewModels.CreditCardViewModelDB
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
    UserLoggedShared.getInstance(context)
    val purchaseController: PurchaseController = PurchaseController.getData(context, lifecycleOwner)
    val creditCardViewModelDB = CreditCardViewModelDB(context, lifecycleOwner)

    var finishConsultRepeat by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = finishConsultRepeat){
        val email = UserLoggedShared.getEmailUserCurrent()

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

                if(finishConsultRepeat){
                    finish(intent)
                }

            }
    }

    LaunchedEffect(Unit) {

        lifecycleOwner.lifecycleScope.launch {

            UserLoggedShared.getInstance(context)

            creditCardViewModelDB.getAll().observeForever {
                Log.d("TESTANDO", "IT ${it.size}")
                purchaseController.getPurchaseRepeatHabilitated(it, object : Callback{
                    override fun onSuccess() {
                        finishConsultRepeat = true
                    }

                    override fun onFailed(messageError: String) {
                        finishConsultRepeat = true
                    }
                })
            }
        }
    }
}