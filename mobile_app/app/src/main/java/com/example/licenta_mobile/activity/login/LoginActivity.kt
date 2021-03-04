package com.example.licenta_mobile.activity.login

import android.content.Intent
import android.os.Bundle
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.main.MainActivity
import com.example.licenta_mobile.activity.register.RegisterActivity
import com.example.licenta_mobile.base.BaseActivity

class LoginActivity : BaseActivity(), LoginCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupFragment()
    }

    private fun setupFragment() {
        addFragment(R.id.container, LoginFragment.newInstance())
    }

    override fun onLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onGoToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}