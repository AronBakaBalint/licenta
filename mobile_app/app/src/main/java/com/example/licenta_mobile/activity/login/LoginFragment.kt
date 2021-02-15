package com.example.licenta_mobile.activity.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.databinding.FragmentLoginBinding
import java.lang.ClassCastException

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: FragmentLoginBinding

    private var loginCommandListener: LoginCommandListener? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            loginCommandListener = context as LoginCommandListener
        } catch (e : ClassCastException) {
            println("Activity should implement LoginCommandListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        loginCommandListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    private fun setupObservers(){
        viewModel.shouldLogin.observe(viewLifecycleOwner, {
            loginCommandListener?.onLogin()
        })

        viewModel.loginToastMessage.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.registerCommand.observe(viewLifecycleOwner, {
            loginCommandListener?.onGoToRegister()
        })
    }

}