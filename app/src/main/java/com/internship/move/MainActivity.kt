package com.internship.move

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.internship.move.feature.onboarding.OnSkipButtonPressed
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.feature.scooter_unlock.ScooterStateViewModel
import com.internship.move.feature.splash.SplashFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnSkipButtonPressed {

    private val onboardingViewModel: OnboardingViewModel by viewModel()
    private val scooterStateViewModel: ScooterStateViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPressed() {
        onboardingViewModel.changeLogStatus(true)
        findNavController(R.id.navigationHost).navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, RESULT_UNCHANGED_SHOWN)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    v.hideKeyboard()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}