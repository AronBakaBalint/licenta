package com.example.licenta_mobile.activity.register

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dto.RegistrationDto
import com.example.licenta_mobile.rest.RegistrationService
import com.example.licenta_mobile.rest.RestClient.client
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    
    private val registrationService = client!!.create(RegistrationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_registration)
    }

    fun confirmRegistration(view: View?) {
        val name = (findViewById<View>(R.id.full_name) as TextInputEditText).text.toString()
        val email = (findViewById<View>(R.id.email) as TextInputEditText).text.toString()
        val username = (findViewById<View>(R.id.username) as TextInputEditText).text.toString()
        val password = (findViewById<View>(R.id.password) as TextInputEditText).text.toString()
        val password2 = (findViewById<View>(R.id.repeat_password) as TextInputEditText).text.toString()
        if (password != password2) {
            Toast.makeText(this, "The two passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            registerUser(name, username, password, email)
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
                        Toast.makeText(this@RegistrationActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegistrationActivity, "Username already exists!", Toast.LENGTH_SHORT).show()
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