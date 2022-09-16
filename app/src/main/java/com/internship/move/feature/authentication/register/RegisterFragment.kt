package com.internship.move.feature.authentication.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.ERROR
import com.internship.move.utils.ERROR_DURATION
import com.internship.move.utils.LOGGED
import com.internship.move.utils.addClickableText
import com.internship.move.utils.checkMail
import com.internship.move.utils.checkUserOrPassword
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.launchHomeBtn.setOnClickListener {
            var checkStatus = true

            if (!checkMail(binding.emailInputET)) {
                binding.emailInputTIL.error = "Invalid email"
                checkStatus = false
            } else {
                binding.emailInputTIL.error = null
            }

            if (!checkUserOrPassword(binding.passwordInputET.text.toString())) {
                checkStatus = false
                binding.passwordInputTIL.error = "At least 5 characters"
            } else {
                binding.passwordInputTIL.error = null
            }

            if (!checkUserOrPassword(binding.usernameInputET.text.toString())) {
                checkStatus = false
                binding.usernameInputTIL.error = "At least 5 characters"
            } else {
                binding.usernameInputTIL.error = null
            }

            if (checkStatus) {
                viewModel.register(
                    user = UserRegisterRequest(
                        email = binding.emailInputET.text.toString(),
                        password = binding.passwordInputET.text.toString(),
                        username = binding.usernameInputET.text.toString()
                    )
                )
            }
        }

        binding.goToLoginTV.addClickableText(getString(R.string.launch_login_link)) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        binding.termsAndConditionsTV.addClickableText(getString(R.string.t_and_c_link)) {
            Toast.makeText(requireContext(), "Los termos and conditiones", Toast.LENGTH_SHORT).show()
        }

        binding.termsAndConditionsTV.addClickableText(getString(R.string.privacy_policy_link)) {
            Toast.makeText(requireContext(), "Los privaciones policiones", Toast.LENGTH_SHORT).show()
        }

        binding.emailInputET.doOnTextChanged { _, _, _, _ ->
            updateRegisterButtonState()
        }

        binding.passwordInputET.doOnTextChanged { _, _, _, _ ->
            updateRegisterButtonState()
        }

        binding.usernameInputET.doOnTextChanged { _, _, _, _ ->
            updateRegisterButtonState()
        }
    }

    private fun updateRegisterButtonState() {
        if (binding.emailInputET.text?.isNotEmpty() == true && binding.passwordInputET.text?.isNotEmpty() == true && binding.usernameInputET.text?.isNotEmpty() == true) {
            binding.launchHomeBtn.isEnabled = true
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_white, null))
        } else {
            binding.launchHomeBtn.isEnabled = false
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_pink, null))

        }
    }

    private fun initViews() {
        binding.goToLoginTV.text =
            getString(R.string.launch_login, getString(R.string.launch_login_Text), getString(R.string.launch_login_link))
        binding.termsAndConditionsTV.text = getString(
            R.string.terms_and_conditions_text,
            getString(R.string.terms_and_conditions_intro_text),
            getString(R.string.t_and_c_link),
            getString(R.string.just_and),
            getString(R.string.privacy_policy_link)
        )
    }

    private fun initObservers() {
        viewModel.onUserLoggedIn.observe(viewLifecycleOwner) { logValue ->
            val userResponse = viewModel.userData.value
            if (logValue == LOGGED && userResponse != null) {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLicenseInstructionsFragment(userData = userResponse))
            } else if (logValue == ERROR) {
                Alerter.create(requireActivity())
                    .setTitle(getString(R.string.error_text_tapadoo_toast))
                    .setText(getString(R.string.register_error_message))
                    .setBackgroundColorRes(R.color.primary_dark_purple)
                    .setDuration(ERROR_DURATION)
                    .show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner)
        { logValue ->
            binding.apply {
                validationProcessSpn.isVisible = logValue
                emailInputET.isActivated = !logValue
                passwordInputET.isActivated = !logValue
                usernameInputET.isActivated = !logValue
                launchHomeBtn.isActivated = !logValue
            }

        }
    }
}