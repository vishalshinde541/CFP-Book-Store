package com.example.bookstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.mode.AuthListener
import com.example.bookstore.mode.User
import com.example.bookstore.mode.UserAuthService

class RegisterViewModel(var userAuthService: UserAuthService): ViewModel() {

    private var userRegisterStatus = MutableLiveData<AuthListener>()
    val _userRegisterStatus: LiveData<AuthListener> = userRegisterStatus

    fun registerUSer(user: User){
        userAuthService.userRegister(user,{
            userRegisterStatus.value = it
        })
    }

}