package com.internship.move.feature.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserDTO(
    @Json(name = "_id")
    val id: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "driverLicenseKey")
    var driverLicenseKey: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "username")
    val username: String
) : Parcelable