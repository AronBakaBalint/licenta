package com.example.licenta_mobile.activity.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R

class SpotReservationFragment : Fragment() {

    private lateinit var viewModel: SpotReservationViewModel

    companion object {
        fun newInstance() = SpotReservationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_spot_reservation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SpotReservationViewModel::class.java)
        // TODO: Use the ViewModel
    }
}