package com.example.licenta_mobile.activity.main.parkingspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.login.LoginViewModel
import com.example.licenta_mobile.databinding.FragmentLoginBinding
import com.example.licenta_mobile.databinding.FragmentParkingLotBinding

class ParkingSpotsFragment : Fragment() {

  private lateinit var viewModel: ParkingSpotsViewModel

  private lateinit var binding: FragmentParkingLotBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_parking_lot, container, false)
    binding.lifecycleOwner = this
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(ParkingSpotsViewModel::class.java)
    binding.viewModel = viewModel
    setupObservers()
    // TODO: Use the ViewModel
  }

  private fun setupObservers(){

  }

}