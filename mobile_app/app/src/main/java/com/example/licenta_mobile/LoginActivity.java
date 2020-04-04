package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;

import com.example.licenta_mobile.dto.JwtTokenDto;
import com.example.licenta_mobile.dto.LoginRequestDto;
import com.example.licenta_mobile.dto.RegistrationDto;
import com.example.licenta_mobile.rest.LoginService;
import com.example.licenta_mobile.rest.RegistrationService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginService = RestClient.getClient().create(LoginService.class);
    }

    public void loginRequest(View view) {
        EditText usernameET = findViewById(R.id.username);
        EditText passwordET = findViewById(R.id.password);

        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        Call<JwtTokenDto> call = loginService.authenticate(loginRequestDto);
        call.enqueue(new Callback<JwtTokenDto>(){

            @Override
            public void onResponse(Call<JwtTokenDto> call, Response<JwtTokenDto> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().getToken();
                    Token.setJwtToken(responseBody);
                    try{
                        System.out.println(getJson(responseBody));
                    } catch (UnsupportedEncodingException e){
                        System.out.println("Wrong encoding");
                    }
                    Intent intent = new Intent(LoginActivity.this, ParkingActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JwtTokenDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private  String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public void register(View view){
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }


}
