package com.internship.move

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.databinding.FragmentOnboardingBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons() {
        binding.onboardingBtn.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionGlobalRegisterFragment())
        }
    }
}