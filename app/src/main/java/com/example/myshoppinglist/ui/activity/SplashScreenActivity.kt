package com.example.myshoppinglist.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.screen.SplashScreen
import kotlinx.coroutines.delay

val ROUTE_INITIAL = "route"

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen(contextActivity = this@SplashScreenActivity, finish = {
                startActivity(it)
                finish()
            })
        }
    }

    override fun finish() {
        super.finish()
    }
}