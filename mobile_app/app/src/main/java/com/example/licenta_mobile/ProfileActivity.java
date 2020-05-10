package com.example.licenta_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta_mobile.dialog.MoneyTransferDialog;
import com.example.licenta_mobile.dialog.ReservationDialog;
import com.example.licenta_mobile.dto.MoneyTransferDto;
import com.example.licenta_mobile.dto.ParkingPlaceDto;
import com.example.licenta_mobile.model.UserData;
import com.example.licenta_mobile.rest.RestClient;
import com.example.licenta_mobile.rest.UserDataService;
import com.example.licenta_mobile.security.Token;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private MoneyTransferDialog moneyTransferDialog;

    private UserDataService userDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userDataService = RestClient.getClient().create(UserDataService.class);
        TextView currentSold = findViewById(R.id.current_sold);
        currentSold.setText(UserData.getCurrentSold()+" LEI");
        TextView username = findViewById(R.id.profile_username);
        username.setText(UserData.getUserName());
    }

    public void addMoney(View view){
        showAddMoneyDialog();
    }

    public void confirmTransfer(View view){
        try {
            Double introducedAmount = Double.parseDouble(moneyTransferDialog.getIntroducedAmount().getText().toString());
            MoneyTransferDto moneyTransferDto = new MoneyTransferDto();
            moneyTransferDto.setUserId(UserData.getUserId());
            moneyTransferDto.setAmount(introducedAmount);
            transfer(moneyTransferDto);
        }catch(Exception npe){
            System.out.println("No value introduced");
        } finally {
            moneyTransferDialog.cancel();
        }
    }

    private void transfer(final MoneyTransferDto moneyTransferDto){
        Call<Void> call = userDataService.transferMoney("Bearer "+ Token.getJwtToken(), moneyTransferDto);
        call.enqueue(new Callback<Void>(){

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    TextView currentSold = findViewById(R.id.current_sold);
                    Double newAmount = Double.valueOf(UserData.getCurrentSold())+moneyTransferDto.getAmount();
                    currentSold.setText(newAmount+" LEI");
                    Toast.makeText(ProfileActivity.this, moneyTransferDto.getAmount()+" LEI added succesfully", Toast.LENGTH_SHORT).show();
                    UserData.update();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                call.cancel();
            }
        });
    }

    private void showAddMoneyDialog(){
        moneyTransferDialog  = new MoneyTransferDialog(this);
        moneyTransferDialog.show();
    }
}
