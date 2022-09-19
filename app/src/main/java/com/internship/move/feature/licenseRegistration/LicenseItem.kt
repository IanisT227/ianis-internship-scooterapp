package com.internship.move.feature.licenseRegistration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class LicenseItem(
    val token: String,
    val image: File
) : Parcelable