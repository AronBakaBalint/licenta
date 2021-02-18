package com.example.licenta_mobile.activity.main.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.main.parkingspots.ParkingSpotsViewModel
import com.example.licenta_mobile.adapter.ReservationsListAdapter
import com.example.licenta_mobile.databinding.FragmentParkingLotBinding
import com.example.licenta_mobile.databinding.FragmentReservationHistoryBinding

class ReservationsFragment : Fragment() {

    private lateinit var viewModel: ReservationsViewModel

    private lateinit var binding: FragmentReservationHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_history, container, false)
      binding.lifecycleOwner = this
      return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      viewModel = ViewModelProvider(this).get(ReservationsViewModel::class.java)
      binding.viewModel = viewModel
      setupObservers()
      // TODO: Use the ViewModel
    }

    private fun setupObservers(){
        viewModel.reservations.observe(viewLifecycleOwner, {
            val adapter = ReservationsListAdapter(it, requireActivity())
            val lView = binding.reservationsList
            lView.adapter = adapter
            println(it.size)
        })
    }
}