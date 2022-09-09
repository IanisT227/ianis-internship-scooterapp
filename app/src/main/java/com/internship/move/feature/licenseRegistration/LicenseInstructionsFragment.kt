package com.internship.move.feature.licenseRegistration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentLicenseInstructionsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LicenseInstructionsFragment : Fragment(R.layout.fragment_license_instructions) {

    private val binding by viewBinding(FragmentLicenseInstructionsBinding::bind)
    private val args: LicenseInstructionsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()

        binding.bar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initListeners() {
        binding.addLicenseBtn.setOnClickListener {
            findNavController().navigate(
                LicenseInstructionsFragmentDirections.actionLicenseInstructionsFragmentToLicensePreviewFragment(
                )
            )
        }
    }

    private fun initViews() {
        binding.disclaimerTitleTV.text = getString(R.string.license_disclaimer_title_string)
        binding.disclaimerInstructionsTV.text = getString(R.string.disclaimer_instructions_string)
        binding.addLicenseBtn.text = getString(R.string.add_license_button_string)
    }
}