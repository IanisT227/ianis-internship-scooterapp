package com.internship.move.feature.scooter_unlock

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartScooterDTO(
    @Json(name = "longitude")
    val longitude: String,
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "scooterNumber")
    val scooterNumber: String,
    @Json(name = "startMode")
    val startMode: String = "PIN"
)

