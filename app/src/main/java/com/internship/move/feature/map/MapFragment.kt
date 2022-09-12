package com.internship.move.feature.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val onboardingViewModel: OnboardingViewModel by viewModel()
    private val authViewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getCurrentUser()
        initListeners()
        initViews()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            authViewModel.logOut()
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }

        binding.clearBtn.setOnClickListener {
            onboardingViewModel.changeLogStatus(logValue = false)
            authViewModel.logOut()

            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }
    }


    private fun initViews() {
        binding.mapTV.text = authViewModel.userData.value?.user?.email
    }
}