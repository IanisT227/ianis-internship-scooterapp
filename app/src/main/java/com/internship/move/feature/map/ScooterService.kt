package com.internship.move.feature.map

import retrofit2.http.GET
import retrofit2.http.Query

interface ScooterService {

    @GET("scooters/near")
    suspend fun getAllScooters(@Query("longitude") longitude: Float, @Query("latitude") latitude: Float): List<ScooterResponseDTO>
}