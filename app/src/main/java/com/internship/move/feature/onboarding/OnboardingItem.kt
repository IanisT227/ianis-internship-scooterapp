package com.internship.move.feature.onboarding

import android.media.Image
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingItem(
    val imageURL: Int,
    val titleText: String,
    val bodyText: String,
    val buttonText: String
) : Parcelable
