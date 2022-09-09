package com.internship.move.feature.licenseRegistration

import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LicenseService {

    @POST("user/upload")
    @Multipart
    suspend fun uploadLicense(@Header("Authorization") token: String, @Part("driverLicenseKey") image: MultipartBody.Part)
}