package com.internship.move.feature.licenseRegistration

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class LicenseInstructionsFragment : Fragment(R.layout.fragment_license_instructions) {

    private val binding by viewBinding(FragmentLicenseInstructionsBinding::bind)
    private val args: LicenseInstructionsFragmentArgs by navArgs()
    private val viewModel: LicenseRegistrationViewModel by viewModel()
    private lateinit var imageUri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObservers()

    }

    private fun initListeners() {
        binding.addLicenseBtn.setOnClickListener {
            takeImage()
        }

        binding.bar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
//                previewImage.setImageURI(uri)
                viewModel.uploadImage(args.userData.token, File(imageUri.path.toString()))
            }
        }
    }

    private var latestTmpUri: Uri? = null

//    private val previewImage by lazy { binding.imagePreview }
//
//    private fun initListeners() {
//        binding.takeImageButton.setOnClickListener { takeImage() }
//        binding.selectImageButton.setOnClickListener {
//            viewModel.uploadImage(
//                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2MzE3MmFlYWVlYzIyMWViN2NmNmZlY2UiLCJpYXQiOjE2NjI5NzQwODJ9.nN7qRF0znzbUnwJ4wGTSGE63sn9UBcAGkewT9xEfQ_s",
//                File(imageUri.path.toString())
//            )
//        }
//    }

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

    private fun initObservers() {
        viewModel.licenseResponse.observe(viewLifecycleOwner) { licenseValue ->
            if (licenseValue != null) {
                Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(LicenseInstructionsFragmentDirections.actionLicenseInstructionsFragmentToLicenseConfirmFragment())
            } else {
                Toast.makeText(requireContext(), "Upload NONO", Toast.LENGTH_SHORT).show()
            }
        }
    }
}