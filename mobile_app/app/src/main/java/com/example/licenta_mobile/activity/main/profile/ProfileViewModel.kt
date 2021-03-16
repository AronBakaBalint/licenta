package com.example.licenta_mobile.activity.main.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.licenta_mobile.base.BaseViewModel
import com.example.licenta_mobile.di.DaggerAppComponent
import com.example.licenta_mobile.repository.user.UserRepository
import com.example.licenta_mobile.repository.user.UserRepositoryImpl
import com.example.licenta_mobile.util.Event
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class ProfileViewModel : BaseViewModel() {

    val username = ObservableField("")

    val balance = ObservableField("0 LEI")

    private val _showMoneyTransferDialog = MutableLiveData<Event<Boolean>>()
    val showMoneyTransferDialog: LiveData<Event<Boolean>> = _showMoneyTransferDialog

    private val _toastMsg = MutableLiveData<Event<String>>()
    val toastMsg: LiveData<Event<String>> = _toastMsg

    @Inject
    lateinit var userRepository: UserRepository

    init {
        DaggerAppComponent.create().inject(this)
        updateUserData()
        DaggerAppComponent.create().inject(this)
    }

    fun showMoneyTransferDialog() {
        _showMoneyTransferDialog.value = Event(true)
    }

    fun addMoney(amount: Double) {
        userRepository.addMoney(amount) { response ->
            if (response) {
                _toastMsg.value = Event("$amount transferred successfully")
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