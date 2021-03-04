package com.example.licenta_mobile.repository.prices

interface PriceRepository {

    fun getPricePerHour(price: (response: Double) -> Unit)
}