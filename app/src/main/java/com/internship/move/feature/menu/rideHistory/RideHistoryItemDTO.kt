package com.internship.move.feature.menu.rideHistory

import android.os.Parcelable
import com.internship.move.feature.scooter_unlock.LocationDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RideHistoryItemDTO(
    @Json(name = "distance")
    val distance: String = "0",
    @Json(name = "coordinates")
    val coordinates: List<LocationDTO>,
    @Json(name = "duration")
    val duration: Int
) : Parcelable
