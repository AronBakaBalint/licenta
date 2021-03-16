package com.example.licenta_mobile.repository.prices

import com.example.licenta_mobile.rest.ReservationService
import com.example.licenta_mobile.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PriceRepositoryImpl @Inject constructor(): PriceRepository {

    private val reservationService = RestClient.client?.create(ReservationService::class.java)

    override fun getPricePerHour(price: (priceResponse: Double) -> Unit) {
        val call = reservationService?.getPricePerHour()
        call?.enqueue(object : Callback<Double> {
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    price.invoke(responseBody)
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }
}