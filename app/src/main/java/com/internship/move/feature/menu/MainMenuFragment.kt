package com.internship.move.feature.menu

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMainMenuBinding
import com.internship.move.utils.logTag
import com.internship.move.utils.showAlerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {
    private val binding by viewBinding(FragmentMainMenuBinding::bind)
    private val menuViewModel: MenuViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
        initObservers()
        menuViewModel.getUser()
    }

    private fun initButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.seeAllRidesBtn.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToRidesHistoryFragment())
        }

        binding.accessAccountBtn.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToAccountDetailsFragment())
        }

        binding.changePasswordBtn.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToAccountResetPasswordFragment())
        }
    }

    private fun initObservers() {
        menuViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.menuWrapperCL.isClickable = !isLoading
            binding.getUserProcessSpn.isVisible = isLoading
        }

        menuViewModel.userData.observe(viewLifecycleOwner) { userData ->
            logTag("USERDATA_MAIN_MENU", userData.toString())
            binding.toolbar.title = userData.username
            binding.rideHistorySubtitleTV.text = getString(R.string.ride_history_subtitle_text, userData.numberRides)
        }

        menuViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (!isError.isNullOrEmpty())
                showAlerter(isError.toString(), requireActivity())
        }
    }

}