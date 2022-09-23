package com.internship.move.feature.licenseRegistration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LicenseItem(
    val token: String,
    val imageUri: String
) : Parcelable