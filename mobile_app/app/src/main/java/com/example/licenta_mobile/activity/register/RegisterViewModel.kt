package com.example.licenta_mobile.activity.register

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.activity.register.RegisterResponse.*
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.repository.user.UserRepository
import com.example.licenta_mobile.repository.user.UserRepositoryImpl
import com.example.licenta_mobile.util.addOnPropertyChanged

class RegisterViewModel : BaseViewModel() {

    val fullName = ObservableField("")

    val email = ObservableField("")

    val username = ObservableField("")
    val usernameError = ObservableField("")

    val password = ObservableField("")
    val passwordError = ObservableField("")

    val repeatPassword = ObservableField("")

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _registerToastMessage = MutableLiveData<String>()
    val registerToastMessage: LiveData<String> = _registerToastMessage

    private val userRepository: UserRepository = UserRepositoryImpl()

    init {
        username.addOnPropertyChanged {
            if(usernameError.get() != null) {
                usernameError.set(null)
            }
        }

        repeatPassword.addOnPropertyChanged {
            if(passwordError.get() != null) {
                passwordError.set(null)
            }
        }
    }

    fun register(){
        if (password.get() != repeatPassword.get()) {
            passwordError.set("The two passwords should match")
        } else {
            userRepository.register(fullName.get()!!, username.get()!!, email.get()!!, password.get()!!) {
                response -> when(response) {

                    SUCCESS -> {
                        _registerSuccess.value = true
                        _registerToastMessage.value = "Registration successful!"
                    }

                    USERNAME_EXISTS -> {
                        usernameError.set("Username already exists")
                    }

                    CONNECTION_ERROR -> {
                        _registerToastMessage.value = "Connection Error! Please try again later."
                    }
                }
            }
        }
    }
}