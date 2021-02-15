package com.example.licenta_mobile.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.main.MainActivity

class LoginActivity : AppCompatActivity(), LoginCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onLogin() {
        val intent = Intent(LoginActivity@this, MainActivity::class.java)
        startActivity(intent)
    }
}