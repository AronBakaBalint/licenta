package com.example.licenta_mobile.activity.main.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.repository.user.UserRepository
import com.example.licenta_mobile.repository.user.UserRepositoryImpl
import java.math.RoundingMode
import java.text.DecimalFormat

class ProfileViewModel : BaseViewModel() {

    val username = ObservableField("")

    val balance = ObservableField("0 LEI")

    private val _showMoneyTransferDialog = MutableLiveData<Boolean>()
    val showMoneyTransferDialog: LiveData<Boolean> = _showMoneyTransferDialog

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val userRepository: UserRepository = UserRepositoryImpl()

    init {
        updateUserData()
    }

    fun showMoneyTransferDialog() {
        _showMoneyTransferDialog.value = true
    }

    fun addMoney(amount: Double) {
        userRepository.addMoney(amount) { response ->
            if (response) {
                _toastMsg.value = "$amount transferred successfully"
                updateUserData()
            }
        }
    }

    private fun updateUserData() {
        userRepository.getUserDetails { userData ->
            username.set(userData.username)
            balance.set("${format(userData.balance)} LEI")
        }
    }

    private fun format(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value)
    }
}