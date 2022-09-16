package com.internship.move.feature.licenseRegistration

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LicenseItem(
    val token: String,
    val imageUri: Uri
) : Parcelable