package com.internship.move.feature.licenseRegistration

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentLicenseConfirmBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

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
        licenseRegistrationViewModel.uploadImage(File(args.licenseItem.toUri().path.toString()))
    }

    private fun initObservers() {
        licenseRegistrationViewModel.isLoading.observe(viewLifecycleOwner) { loadingValue ->
            binding.apply {
                validationProcessSpn.isVisible = loadingValue
                launchMapBtn.isVisible = !loadingValue
                licenseConfirmIV.isVisible = !loadingValue
                validationStatusTV.isVisible = loadingValue
                validationConfirmTV.isVisible = !loadingValue
            }
        }

        licenseRegistrationViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError.equals(true)) {
                binding.licenseConfirmIV.isVisible = false
                binding.validationConfirmTV.isVisible = true
                binding.validationConfirmTV.text = getString(R.string.license_registration_error_text)
                binding.launchMapBtn.isVisible = true
                binding.launchMapBtn.isEnabled = false
            }
        }
    }

    private fun initButton() {
        binding.launchMapBtn.setOnClickListener {
            findNavController().navigate(LicenseConfirmFragmentDirections.actionLicenseConfirmFragmentToMapFragment())
        }
    }
}