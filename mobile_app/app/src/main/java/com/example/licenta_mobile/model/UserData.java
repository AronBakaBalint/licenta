package com.example.licenta_mobile.model;

import com.example.licenta_mobile.dto.UserDataDto;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.rest.UserDataService;
import com.example.licenta_mobile.security.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData {
    private static Integer userId;
    private static String userName;
    private static Double currentSold;
    private static String email;
    private static UserDataService userDataService = RestClient.getClient().create(UserDataService.class);

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserData.email = email;
    }

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        UserData.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserData.userName = userName;
    }

    public static Double getCurrentSold() {
        return currentSold;
    }

    public static void setCurrentSold(Double currentSold) {
        UserData.currentSold = currentSold;
    }

    public static void update() {
        Call<UserDataDto> call = userDataService.getUserDetails("Bearer " + Token.getJwtToken(), userId);
        call.enqueue(new Callback<UserDataDto>() {

            @Override
            public void onResponse(Call<UserDataDto> call, Response<UserDataDto> response) {
                if (response.isSuccessful()) {
                    userName = response.body().getUserName();
                    email = response.body().getEmail();
                    currentSold = response.body().getCurrentSold();
                }
            }

            @Override
            public void onFailure(Call<UserDataDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }
}
