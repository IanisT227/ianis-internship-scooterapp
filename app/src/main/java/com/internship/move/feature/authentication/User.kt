package com.internship.move.feature.authentication

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.sql.ClientInfoStatus

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "_id")
    val id: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "driverLicenseKey")
    val driverLicenseKey: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "username")
    val username: String
)