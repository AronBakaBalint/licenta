package com.example.licenta_mobile.activity.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.licenta_mobile.model.UserData

class ProfileViewModel : ViewModel() {

    val username = UserData.userName

    val email = UserData.email

    val currentSold = "${UserData.currentSold} LEI"
}