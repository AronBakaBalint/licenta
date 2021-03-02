package com.example.licenta_mobile.activity.reservation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentReservationLicensePlateBinding
import com.example.licenta_mobile.factory.ReservationVMFactory

class ReservationLicensePlateFragment(private val parkingSpotId: Int?) : BaseFragment<SpotReservationViewModel, FragmentReservationLicensePlateBinding>(R.layout.fragment_reservation_license_plate) {

    override val viewModel: SpotReservationViewModel by activityViewModels{ ReservationVMFactory(parkingSpotId) }

    private var reservationCommandListener: ReservationCommandListener? = null

    companion object {
        fun newInstance(parkingSpotId: Int?) = ReservationLicensePlateFragment(parkingSpotId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            reservationCommandListener = context as ReservationCommandListener
        } catch (e : ClassCastException) {
            println("Activity should implement ReservationCommandListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        reservationCommandListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {

        viewModel.cancelReservation.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show()
            reservationCommandListener?.cancelReservation()
        })

        viewModel.navigateToSummary.observe(viewLifecycleOwner, {
            reservationCommandListener?.navigateToSummary()
        })
    }

}