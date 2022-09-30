package com.internship.move.feature.menu.rideHistory

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageDataDTO(
    @Json(name = "pageSize")
    val pageSize: Int = 4,
    @Json(name = "pageNumber")
    val pageNumber: Int = 0
)