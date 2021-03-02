package com.example.licenta_mobile.activity.login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by activityViewModels()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
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