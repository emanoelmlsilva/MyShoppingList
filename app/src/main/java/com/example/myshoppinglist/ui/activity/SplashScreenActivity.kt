package com.example.myshoppinglist.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import kotlinx.coroutines.delay

val ROUTE_INITIAL = "route"

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

    private lateinit var userDTOLiveData: LiveData<UserDTO>
    private lateinit var userDTOObserver: Observer<UserDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {

            UserLoggedShared.getInstance(applicationContext)

            val email = UserLoggedShared.getEmailUserCurrent()

            UserInstanceImpl.getInstance(applicationContext)

            delay(3000)

            userDTOLiveData = UserInstanceImpl.getInstance(applicationContext).getUserViewModelCurrent().findUserByName(email)

            userDTOObserver = Observer<UserDTO> { userDTO ->
                val route =
                    if (userDTO != null && userDTO.email.isNotBlank()) Screen.Home else Screen.ChoiceLogin

                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)

                intent.putExtra(
                    ROUTE_INITIAL,
                    route.name
                )

                startActivity(intent)
                finish()
            }

            userDTOLiveData.observeForever(
                userDTOObserver
            )
        }
    }

    override fun finish() {
        userDTOLiveData.removeObserver(userDTOObserver)
        super.finish()
    }
}