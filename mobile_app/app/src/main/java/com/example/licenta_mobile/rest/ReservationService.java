package com.example.licenta_mobile.rest;

import com.example.licenta_mobile.dto.ParkingPlaceDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ReservationService {
    @GET( "/parking" )
    @Headers( {
            "Content-Type: application/json"
        } )
    Call<List<ParkingPlaceDto>> getAllParkingPlaces (@Header("Authorization") String auth);
}
