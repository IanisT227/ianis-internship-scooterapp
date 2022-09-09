package com.internship.move.feature.authentication

import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegisterRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {

    @POST("user/register")
    suspend fun registerUser(@Body userdata: UserRegisterRequest): UserResponse

    @POST("user/login")
    suspend fun loginUser(@Body userdata: UserLogin): UserResponse

    @DELETE("user/logout")
    suspend fun logoutUser(@Header("Authorization") token: String)
}