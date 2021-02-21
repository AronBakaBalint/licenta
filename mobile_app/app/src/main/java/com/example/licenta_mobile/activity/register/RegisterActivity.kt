package com.example.licenta_mobile.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.login.LoginActivity
import com.example.licenta_mobile.activity.main.MainActivity

class RegisterActivity : AppCompatActivity(), RegisterCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RegisterFragment.newInstance())
                    .commitNow()
        }
    }

    override fun returnToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}