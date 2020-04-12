package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    public void confirmRegistration(View view){
        EditText namet = findViewById(R.id.regname);
        EditText usernamet = findViewById(R.id.regusername);
        EditText emailt = findViewById(R.id.regemail);
        EditText passwordt = findViewById(R.id.regpassword);
        EditText password2t = findViewById(R.id.regpassword2);

        String name = namet.getText().toString();
        String username = usernamet.getText().toString();
        String email = emailt.getText().toString();
        String password = passwordt.getText().toString();
        String password2 = password2t.getText().toString();

        registerUser(name, username, password, email);
    }

    private void registerUser(String name, String username, String password, String email){
        RegistrationDto registrationDto = new RegistrationDto(name, username, email, password);
        Call<MessageDto> call = registrationService.register(registrationDto);
        call.enqueue(new Callback<MessageDto>(){

            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().getMessage();
                    if("ok".equals(responseBody)){
                        Toast.makeText(RegistrationActivity.this,"Registration successful!", Toast.LENGTH_SHORT).show();
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
