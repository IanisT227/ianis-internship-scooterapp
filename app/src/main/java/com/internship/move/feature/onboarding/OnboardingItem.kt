package com.internship.move.feature.onboarding

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingItem(
    @DrawableRes
    val imageRes: Int,
    @StringRes
    val titleText: Int,
    @StringRes
    val bodyText: Int,
    val isLastPage: Boolean = true
) : Parcelable
