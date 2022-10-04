package com.internship.move.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingPageBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingItemFragment : Fragment(R.layout.fragment_onboarding_page) {

    private val binding by viewBinding(FragmentOnboardingPageBinding::bind)
    private var onboardingItem: OnboardingItem =
        OnboardingItem(R.drawable.onboarding_1, R.string.onboarding_1_title, R.string.onboarding_1_body)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        onboardingItem = arguments?.getParcelable(KEY_ONBOARDING_ITEM)!!
        binding.bodyTV.text = getString(onboardingItem.bodyText)
        binding.titleTV.text = getString(onboardingItem.titleText)
        binding.bannerIV.setImageResource(onboardingItem.imageRes)
        binding.skipBtn.isVisible = onboardingItem.isLastPage
        binding.skipBtn.setOnClickListener {
            (requireActivity() as? OnSkipButtonPressed)?.onPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(receivedOnboardingItem: OnboardingItem) =
            OnboardingItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_ONBOARDING_ITEM, receivedOnboardingItem)
                }
            }

        const val KEY_ONBOARDING_ITEM = "ONBOARDING_ITEM"
    }
}