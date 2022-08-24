package com.internship.move

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.internship.move.feature.onboarding.OnSkipButtonPressed

class MainActivity : AppCompatActivity(), OnSkipButtonPressed {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    override fun onPressed() {
        setContentView(R.layout.fragment_register)
    }
}