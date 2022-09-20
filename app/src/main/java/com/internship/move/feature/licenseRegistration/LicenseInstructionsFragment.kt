package com.internship.move.feature.licenseRegistration

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
    private lateinit var imageUri: String
    private val authenticationViewModel: AuthenticationViewModel by viewModel()
    private var doubleBackPressed = false
    private var latestTmpUri: Uri? = null
    private val takeImageResult = initImageResult()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.addLicenseBtn.setOnClickListener {
            takeImage()
        }

        binding.bar.setNavigationOnClickListener {
            if (doubleBackPressed) {
                authenticationViewModel.logOut()
                requireActivity().finish()
            } else {
                doubleBackPressed = true
                Toast.makeText(requireContext(), getString(R.string.exit_app_button_text), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackPressed = false
                }, ON_BACK_RESET_DURATION)
            }
        }
    }

    private fun initImageResult(): ActivityResultLauncher<Uri> {
        return registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (!isSuccess) return@registerForActivityResult
            latestTmpUri?.let {
                findNavController().navigate(
                    LicenseInstructionsFragmentDirections.actionLicenseInstructionsFragmentToLicenseConfirmFragment(
                         imageUri
                    )
                )
            }
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
        val tmpFile = File.createTempFile(IMAGE_FILE_PREFIX, IMAGE_FILE_SUFIX).apply {
            createNewFile()
            deleteOnExit()
        }
        imageUri = tmpFile.toString()
        return FileProvider.getUriForFile(requireActivity().applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    companion object {
        const val ON_BACK_RESET_DURATION = 3000L
        const val IMAGE_FILE_PREFIX = "tmp_image_file"
        const val IMAGE_FILE_SUFIX = ".png"
    }
}