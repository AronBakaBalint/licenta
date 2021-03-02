package com.example.licenta_mobile.activity.register

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding>(R.layout.fragment_register) {

    override val viewModel: RegisterViewModel by activityViewModels()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    private fun setupObservers(){

        viewModel.registerSuccess.observe(viewLifecycleOwner, {
            registerCommandListener?.returnToLoginPage()
        })

        viewModel.registerToastMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

}