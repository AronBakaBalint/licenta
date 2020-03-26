package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.licenta_mobile.dto.JwtTokenDto;
import com.example.licenta_mobile.dto.LoginRequestDto;
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
}
