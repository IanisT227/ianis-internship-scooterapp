package com.internship.move.feature.licenseRegistration

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentLicenseConfirmBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LicenseConfirmFragment : Fragment(R.layout.fragment_license_confirm) {

    private val binding by viewBinding(FragmentLicenseConfirmBinding::bind)
    private val licenseRegistrationViewModel: LicenseRegistrationViewModel by viewModel()
    private val args: LicenseConfirmFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        uploadPicture()
        initButton()
    }

    private fun uploadPicture() {
        licenseRegistrationViewModel.uploadImage(args.licenseItem.token, args.licenseItem.image)
    }

    private fun initObservers() {
        licenseRegistrationViewModel.isLoading.observe(viewLifecycleOwner) { loadingValue ->
            binding.apply {
                validationProcessSpn.isVisible = loadingValue
                launchMapBtn.isVisible = !loadingValue
                imageView.isVisible = !loadingValue
                validationStatusTV.text =
                    if (loadingValue) getString(R.string.pre_license_check_text) else getString(R.string.post_license_verification_text)
            }
        }
    }

    private fun initButton() {
        binding.launchMapBtn.setOnClickListener {
            findNavController().navigate(LicenseConfirmFragmentDirections.actionLicenseConfirmFragmentToMapFragment())
        }
    }
}