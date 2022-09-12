package com.internship.move.feature.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserResponse(
    @Json(name = "token")
    val token: String,
    @Json(name = "user")
    val user: User
) : Parcelable