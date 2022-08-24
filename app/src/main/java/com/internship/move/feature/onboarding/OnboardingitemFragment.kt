package com.internship.move.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.OnboardingViewpagerItemBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingitemFragment() : Fragment(R.layout.onboarding_viewpager_item) {

    private val binding by viewBinding(OnboardingViewpagerItemBinding::bind)
    private var onboardingItem: OnboardingItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        onboardingItem = arguments?.getParcelable(KEY_ONBOARDING_ITEM)
        binding.bodyTV.text = onboardingItem?.bodyText
        binding.titleTV.text = onboardingItem?.titleText
        binding.bannerIV.setImageResource(onboardingItem?.imageURL!!)
        binding.skipTV.isVisible = onboardingItem?.isSkipVisible!!
        binding.skipTV.setOnClickListener {
            (requireActivity() as? OnSkipButtonPressed)?.onPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(receivedOnboardingItem: OnboardingItem) =
            OnboardingitemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_ONBOARDING_ITEM, receivedOnboardingItem)
                }
            }

        const val KEY_ONBOARDING_ITEM = "ONBOARDING_ITEM"
    }
}