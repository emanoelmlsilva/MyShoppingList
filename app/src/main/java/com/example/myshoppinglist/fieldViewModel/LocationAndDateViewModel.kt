package com.example.myshoppinglist.fieldViewModel

import androidx.lifecycle.MutableLiveData

class LocationAndDateViewModel : BaseFieldViewModel() {

    var locale: MutableLiveData<String> = MutableLiveData("")
    var dateCurrent: MutableLiveData<String> = MutableLiveData("")

    val localeError: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onChangeLocale(newLocale: String) {
        locale.value = newLocale
        localeError.value = newLocale.isBlank()
    }

    fun onChangeDateCurrent(newDateCurrent: String) {
        dateCurrent.value = newDateCurrent
    }
}