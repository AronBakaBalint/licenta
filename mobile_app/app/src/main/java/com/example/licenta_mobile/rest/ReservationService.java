package com.example.licenta_mobile.rest;

import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.ReservationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService {
    @GET( "/parking" )
    @Headers({ "Content-Type: application/json" } )
    Call<List<ParkingPlaceDto>> getAllParkingPlaces (@Header("Authorization") String auth);

    @POST("/reservation")
    @Headers({ "Content-Type: application/json" } )
    Call<MessageDto> reserveParkingPlace (@Header("Authorization") String auth, @Body ReservationDto reservationDto);

    @GET("/parking/{id}")
    @Headers({ "Content-Type: application/json" } )
    Call<MessageDto> getReservationStatus (@Header("Authorization") String auth, @Path("id") Integer id);
}
