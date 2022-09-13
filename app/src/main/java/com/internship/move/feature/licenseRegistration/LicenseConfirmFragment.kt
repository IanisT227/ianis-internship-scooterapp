package com.internship.move.feature.licenseRegistration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentLicenseConfirmBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
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
            if (loadingValue == true) {
                binding.validationProcessSpn.visibility = View.VISIBLE
                binding.validationStatusTV.text = "We are currently verifying your driving license"
                binding.imageView.visibility = View.GONE
                binding.launchMapBtn.visibility = View.INVISIBLE
            } else {
                binding.validationProcessSpn.visibility = View.GONE
                binding.validationStatusTV.text = "We’ve succesfuly validated your driving license"
                binding.imageView.visibility = View.VISIBLE
                binding.launchMapBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun initButton() {
        binding.launchMapBtn.setOnClickListener {
            findNavController().navigate(LicenseConfirmFragmentDirections.actionLicenseConfirmFragmentToMapFragment())
        }
    }
}