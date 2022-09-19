package com.internship.move.feature.licenseRegistration

import com.internship.move.feature.authentication.User
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface LicenseService {

    @PUT("user/upload")
    @Multipart
    suspend fun uploadLicense(@Header("Authorization") token: String, @Part driverLicenseKey: MultipartBody.Part): User
}