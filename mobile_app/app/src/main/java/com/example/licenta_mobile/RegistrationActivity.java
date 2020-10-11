package com.example.licenta_mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta_mobile.dto.MessageDto;
import com.example.licenta_mobile.dto.RegistrationDto;
import com.example.licenta_mobile.rest.RegistrationService;
import com.example.licenta_mobile.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private RegistrationService registrationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationService = RestClient.getClient().create(RegistrationService.class);
    }

    public void confirmRegistration(View view) {
        String name = ((EditText) findViewById(R.id.regname)).getText().toString();
        String username = ((EditText) findViewById(R.id.regusername)).getText().toString();
        String email = ((EditText) findViewById(R.id.regemail)).getText().toString();
        String password = ((EditText) findViewById(R.id.regpassword)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.regpassword2)).getText().toString();

        if (!password.equals(password2)) {
            Toast.makeText(this, "The two passwords do not match", Toast.LENGTH_LONG).show();
        } else {
            registerUser(name, username, password, email);
        }
    }

    private void registerUser(String name, String username, String password, String email) {
        RegistrationDto registrationDto = new RegistrationDto(name, username, email, password);
        Call<MessageDto> call = registrationService.register(registrationDto);
        call.enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().getMessage();
                    if ("ok".equals(responseBody)) {
                        Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }
}
