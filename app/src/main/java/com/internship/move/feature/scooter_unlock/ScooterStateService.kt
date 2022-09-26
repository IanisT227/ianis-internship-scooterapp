package com.internship.move.feature.scooter_unlock

import com.internship.move.feature.map.ScooterResponseDTO
import retrofit2.http.PUT
import retrofit2.http.Path

interface ScooterStateService {

    @PUT("rides/scan/{scooterNumber}")
    suspend fun startUnlock(@Path(value = "scooterNumber") scooterNumber: Int): ScooterResponseDTO
}