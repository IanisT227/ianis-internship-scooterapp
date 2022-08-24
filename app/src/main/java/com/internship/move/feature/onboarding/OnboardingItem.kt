package com.internship.move.feature.onboarding

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingItem(
    val imageURL: Int,
    val titleText: String,
    val bodyText: String,
    val isSkipVisible: Boolean
) : Parcelable
