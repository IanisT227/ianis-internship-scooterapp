package com.internship.move.feature.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.internship.move.R
import com.internship.move.feature.authentication.UserResponse
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.utils.logTag
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
                if (viewModel.getAuthStatus().isNullOrEmpty() || viewModel.getAuthStatus()
                        .equals(NULL_USER)
                ) {
                    logTag("USERDATA", viewModel.getAuthStatus().toString())
                    findNavController().navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
                } else {
                    logTag("USERDATA", viewModel.getAuthStatus().toString())
                    val userResponse: UserResponse = Gson().fromJson(
                        viewModel.getAuthStatus(), UserResponse::class.java
                    )

                    findNavController().navigate(SplashFragmentDirections.actionGlobalMapFragment(userResponse))
                }
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
            }
        }
    }

    companion object {
        private const val SPLASH_NAV_DELAY = 2000L
        private const val NULL_USER = "{\"token\":\"\",\"user\":{\"driverLicenseKey\":\"\",\"email\":\"\",\"id\":\"\",\"status\":\"\",\"username\":\"\"}}"
    }
}