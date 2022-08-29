package com.internship.move.feature.onboarding

import android.content.Context
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
                if (binding.onboardingVP2.currentItem == binding.onboardingVP2.adapter?.itemCount?.minus(2)!!) {
                    binding.nextItemBtn.text = getString(R.string.onboarding_button_start_text)
                }
                binding.onboardingVP2.currentItem = binding.onboardingVP2.currentItem + 1
            } else {
                initPersistence()
                findNavController().navigate(
                    OnboardingFragmentDirections.actionGlobalRegisterFragment()
                )
            }
        }
    }

    private fun initList(): List<OnboardingItem> = mutableListOf<OnboardingItem>(

        OnboardingItem(
            R.drawable.onboarding_1,
            R.string.onboarding_1_title,
            R.string.onboarding_1_body,
        ),
        OnboardingItem(
            R.drawable.onboarding_2,
            R.string.onboarding_2_title,
            R.string.onboarding_2_body,
        ),
        OnboardingItem(
            R.drawable.onboarding_3,
            R.string.onboarding_3_title,
            R.string.onboarding_3_body,
        ),
        OnboardingItem(
            R.drawable.onboarding_4,
            R.string.onboarding_4_title,
            R.string.onboarding_4_body,
        ),
        OnboardingItem(
            R.drawable.onboarding_5,
            R.string.onboarding_5_title,
            R.string.onboarding_5_body,
            false
        )
    )

    private fun initPersistence() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("IS_LOGGED", true)
        editor?.commit()
    }
}