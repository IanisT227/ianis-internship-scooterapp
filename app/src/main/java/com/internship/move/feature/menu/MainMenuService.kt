package com.internship.move.feature.menu

import com.internship.move.feature.menu.rideHistory.RideHistoryItemDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MainMenuService {

    @GET("users/me")
    suspend fun getUserByToken(): MainMenuUserDTO

    @GET("rides")
    suspend fun getRidesByUser(@Query("pageSize") pageSize: Int, @Query("pageNumber") pageNumber: Int = 0): List<RideHistoryItemDTO>
}