package com.example.licenta_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta_mobile.dto.JwtTokenDto;
import com.example.licenta_mobile.dto.LoginRequestDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.LoginService;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.security.Token;

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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void loginRequest(View view) {
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        Call<JwtTokenDto> call = loginService.authenticate(loginRequestDto);
        call.enqueue(new Callback<JwtTokenDto>(){
            @Override
            public void onResponse(Call<JwtTokenDto> call, Response<JwtTokenDto> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().getToken();
                    if ("false".equals(responseBody)) {
                        Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                    } else {
                        Token.setJwtToken(responseBody);
                        UserData.update();
                        Intent intent = new Intent(LoginActivity.this, ParkingActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JwtTokenDto> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Connection Error! Please try again later!", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void register(View view){
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
