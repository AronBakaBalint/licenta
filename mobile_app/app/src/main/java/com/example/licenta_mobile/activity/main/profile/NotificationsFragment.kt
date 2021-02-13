package com.example.licenta_mobile.activity.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.licenta_mobile.R

class NotificationsFragment : Fragment() {

  private lateinit var notificationsViewModel: NotificationsViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_profile, container, false)
    return root
  }
}