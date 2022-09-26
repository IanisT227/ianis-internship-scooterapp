package com.internship.move.feature.scooter_unlock

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentUnlockByCodeBinding
import com.internship.move.utils.logTag
import com.internship.move.utils.showAlerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UnlockByCodeFragment : Fragment(R.layout.fragment_unlock_by_code) {

    private val binding by viewBinding(FragmentUnlockByCodeBinding::bind)
    private val scooterStateViewModel: ScooterStateViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initPinView()
        initObservers()
    }

    private fun initButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initPinView() {
        binding.codePinView.doOnTextChanged { _, _, _, _ ->
            if (binding.codePinView.text?.length == 4) {
                logTag("PIN_VALUE", binding.codePinView.text.toString())
                scooterStateViewModel.startScooterUnlock(binding.codePinView.text.toString().toInt())
            }
        }
    }

    private fun initObservers() {
        scooterStateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.codePinView.isEnabled = !isLoading
            binding.unlockLoadingIndicatorCPI.isVisible = isLoading
        }

        scooterStateViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError.isNotEmpty())
                showAlerter(isError.toString(), requireActivity())
        }

        scooterStateViewModel.scooterResult.observe(viewLifecycleOwner) { scooterResult ->
            if (scooterResult != null) {
                findNavController().navigate(UnlockByCodeFragmentDirections.actionUnlockByCodeFragmentToUnlockSuccessfulFragment())
            }
        }
    }
}