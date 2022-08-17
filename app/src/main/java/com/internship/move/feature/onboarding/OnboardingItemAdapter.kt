package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.internship.move.R

class OnboardingItemAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4
    private lateinit var onboardingItemList: List<OnboardingItem>

    override fun createFragment(position: Int): Fragment {
        onboardingItemList = initList()
        val fragment = OnboardingitemFragment.newInstance(onboardingItemList[position])
        return fragment
    }

    private fun initList(): List<OnboardingItem> {
        val onboardingItemsList = mutableListOf<OnboardingItem>()
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_1, "Title1", "Body1", "Next"))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_2, "Title2", "Body2", "Next"))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_3, "Title3", "Body3", "Next"))
        onboardingItemsList.add(OnboardingItem(R.drawable.onboarding_4, "Title4", "Body4", "Get Started"))

        return onboardingItemsList
    }
}