package com.example.bookstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.mode.AuthListener
import com.example.bookstore.mode.User
import com.example.bookstore.mode.UserAuthService

class LoginViewModel (var userAuthService: UserAuthService): ViewModel() {

    private var userLoinStatus = MutableLiveData<AuthListener>()
    val _userLoginStatus: LiveData<AuthListener> = userLoinStatus

    fun loginUser(user: User){
        userAuthService.userLogin(user,{
            userLoinStatus.value = it
        })
    }
}