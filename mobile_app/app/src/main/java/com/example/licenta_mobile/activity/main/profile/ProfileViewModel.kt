package com.example.licenta_mobile.activity.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.dto.MoneyTransferDto
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.rest.RestClient
import com.example.licenta_mobile.rest.UserDataService
import com.example.licenta_mobile.security.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class ProfileViewModel : ViewModel() {

    val username = UserData.userName

    val currentSold = MutableLiveData("${format(UserData.currentSold)} LEI")

    private val _showMoneyTransferDialog = MutableLiveData<Boolean>()
    val showMoneyTransferDialog: LiveData<Boolean> = _showMoneyTransferDialog

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val userDataService = RestClient.client!!.create(UserDataService::class.java)

    fun showMoneyTransferDialog() {
        _showMoneyTransferDialog.value = true
    }

    fun addMoney(amount: Double) {

        val moneyTransferDto = MoneyTransferDto()
        moneyTransferDto.userId = UserData.userId
        moneyTransferDto.amount = amount

        val call = userDataService.transferMoney("Bearer " + Token.jwtToken, moneyTransferDto)
        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    _toastMsg.value = "$amount transferred successfully"
                    UserData.update()
                    currentSold.value = "${format(amount + UserData.currentSold)} LEI"
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                println(t.message)
                call.cancel()
            }
        })
    }

    private fun format(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value)
    }
}