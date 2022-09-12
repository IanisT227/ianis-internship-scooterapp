package com.internship.move.feature.licenseRegistration

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.BuildConfig
import com.internship.move.R
import com.internship.move.databinding.FragmentLicenseInstructionsBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class LicenseInstructionsFragment : Fragment(R.layout.fragment_license_instructions) {

    private val binding by viewBinding(FragmentLicenseInstructionsBinding::bind)
    private val args: LicenseInstructionsFragmentArgs by navArgs()
    private lateinit var imageUri: Uri
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.addLicenseBtn.setOnClickListener {
            takeImage()
        }

        binding.bar.setNavigationOnClickListener {
            authenticationViewModel.logOut()
            requireActivity().finish()
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                findNavController().navigate(
                    LicenseInstructionsFragmentDirections.actionLicenseInstructionsFragmentToLicenseConfirmFragment(
                        LicenseItem(args.userData.token, File(imageUri.path.toString()))
                    )
                )
            }
        }
    }

    private var latestTmpUri: Uri? = null

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }
        imageUri = tmpFile.toUri()
        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun initViews() {
        binding.disclaimerTitleTV.text = getString(R.string.license_disclaimer_title_string)
        binding.disclaimerInstructionsTV.text = getString(R.string.disclaimer_instructions_string)
        binding.addLicenseBtn.text = getString(R.string.add_license_button_string)
    }

}