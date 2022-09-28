package com.internship.move.feature.scooter_unlock

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "latitude")
    val latitude: Double
)
