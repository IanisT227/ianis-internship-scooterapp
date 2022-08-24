package com.internship.move.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initNextButton()
    }

    private fun initViewPager() {
        binding.onboardingVP2.isUserInputEnabled = false
        binding.onboardingVP2.adapter = OnboardingItemAdapter(this, initList())
        binding.dotsIndicator.attachTo(binding.onboardingVP2)
    }

    private fun initNextButton() {
        binding.nextItemBtn.setOnClickListener {
            if (binding.onboardingVP2.currentItem < 3) {
                println("Item no" + binding.onboardingVP2.currentItem)
                if (binding.onboardingVP2.currentItem == 2)
                    binding.nextItemBtn.text = "Get started"
                binding.onboardingVP2.currentItem = binding.onboardingVP2.currentItem + 1
            } else {
                findNavController().navigate(OnboardingFragmentDirections.actionGlobalRegisterFragment())
            }
        }
    }

    private fun initList(): List<OnboardingItem> {
        val onboardingItemsList = mutableListOf<OnboardingItem>()
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_1, "Title1", "Body1", true))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_2, "Title2", "Body2", true))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_3, "Title3", "Body3", true))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_4, "Title4", "Body4", false))

        return onboardingItemsList
    }
}