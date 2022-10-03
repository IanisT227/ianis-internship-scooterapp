package com.internship.move.feature.scooter_unlock

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class endRideDTO(
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "endAddress")
    val endAddress: String
) : Parcelable