package com.internship.move.feature.scooter_unlock

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "longitutde")
    val longitude: String,
    @Json(name = "latitude")
    val latitude: String
)
