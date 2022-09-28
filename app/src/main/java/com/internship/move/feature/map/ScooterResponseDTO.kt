package com.internship.move.feature.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScooterResponseDTO(
    @Json(name = "location")
    val location: CoordinatesDTO,
    @Json(name = "_id")
    val id: String,
    @Json(name = "scooterNumber")
    val scooterNumber: String,
    @Json(name = "battery")
    val battery: String,
    @Json(name = "bookedStatus")
    val bookedStatus: String,
    @Json(name = "lockedStatus")
    val lockedStatus: String
)