package com.internship.move.feature.onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingBinding
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private lateinit var viewModel: OnboardingViewModel
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java).also {
            logTag("MVVM_FRAG", "My viewModel: ${it.hashCode()}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initNextButton()
        initObservers()
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
                scope.launch {
                    viewModel.logIn()
                }
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

    private fun initObservers() {
        viewModel.isLogged.observe(viewLifecycleOwner) { isLogged ->
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.putBoolean("IS_LOGGED", isLogged)
            editor?.apply()
        }
    }
}