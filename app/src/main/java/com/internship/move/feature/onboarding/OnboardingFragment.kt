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
            if (binding.onboardingVP2.currentItem < binding.onboardingVP2.adapter?.itemCount?.minus(1)!!) {
                println("Item no" + binding.onboardingVP2.currentItem)
                if (binding.onboardingVP2.currentItem == binding.onboardingVP2.adapter?.itemCount?.minus(2)!!)
                    binding.nextItemBtn.text = "Get started"
                binding.onboardingVP2.currentItem = binding.onboardingVP2.currentItem + 1
            } else {
                findNavController().navigate(OnboardingFragmentDirections.actionGlobalRegisterFragment())
            }
        }
    }

    private fun initList(): List<OnboardingItem> {
        val onboardingItemsList = mutableListOf<OnboardingItem>()
        onboardingItemsList.add(
            OnboardingItem(
                R.drawable.onboarding_1,
                "Safety",
                "Please wear a helmet and protect yourself while riding",
                true
            )
        )
        onboardingItemsList.add(
            OnboardingItem(
                R.drawable.onboarding_2,
                "Scan",
                "Scan the QR code or NFC sticker on top of the scooter to unlock and ride.",
                true
            )
        )
        onboardingItemsList.add(
            OnboardingItem(
                R.drawable.onboarding_3,
                "Ride",
                "Step on the scooter with one foot and kick off the ground. When the scooter starts to coast, push the right throttle to accelerate.",
                true
            )
        )
        onboardingItemsList.add(
            OnboardingItem(
                R.drawable.onboarding_4,
                "Parking",
                "If convenient, park at a bike rack. If not, park close to the edge of the sidewalk closest to the street. Do not block sidewalks, doors or ramps.",
                true
            )
        )
        onboardingItemsList.add(
            OnboardingItem(
                R.drawable.onboarding_5,
                "Rules",
                "You must be 18 years or and older with a valid driving licence to operate a scooter. Please follow all street signs, signals and markings, and obey local traffic laws.",
                false
            )
        )

        return onboardingItemsList
    }
}