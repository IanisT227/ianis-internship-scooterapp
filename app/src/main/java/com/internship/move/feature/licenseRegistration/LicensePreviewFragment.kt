package com.internship.move.feature.licenseRegistration

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.internship.move.BuildConfig
import com.internship.move.R
import com.internship.move.databinding.FragmentPreviewLicenseBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class LicensePreviewFragment : Fragment(R.layout.fragment_preview_license) {

    private val binding by viewBinding(FragmentPreviewLicenseBinding::bind)
    private val viewModel: LicenseRegistrationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeImage()
        initListeners()
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                previewImage.setImageURI(uri)
            }
        }
    }

    private var latestTmpUri: Uri? = null

    private val previewImage by lazy { binding.imagePreview }

    private fun initListeners() {
        binding.takeImageButton.setOnClickListener { takeImage() }
        binding.selectImageButton.setOnClickListener {
            //viewModel.uploadImage(token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2MzBmMTBhZDJmNWYwMDIxYjM3NjQ5MzEiLCJpYXQiOjE2NjIwMzExMDN9.FYPMKx4QzRhG_qPSf_Cy9E11XcHCvwGH0YdVU1sVxGI",)
        }
    }

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

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}