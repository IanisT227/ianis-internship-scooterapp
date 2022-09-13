package com.internship.move.feature.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScooterResponse(
    @Json(name = "location")
    val location: Coordinates,
    @Json(name = "_id")
    val _id: String,
    @Json(name = "scooterNumber")
    val scooterNumber: String,
    @Json(name = "battery")
    val battery: String,
    @Json(name = "updatedAt")
    val updatedAt: String,
    @Json(name = "bookedStatus")
    val bookedStatus: String,
    @Json(name = "lockedStatus")
    val lockedStatus: String
)