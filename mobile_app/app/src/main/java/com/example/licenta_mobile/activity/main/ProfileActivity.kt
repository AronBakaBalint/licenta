package com.example.licenta_mobile.activity.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta_mobile.R
import com.example.licenta_mobile.dialog.MoneyTransferDialog
import com.example.licenta_mobile.dto.MoneyTransferDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.model.UserData.update
import com.example.licenta_mobile.model.UserData.userId
import com.example.licenta_mobile.model.UserData.userName
import com.example.licenta_mobile.rest.RestClient.client
import com.example.licenta_mobile.rest.UserDataService
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class ProfileActivity : AppCompatActivity() {

    private lateinit var moneyTransferDialog: MoneyTransferDialog
    private val userDataService = client!!.create(UserDataService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        update()
        val currentSold = findViewById<TextView>(R.id.current_sold)
        val currentBalance = UserData.currentSold
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        currentSold.text = df.format(currentBalance) + " LEI"
        val username = findViewById<TextView>(R.id.profile_username)
        username.text = userName
        val email = findViewById<TextView>(R.id.profile_email)
        email.text = UserData.email
    }

    fun addMoney(view: View?) {
        showAddMoneyDialog()
    }

    fun confirmTransfer(view: View?) {
        try {
            val introducedAmount = moneyTransferDialog.introducedAmount.text.toString().toDouble()
            val moneyTransferDto = MoneyTransferDto()
            moneyTransferDto.userId = userId
            moneyTransferDto.amount = introducedAmount
            transfer(moneyTransferDto)
        } catch (npe: Exception) {
            println("No value introduced")
        } finally {
            moneyTransferDialog.cancel()
        }
    }

    private fun transfer(moneyTransferDto: MoneyTransferDto) {
        val call = userDataService.transferMoney("Bearer " + Token.jwtToken, moneyTransferDto)
        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    val currentSold = findViewById<TextView>(R.id.current_sold)
                    val newAmount = java.lang.Double.valueOf(UserData.currentSold) + moneyTransferDto.amount
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.CEILING
                    currentSold.text = df.format(newAmount) + " LEI"
                    Toast.makeText(this@ProfileActivity, moneyTransferDto.amount.toString() + " LEI added succesfully", Toast.LENGTH_SHORT).show()
                    update()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun showAddMoneyDialog() {
        moneyTransferDialog = MoneyTransferDialog(this)
        moneyTransferDialog.show()
    }
}