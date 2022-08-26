package com.internship.move.feature.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.internship.move.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextFragment()
        }, SPLASH_NAV_DELAY)
    }

    private fun navigateToNextFragment() {
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        if (pref.getBoolean("IS_LOGGED", false)) {
            findNavController().navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
        }
    }

    companion object {
        private const val SPLASH_NAV_DELAY = 2000L
    }
}