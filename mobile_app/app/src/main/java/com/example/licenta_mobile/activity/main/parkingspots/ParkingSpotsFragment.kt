package com.example.licenta_mobile.activity.main.parkingspots

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.example.licenta_mobile.R
import com.example.licenta_mobile.activity.main.MainCommandListener
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentParkingLotBinding
import com.example.licenta_mobile.factory.ParkingSpotVMFactory


class ParkingSpotsFragment : BaseFragment<ParkingSpotsViewModel, FragmentParkingLotBinding>(R.layout.fragment_parking_lot) {

    override val viewModel: ParkingSpotsViewModel by activityViewModels { ParkingSpotVMFactory { reserve(it) } }

    private var mainCommandListener: MainCommandListener? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainCommandListener = context as MainCommandListener
        } catch (e: ClassCastException) {
            println("Activity should implement MainCommandListener")
        }
    }

    private fun reserve(spotId: Int){
        mainCommandListener?.onGoToReservation(spotId)
    }

}