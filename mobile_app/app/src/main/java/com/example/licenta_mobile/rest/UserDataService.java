package com.example.licenta_mobile.rest;

import com.example.licenta_mobile.dto.MoneyTransferDto;
import com.example.licenta_mobile.dto.UserDataDto;

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

    @POST("/users/addMoney")
    @Headers({ "Content-Type: application/json" } )
    Call<Void> transferMoney (@Header("Authorization") String auth,@Body MoneyTransferDto moneyTransferDto);
}
