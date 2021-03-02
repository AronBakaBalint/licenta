package com.example.licenta_mobile.activity.register

import android.content.Intent
import android.os.Bundle
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.login.LoginActivity
import com.example.licenta_mobile.base.BaseActivity

class RegisterActivity : BaseActivity(), RegisterCommandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupFragment()
    }

    private fun setupFragment() {
        addFragment(R.id.container, RegisterFragment.newInstance())
    }

    override fun returnToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}