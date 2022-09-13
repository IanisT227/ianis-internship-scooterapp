package com.internship.move.utils

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.internship.move.R

fun TextView.addClickableText(text: String, color: Int = context.getColor(R.color.accent_pink), callback: ClickCallBack) {
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

const val UNCHECKED = 0
const val LOGGED = 1
const val ERROR = -1
const val MAPS_API_KEY = "AIzaSyDjK7n4r_ENUgIKBNIsxMD8J09ADCOVUGk"