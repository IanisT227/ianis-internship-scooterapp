package com.internship.move.feature.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.feature.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: OnboardingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextFragment()
        }, SPLASH_NAV_DELAY)
    }

    private fun navigateToNextFragment() {

        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.getLoggedStatus()) {
                findNavController().navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
            }
        }
    }

    companion object {
        private const val SPLASH_NAV_DELAY = 2000L
    }
}