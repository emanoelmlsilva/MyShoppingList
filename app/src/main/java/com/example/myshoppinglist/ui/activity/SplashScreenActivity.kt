package com.example.myshoppinglist.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.R
import com.example.myshoppinglist.model.UserInstanceImpl
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)

        UserInstanceImpl.getInstance(this)

        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {

            delay(3000)

            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}