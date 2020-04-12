package com.example.licenta_mobile.rest;

import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.dto.RegistrationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegistrationService {
    @POST( "/register" )
    @Headers( {
            "Content-Type: application/json"
    } )
    Call<MessageDto> register (@Body RegistrationDto registrationDto);
}
