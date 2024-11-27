package com.example.a16_6_bottomnavigationview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()
    val login: LiveData<String> = _login

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    fun updateLogin(newLogin: String) {
        _login.value = newLogin
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
}
