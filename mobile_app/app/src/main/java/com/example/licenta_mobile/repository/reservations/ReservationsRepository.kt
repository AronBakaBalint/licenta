package com.example.licenta_mobile.repository.reservations

import com.example.licenta_mobile.dto.ReservationDto

interface ReservationsRepository {

    fun loadReservationHistory(reservationHistoryResponse: (reservations: List<ReservationDto>) -> Unit)

    fun cancelReservation(reservationId: Int, reservationCancelResponse: (response: Boolean) -> Unit)

    fun makeReservation(reservationDto: ReservationDto, reservationResponse: (response: Int) -> Unit)
}