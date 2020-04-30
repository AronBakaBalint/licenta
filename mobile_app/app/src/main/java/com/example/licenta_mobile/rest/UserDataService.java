package com.example.licenta_mobile.rest;

import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.RegistrationDto;
import com.example.licenta_mobile.dto.UnconfirmedReservationDto;
import com.example.licenta_mobile.dto.UserDataDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserDataService {
    @GET("/users/{id}")
    @Headers({ "Content-Type: application/json" } )
    Call<UserDataDto> getUserDetails (@Header("Authorization") String auth, @Path("id") Integer id);
}
