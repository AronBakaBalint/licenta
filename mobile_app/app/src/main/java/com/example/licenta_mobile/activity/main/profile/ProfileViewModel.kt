package com.example.licenta_mobile.activity.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.model.UserData
import com.example.licenta_mobile.repository.user.UserRepository
import com.example.licenta_mobile.repository.user.UserRepositoryImpl
import java.math.RoundingMode
import java.text.DecimalFormat

class ProfileViewModel : BaseViewModel() {

    val username = UserData.userName

    val currentSold = MutableLiveData("${format(UserData.currentSold)} LEI")

    private val _showMoneyTransferDialog = MutableLiveData<Boolean>()
    val showMoneyTransferDialog: LiveData<Boolean> = _showMoneyTransferDialog

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val userRepository: UserRepository = UserRepositoryImpl()

    fun showMoneyTransferDialog() {
        _showMoneyTransferDialog.value = true
    }

    fun addMoney(amount: Double) {
        userRepository.addMoney(UserData.userId, amount) { response ->
            if (response) {
                _toastMsg.value = "$amount transferred successfully"
                currentSold.value = "${format(amount + UserData.currentSold)} LEI"
            }
        }
    }

    private fun format(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value)
    }
}