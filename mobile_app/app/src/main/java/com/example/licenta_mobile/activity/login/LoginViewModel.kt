package com.example.licenta_mobile.activity.login

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.activity.login.LoginResponse.*
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.di.DaggerAppComponent
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.repository.user.UserRepository
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    val username = ObservableField("asd31")

    val password = ObservableField("123")

    val progressBar = ObservableField(View.GONE)

    private val _shouldLogin = MutableLiveData<Boolean>()
    val shouldLogin: LiveData<Boolean> = _shouldLogin

    private val _registerCommand = MutableLiveData<Boolean>()
    val registerCommand: LiveData<Boolean> = _registerCommand

    private val _loginToastMessage = MutableLiveData<String>()
    val loginToastMessage: LiveData<String> = _loginToastMessage

    @Inject
    lateinit var userRepository: UserRepository

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun login() {
        showProgressBar()
        val loginRequestDto = LoginRequestDto(username.get()!!, password.get()!!)
        userRepository.login(loginRequestDto) { response ->
            hideProgressBar()
            when (response) {

                SUCCESS -> {
                    _shouldLogin.value = true
                }

                WRONG_PASSWORD -> {
                    _loginToastMessage.postValue("Incorrect Username or Password")
                }

                CONNECTION_ERROR -> {
                    _loginToastMessage.postValue("Connection Error! Please try again later!")
                }
            }
        }
    }

    fun register() {
        _registerCommand.value = true
    }

    private fun showProgressBar() {
        progressBar.set(View.VISIBLE)
    }

    private fun hideProgressBar() {
        progressBar.set(View.GONE)
    }
}