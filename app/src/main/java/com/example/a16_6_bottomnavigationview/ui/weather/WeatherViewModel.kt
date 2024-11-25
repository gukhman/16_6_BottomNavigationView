package com.example.a16_6_bottomnavigationview.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Weather Fragment"
    }
    val text: LiveData<String> = _text
}