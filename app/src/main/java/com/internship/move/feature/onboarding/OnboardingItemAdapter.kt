package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingItemAdapter(fragment: Fragment, private val itemsList: List<OnboardingItem>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = itemsList.size

    override fun createFragment(position: Int): Fragment = OnboardingItemFragment.newInstance(itemsList[position])
}