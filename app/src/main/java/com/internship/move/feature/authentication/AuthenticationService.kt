package com.internship.move.feature.authentication

import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegisterRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthenticationService {

    @POST("auth/register")
    suspend fun registerUser(@Body userdata: UserRegisterRequest): UserResponse

    @POST("auth/login")
    suspend fun loginUser(@Body userdata: UserLogin): UserResponse

    @DELETE("auth/logout")
    suspend fun logoutUser()
}