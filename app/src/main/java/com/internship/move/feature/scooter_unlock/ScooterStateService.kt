package com.internship.move.feature.scooter_unlock

import com.internship.move.feature.map.ScooterResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ScooterStateService {

    @PUT("rides/scan/{scooterNumber}")
    suspend fun startUnlock(@Path(value = "scooterNumber") scooterNumber: Int): ScooterResponseDTO

    @PUT("scooters/lock/{scooterNumber}")
    suspend fun lockScooter(@Path(value = "scooterNumber") scooterNumber: Int): ScooterResponseDTO

    @PUT("scooters/unlock/{scooterNumber}")
    suspend fun unlockScooter(@Path(value = "scooterNumber") scooterNumber: Int): ScooterResponseDTO

    @POST("rides")
    suspend fun startRide(@Body startScooterDto: StartScooterDTO): RideDTO

    @PUT("rides/{idRide}")
    suspend fun endRide(@Path(value = "idRide") rideId: String, @Body location: endRideDTO)

    @PUT("rides")
    suspend fun updateRideLocation(@Body location: LocationDTO): RideDTO

    @POST("tcp/{scooterNumber}")
    suspend fun pingScooter(@Path(value = "scooterNumber") scooterNumber: Int, @Body location: LocationDTO)
}