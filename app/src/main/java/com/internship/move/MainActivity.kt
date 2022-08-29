package com.internship.move

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.internship.move.feature.onboarding.OnSkipButtonPressed
import com.internship.move.feature.splash.SplashFragmentDirections

class MainActivity : AppCompatActivity(), OnSkipButtonPressed {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPressed() {
        findNavController(R.id.navigationHost).navigate(SplashFragmentDirections.actionGlobalRegisterFragment())
    }
}