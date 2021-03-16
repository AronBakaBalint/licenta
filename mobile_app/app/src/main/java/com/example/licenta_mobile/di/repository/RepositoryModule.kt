package com.example.licenta_mobile.di.repository

import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepository
import com.example.licenta_mobile.repository.parkingspots.ParkingSpotsRepositoryImpl
import com.example.licenta_mobile.repository.prices.PriceRepository
import com.example.licenta_mobile.repository.prices.PriceRepositoryImpl
import com.example.licenta_mobile.repository.reservations.ReservationsRepository
import com.example.licenta_mobile.repository.reservations.ReservationsRepositoryImpl
import com.example.licenta_mobile.repository.user.UserRepository
import com.example.licenta_mobile.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideParkingSpotRepository(repository: ParkingSpotsRepositoryImpl): ParkingSpotsRepository

    @Binds
    abstract fun provideReservationRepository(repository: ReservationsRepositoryImpl): ReservationsRepository

    @Binds
    abstract fun providePricesRepository(repository: PriceRepositoryImpl): PriceRepository
}