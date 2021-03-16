package com.example.licenta_mobile.di

import com.example.licenta_mobile.activity.login.LoginViewModel
import com.example.licenta_mobile.activity.main.parkingspots.ParkingSpotsViewModel
import com.example.licenta_mobile.activity.main.profile.ProfileViewModel
import com.example.licenta_mobile.activity.main.reservations.ReservationsViewModel
import com.example.licenta_mobile.activity.register.RegisterViewModel
import com.example.licenta_mobile.activity.reservation.SpotReservationViewModel
import com.example.licenta_mobile.di.repository.RepositoryModule
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface AppComponent {

    fun inject(loginViewModel: LoginViewModel): LoginViewModel

    fun inject(registerViewModel: RegisterViewModel): RegisterViewModel

    fun inject(profileViewModel: ProfileViewModel): ProfileViewModel

    fun inject(spotsViewModel: ParkingSpotsViewModel): ParkingSpotsViewModel

    fun inject(reservationViewModel: SpotReservationViewModel): SpotReservationViewModel

    fun inject(reservationHistoryViewModel: ReservationsViewModel): ReservationsViewModel
}