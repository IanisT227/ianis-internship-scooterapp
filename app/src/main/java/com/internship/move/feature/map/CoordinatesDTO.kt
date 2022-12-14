package com.internship.move.feature.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinatesDTO(
    @Json(name = "coordinates")
    val coordinates: List<Double>
)
