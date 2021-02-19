package com.example.licenta_mobile.activity.main.reservations

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R
import com.example.licenta_mobile.databinding.FragmentReservationHistoryBinding
import com.google.zxing.WriterException

class ReservationsFragment : Fragment() {

    private lateinit var viewModel: ReservationsViewModel

    private lateinit var binding: FragmentReservationHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_history, container, false)
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

    private fun setupObservers() {
        viewModel.reservations.observe(viewLifecycleOwner, {
            val lView = binding.reservationsList
            val adapter = ReservationsListAdapter(it, requireActivity())
            adapter.qrCodeToDisplay.observe(viewLifecycleOwner, { code ->
                showQRCodeDialog(code)
            })
            adapter.reservationToCancel.observe(viewLifecycleOwner, { reservation ->
                run {
                    viewModel.cancelReservation(reservation)
                }
            })
            lView.adapter = adapter
        })

        viewModel.activateFilter.observe(viewLifecycleOwner, {
            val lView = binding.reservationsList
            var reservationsList = viewModel.reservations.value!!
            if (!binding.switch1.isChecked) {
                reservationsList = reservationsList.filter { r -> r.status != "cancelled" && r.status != "finished" }
            }
            val adapter = ReservationsListAdapter(reservationsList, requireActivity())
            adapter.qrCodeToDisplay.observe(viewLifecycleOwner, { code ->
                showQRCodeDialog(code)
            })
            adapter.reservationToCancel.observe(viewLifecycleOwner, { reservation ->
                run {
                    viewModel.cancelReservation(reservation)
                }
            })
            lView.adapter = adapter
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    //source
    //https://www.c-sharpcorner.com/article/how-to-generate-qr-code-in-android/
    private fun showQRCodeDialog(reservationId: String) {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x * 0.95).toInt()
        val qrgEncoder = QRGEncoder(reservationId, null, QRGContents.Type.TEXT, width)
        val builder = Dialog(requireContext())
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setOnDismissListener {
            //nothing;
        }
        val imageView = ImageView(requireContext())
        try {
            imageView.setImageBitmap(qrgEncoder.encodeAsBitmap())
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        builder.addContentView(imageView, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        builder.show()
    }
}