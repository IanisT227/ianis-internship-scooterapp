package com.internship.move.feature.menu

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainMenuUserDTO(
    @Json(name = "username")
    val username: String,
    @Json(name = "numberRides")
    val numberRides: Int,
    @Json(name = "email")
    val email: String
)
