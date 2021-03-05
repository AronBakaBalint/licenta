package com.example.licenta_mobile.activity.main.reservations

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licenta_mobile.R
import com.example.licenta_mobile.base.BaseFragment
import com.example.licenta_mobile.databinding.FragmentReservationHistoryBinding
import com.google.zxing.WriterException
import java.util.*

class ReservationsFragment : BaseFragment<ReservationsViewModel, FragmentReservationHistoryBinding>(R.layout.fragment_reservation_history) {

    override val viewModel: ReservationsViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.viewModel = viewModel
        setupObservers()
        // TODO: Use the ViewModel
    }

    private fun setupObservers() {

        viewModel.updateReservationHistory.observe(viewLifecycleOwner, {
            val recyclerView = binding?.reservationsHistoryList
            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
            var reservationsList = viewModel.reservations.value!!
            if (!binding?.switchShowCompleted?.isChecked!!) {
                reservationsList = reservationsList.filter { r -> r.status != "cancelled" && r.status != "finished" }
            }
            val adapter = ReservationHistoryAdapter(reservationsList, { resId -> viewModel.cancelReservation(resId)}, { resId -> showQRCodeDialog(resId) })
            recyclerView?.adapter = adapter
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