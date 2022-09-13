package com.internship.move

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.internship.move.feature.onboarding.OnSkipButtonPressed
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.feature.splash.SplashFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnSkipButtonPressed {

    private val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPressed() {
        viewModel.changeLogStatus(true)
        findNavController(R.id.navigationHost).navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
    }
}