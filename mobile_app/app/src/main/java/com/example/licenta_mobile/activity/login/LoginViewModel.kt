package com.example.licenta_mobile.activity.login

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.model.UserData.update
import com.example.licenta_mobile.rest.LoginService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    val username = ObservableField("")

    val password = ObservableField("")

    val progressBar = ObservableField(View.GONE)

    private val loginService = client!!.create(LoginService::class.java)

    private val _shouldLogin = MutableLiveData<Boolean>()
    val shouldLogin: LiveData<Boolean> = _shouldLogin

    private val _loginToastMessage = MutableLiveData<String>()
    val loginToastMessage: LiveData<String> = _loginToastMessage

    fun login(){
        showProgressBar()
        val loginRequestDto = LoginRequestDto(username.get()!!, password.get()!!)
        val call = loginService.authenticate(loginRequestDto)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                hideProgressBar()
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if ("false" == responseBody) {
                        _loginToastMessage.postValue("Incorrect Username or Password")
                    } else {
                        _shouldLogin.value = true
                        Token.jwtToken = responseBody
                        update()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                hideProgressBar()
                _loginToastMessage.postValue("Connection Error! Please try again later!")
                call.cancel()
            }
        })
    }

    private fun showProgressBar(){
        progressBar.set(View.VISIBLE)
    }

    private fun hideProgressBar(){
        progressBar.set(View.GONE)
    }
}