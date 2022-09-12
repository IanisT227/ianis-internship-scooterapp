package com.internship.move.feature.authentication

import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegister
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {

    @POST("auth/register")
    suspend fun registerUser(@Body userdata: UserRegister): UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body userdata: UserLogin): UserResponse

    @DELETE("auth/logout")
    suspend fun logoutUser(@Header("Authorization") token: String)
}