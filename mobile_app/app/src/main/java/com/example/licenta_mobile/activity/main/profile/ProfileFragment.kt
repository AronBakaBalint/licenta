package com.example.licenta_mobile.activity.main.profile

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    private fun setupObservers() {

        viewModel.showMoneyTransferDialog.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                showAddMoneyDialog()
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), viewModel.toastMsg.value?.peekContent(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddMoneyDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Money")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("Add") { _, _ ->
            if(input.text.isNotEmpty()){
                viewModel.addMoney(input.text.toString().toDouble())
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}