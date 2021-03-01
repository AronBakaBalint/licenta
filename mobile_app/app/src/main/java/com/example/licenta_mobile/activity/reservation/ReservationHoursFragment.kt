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
import com.example.licenta_mobile.databinding.FragmentReservationHoursBinding

class ReservationHoursFragment : Fragment() {

    private var binding: FragmentReservationHoursBinding? = null

    private val sharedViewModel: SpotReservationViewModel by activityViewModels()

    private var reservationCommandListener: ReservationCommandListener? = null

    companion object {
        fun newInstance() = ReservationHoursFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_hours, container, false)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = sharedViewModel
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

        sharedViewModel.cancelReservation.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show()
            reservationCommandListener?.cancelReservation()
        })

        sharedViewModel.navigateToLicensePlate.observe(viewLifecycleOwner, {
            reservationCommandListener?.navigateToLicensePlate()
        })
    }
}