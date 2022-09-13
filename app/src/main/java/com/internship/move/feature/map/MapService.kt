package com.internship.move.feature.map

import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    @GET("scooter/near")
    suspend fun getAllScooters(@Query("longitude") longitude: Float, @Query("latitude") latitude: Float): List<ScooterResponse>
}