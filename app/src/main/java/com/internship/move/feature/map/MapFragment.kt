package com.internship.move.feature.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: OnboardingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }

        binding.clearBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.changeLogStatus(logValue = false)
            }
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }
    }
}