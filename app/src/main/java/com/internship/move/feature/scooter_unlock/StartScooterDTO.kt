package com.internship.move.feature.scooter_unlock

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartScooterDTO(
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "scooterNumber")
    val scooterNumber: Int,
    @Json(name = "startMode")
    val startMode: String = "PIN"
)

