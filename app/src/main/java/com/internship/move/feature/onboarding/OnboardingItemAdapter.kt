package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingItemAdapter(fragment: Fragment, itemsList: List<OnboardingItem>) : FragmentStateAdapter(fragment) {

    private val onboardingItemList: List<OnboardingItem> = itemsList

    override fun getItemCount(): Int = onboardingItemList.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingitemFragment.newInstance(onboardingItemList[position])
    }
}