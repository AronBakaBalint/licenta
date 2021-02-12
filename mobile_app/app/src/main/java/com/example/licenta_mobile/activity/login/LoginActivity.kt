package com.example.licenta_mobile.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.activity.main.ParkingActivity
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.register.RegistrationActivity
import com.example.licenta_mobile.dto.LoginRequestDto
import com.example.licenta_mobile.model.UserData.update
import com.example.licenta_mobile.rest.LoginService
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.security.Token
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val loginService = client!!.create(LoginService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun loginRequest(view: View?) {
        val username = (findViewById<View>(R.id.username) as TextInputEditText).text.toString()
        val password = (findViewById<View>(R.id.password) as TextInputEditText).text.toString()
        val loginRequestDto = LoginRequestDto(username, password)
        val call = loginService.authenticate(loginRequestDto)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if ("false" == responseBody) {
                        Toast.makeText(this@LoginActivity, "Incorrect Username or Password", Toast.LENGTH_LONG).show()
                    } else {
                        Token.jwtToken = responseBody
                        update()
                        val intent = Intent(this@LoginActivity, ParkingActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Connection Error! Please try again later!", Toast.LENGTH_LONG).show()
                call.cancel()
            }
        })
    }

    fun register(view: View?) {
        val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
        startActivity(intent)
    }
}