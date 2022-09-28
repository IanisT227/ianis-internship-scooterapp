package com.internship.move.feature.licenseRegistration

import com.internship.move.feature.authentication.UserDTO
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface LicenseService {

    @PUT("users/upload")
    @Multipart
    suspend fun uploadLicense(@Part driverLicenseKey: MultipartBody.Part): UserDTO
}