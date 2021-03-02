package com.example.licenta_mobile.activity.reservation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentReservationSummaryBinding
import com.example.licenta_mobile.factory.ReservationVMFactory

class ReservationSummaryFragment(private val parkingSpotId: Int?) : BaseFragment<SpotReservationViewModel, FragmentReservationSummaryBinding>(R.layout.fragment_reservation_summary) {

    override val viewModel: SpotReservationViewModel by activityViewModels{ ReservationVMFactory(parkingSpotId) }

    private var reservationCommandListener: ReservationCommandListener? = null

    companion object {
        fun newInstance(parkingSpotId: Int?) = ReservationSummaryFragment(parkingSpotId)
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

    private fun setupObservers() {

        viewModel.cancelReservation.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show()
            reservationCommandListener?.cancelReservation()
        })
    }
}