package com.example.myshoppinglist.fieldViewModel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.sharedPreference.UserLoggedShared
import com.example.myshoppinglist.model.UserInstanceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class BaseFieldViewModel: ViewModel() {
    val visibleLoading = MutableLiveData(false)
    private val handler = Handler(Looper.getMainLooper())
    private val TIME_DELAY = 800L

    fun startLoading(){
        visibleLoading.value = true
    }

    fun updateVisibleLoading(newVisibleLoading: Boolean){

        //melhorar esse codigo
        viewModelScope.launch(Dispatchers.Main) {
            if(!newVisibleLoading){
                delay(400)
            }
            visibleLoading.value = newVisibleLoading
            if (visibleLoading.value!!) {
                handler.postDelayed({
                    kotlin.run {
                        visibleLoading.value = false
                    }
                }, TIME_DELAY)
            }else{
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    open fun checkFields(): Boolean{
        return true
    }

    open fun checkFields(disableError: Boolean): Boolean{
        return true
    }

    fun getUser(): LiveData<UserDTO> {
        val email = UserLoggedShared.getEmailUserCurrent()

        return UserInstanceImpl.getUserViewModelCurrent().findUserByName(email)
    }
}