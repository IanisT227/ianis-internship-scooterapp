package com.internship.move.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.textfield.TextInputEditText
import com.internship.move.R
import com.internship.move.model.ErrorResponse
import com.squareup.moshi.JsonAdapter
import com.tapadoo.alerter.Alerter
import retrofit2.HttpException

fun TextView.addClickableText(text: String, color: Int = context.getColor(R.color.neutral_white), callback: ClickCallBack) {
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = true
            textPaint.color = color
        }

        override fun onClick(view: View) {
            callback.invoke()
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(text, 0)
    spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + text.length, SPAN_EXCLUSIVE_INCLUSIVE)

    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun interface ClickCallBack {

    fun invoke()
}

fun logTag(tag: String, message: String = "") {
    println("[$tag] $message")
}

fun checkMail(mailEditText: TextInputEditText): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(mailEditText.text.toString()).matches() && mailEditText.text?.length!! > 7
}

fun checkUserOrPassword(userOrPasswordText: String?): Boolean {
    return if (userOrPasswordText != null) {
        userOrPasswordText.length > 4
    } else
        false
}

fun showAlerter(bodyText: String, activity: Activity) = Alerter.create(activity)
    .setTitle(R.string.error_text_tapadoo_toast)
    .setText(bodyText)
    .setBackgroundColorRes(R.color.primary_dark_purple)
    .setDuration(ERROR_DURATION)
    .show()


fun bitmapDescriptorFromVector(vectorResId: Int, context: Context): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(vectorDrawable?.intrinsicWidth ?: 0, vectorDrawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable?.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Exception.toErrorResponse(errorResponseDtoJsonAdapter: JsonAdapter<ErrorResponse>): ErrorResponse =
    if (this is HttpException) {
        errorResponseDtoJsonAdapter.fromJson(response()?.errorBody()?.string().toString())
            ?: ErrorResponse(message.toString())
    } else {
        ErrorResponse(message.toString())
    }

const val UNCHECKED = 0
const val LOGGED = 1
const val ERROR = -1
const val ERROR_DURATION = 2500L