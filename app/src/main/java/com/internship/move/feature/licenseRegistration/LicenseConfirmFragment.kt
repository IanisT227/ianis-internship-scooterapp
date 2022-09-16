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
import java.io.File

class LicenseConfirmFragment : Fragment(R.layout.fragment_license_confirm) {

    private val binding by viewBinding(FragmentLicenseConfirmBinding::bind)
    private val licenseRegistrationViewModel: LicenseRegistrationViewModel by viewModel()
    private val args: LicenseConfirmFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        uploadPicture()
        initButton()
    }

    private fun uploadPicture() {
        licenseRegistrationViewModel.uploadImage(args.licenseItem.token, File(args.licenseItem.imageUri.path.toString()))
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
    }

    private fun initViews() {
        binding.validationStatusTV.text = getString(R.string.pre_license_check_text)
        binding.validationConfirmTV.text = getString(R.string.post_license_verification_text)
    }

    private fun initButton() {
        binding.launchMapBtn.setOnClickListener {
            findNavController().navigate(LicenseConfirmFragmentDirections.actionLicenseConfirmFragmentToMapFragment())
        }
    }
}