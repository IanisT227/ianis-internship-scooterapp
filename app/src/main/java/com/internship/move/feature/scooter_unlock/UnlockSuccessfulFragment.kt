package com.internship.move.feature.scooter_unlock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R

class UnlockSuccessfulFragment : Fragment(R.layout.fragment_successful_unlock) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(UnlockSuccessfulFragmentDirections.actionUnlockSuccessfulFragmentToMapFragment())
        }, UNLOCK_TO_MAP_DELAY)
    }

    companion object {
        private const val UNLOCK_TO_MAP_DELAY = 2000L
    }
}