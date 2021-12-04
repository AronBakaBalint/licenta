package com.example.licenta_mobile.activity.reservation

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.di.DaggerAppComponent
import com.example.licenta_mobile.dto.ReservationDto
import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepository
import com.example.licenta_mobile.repository.prices.PriceRepository
import com.example.licenta_mobile.repository.reservations.ReservationsRepository
import java.time.LocalDate
import javax.inject.Inject


class SpotReservationViewModel(private val spotId: Int?) : BaseViewModel() {

    private val _cancelReservation = MutableLiveData<Boolean>()
    val cancelReservation: LiveData<Boolean> = _cancelReservation

    private val _navigateToHourPicker = MutableLiveData<Boolean>()
    val navigateToHourPicker: LiveData<Boolean> = _navigateToHourPicker

    private val _navigateToLicensePlate = MutableLiveData<Boolean>()
    val navigateToLicensePlate: LiveData<Boolean> = _navigateToLicensePlate

    private val _navigateToSummary = MutableLiveData<Boolean>()
    val navigateToSummary: LiveData<Boolean> = _navigateToSummary

    private val pricePerHour = ObservableField(0.0)

    private val _price = MutableLiveData(0.0)
    val price = _price.map {
        formatPrice(it)
    }

    val licensePlate = ObservableField("")

    var reservationHours: List<Int>? = null

    var selectedHours = MutableLiveData<MutableList<Int>>()

    private var selectedDate = LocalDate.now()

    val progressBar = ObservableField(View.GONE)

    private val _reservationInfo = MutableLiveData<String>()
    val reservationInfo: LiveData<String> = _reservationInfo

    @Inject
    lateinit var reservationRepository: ReservationsRepository

    @Inject
    lateinit var parkingSpotsRepository: ParkingSpotsRepository

    @Inject
    lateinit var priceRepository: PriceRepository

    init {
        DaggerAppComponent.create().inject(this)
        selectedHours.value = arrayListOf()
        getParkingSpotSchedule()
        getPricePerHour()
    }

    fun navigateToHourPicker() {
        _navigateToHourPicker.value = true
    }

    fun navigateToLicensePlate() {
        _navigateToLicensePlate.value = true
    }

    fun navigateToSummary() {
        _navigateToSummary.value = true
    }

    fun cancelReservation() {
        _cancelReservation.value = true
    }

    fun setSelectedDate(year: Int, month: Int, day: Int) {
        selectedDate = LocalDate.of(year, month, day)
        getParkingSpotSchedule()
    }

    fun confirmReservation() {
        val reservationDto = ReservationDto()
        reservationDto.parkingSpotId = spotId!!
        reservationDto.licensePlate = licensePlate.get()!!
        reservationDto.startTime = selectedDate.toString()
        reservationDto.duration = selectedHours.value!!

        showProgressBar()

        reservationRepository.makeReservation(reservationDto) {
            hideProgressBar()
            _reservationInfo.value = "Your reservation has been made. You can find the QR code in the reservation history."
        }
    }

    private fun getParkingSpotSchedule() {
        val reservedHours = arrayListOf<Int>()
        parkingSpotsRepository.getReservationSchedule(spotId!!, selectedDate) { reservations ->
            reservations.forEach { reservation ->
                reservation.duration.forEach { reservedHours.add(it) }
            }
        }
        reservationHours = reservedHours
    }

    fun getSelectedDate(): String {
        return "${selectedDate.dayOfMonth}/${selectedDate.monthValue}/${selectedDate.year}"
    }

    fun onHourSelected(hour: Int) {
        val selectedHoursList = selectedHours.value!!
        if(selectedHoursList.contains(hour)) {
            selectedHoursList.remove(hour)
        } else {
            selectedHoursList.add(hour)
        }
        selectedHours.value = selectedHoursList
        updatePrice()
    }

    private fun getPricePerHour() {
        priceRepository.getPricePerHour { pricePerHour.set(it) }
    }

    private fun formatPrice(price: Double): String {
        return "TOTAL $price LEI"
    }

    fun getReservationInterval(): String {
        return "${formatHour(selectedHours.value?.minOrNull()!!)} - ${formatHour(selectedHours.value?.maxOrNull()!! + 1)}"
    }

    private fun formatHour(hour: Int): String {
        return if(hour < 10) {
            "0$hour:00"
        } else {
            "$hour:00"
        }
    }

    private fun updatePrice() {
        _price.value = selectedHours.value?.size!! * pricePerHour.get()!!
    }

    private fun hideProgressBar() {
        progressBar.set(View.GONE)
    }

    private fun showProgressBar() {
        progressBar.set(View.VISIBLE)
    }
}