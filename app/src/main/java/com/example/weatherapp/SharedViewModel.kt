package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val location = MutableLiveData<String>()
    fun sendLocation(text: String){
        location.value = text
    }
}