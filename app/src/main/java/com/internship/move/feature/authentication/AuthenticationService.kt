package com.internship.move.feature.authentication

import com.internship.move.feature.authentication.login.UserLogin
import com.internship.move.feature.authentication.register.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST("register")
    suspend fun registerUser(@Body userdata: UserRegister)

    @POST("login")
    suspend fun loginUser(@Body userdata: UserLogin): Response<UserLogin>
}