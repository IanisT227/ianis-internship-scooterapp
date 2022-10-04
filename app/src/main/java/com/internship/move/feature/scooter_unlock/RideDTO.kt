package com.internship.move.feature.scooter_unlock

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RideDTO(
    @Json(name = "distance")
    val distance: String = "0",
    @Json(name = "price")
    val price: String = "0",
    @Json(name = "status")
    val status: String,
    @Json(name = "startTime")
    val startingTime: Long,
    @Json(name = "_id")
    val rideId: String,
    @Json(name = "coordinates")
    val coordinates: List<LocationDTO>,
    @Json(name = "endTime")
    val endTime: Long = 0,
    @Json(name = "duration")
    val duration: Int
)
