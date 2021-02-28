package com.example.licenta_mobile.activity.main.parkingspots

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.login.LoginCommandListener
import com.example.licenta_mobile.activity.main.MainCommandListener
import com.example.licenta_mobile.databinding.FragmentParkingLotBinding
import java.lang.ClassCastException

class ParkingSpotsFragment : Fragment() {

    private lateinit var viewModel: ParkingSpotsViewModel

    private lateinit var binding: FragmentParkingLotBinding

    private var mainCommandListener: MainCommandListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parking_lot, container, false)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainCommandListener = context as MainCommandListener
        } catch (e : ClassCastException) {
            println("Activity should implement MainCommandListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainCommandListener = null
    }

    private fun setupObservers() {
        viewModel.reservedSpotId.observe(viewLifecycleOwner, {
            mainCommandListener?.onGoToReservation(it)
        })
    }

}