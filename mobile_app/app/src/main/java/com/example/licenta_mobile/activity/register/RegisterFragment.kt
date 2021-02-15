package com.example.licenta_mobile.activity.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    private lateinit var binding: FragmentRegisterBinding

    private var registerCommandListener: RegisterCommandListener? = null

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            registerCommandListener = context as RegisterCommandListener
        } catch (e : ClassCastException) {
            println("Activity should implement RegisterCommandListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        registerCommandListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    private fun setupObservers(){

        viewModel.registerSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Registration successful!", Toast.LENGTH_LONG).show()
            registerCommandListener?.returnToLoginPage()
        })
    }

}