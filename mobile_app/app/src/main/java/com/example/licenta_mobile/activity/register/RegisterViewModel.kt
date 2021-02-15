package com.example.licenta_mobile.activity.register

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.RegistrationDto
import com.example.licenta_mobile.rest.RegistrationService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.util.addOnPropertyChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    val fullName = ObservableField("")

    val email = ObservableField("")

    val username = ObservableField("")
    val usernameError = ObservableField("")

    val password = ObservableField("")
    val passwordError = ObservableField("")

    val repeatPassword = ObservableField("")

    private val _registerSeuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSeuccess

    private val registrationService = client!!.create(RegistrationService::class.java)

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
            registerUser(fullName.get()!!, username.get()!!, password.get()!!, email.get()!!)
        }
    }

    private fun registerUser(name: String, username: String, password: String, email: String) {
        val registrationDto = RegistrationDto(name, username, email, password)
        val call = registrationService.register(registrationDto)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if (0 == responseBody) {
                        _registerSeuccess.postValue(true)
                    } else {
                        usernameError.set("Username already exists")
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }
}