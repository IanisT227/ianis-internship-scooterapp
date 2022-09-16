package com.internship.move.feature.authentication.login

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
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

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
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
                binding.emailInputTIL.error = getString(R.string.invalid_email_string)
                checkStatus = false
            } else {
                binding.emailInputET.error = null
            }

            if (!checkUserOrPassword(binding.passwordInputET.text.toString())) {
                binding.passwordInputTIL.error = getString(R.string.invalid_password_string)
            } else {
                binding.passwordInputTIL.error = null
            }

            if (checkStatus) {
                viewModel.logIn(
                    user = UserLogin(
                        email = binding.emailInputET.text.toString(),
                        password = binding.passwordInputET.text.toString()
                    )
                )
            }
        }
        binding.forgotPasswordTV.addClickableText(text = getString(R.string.forgot_password)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }
        binding.goToRegisterTV.addClickableText(getString(R.string.goToRegisterLink)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.emailInputET.doOnTextChanged { _, _, _, _ ->
            updateLoginButtonState()
        }

        binding.passwordInputET.doOnTextChanged { _, _, _, _ ->
            updateLoginButtonState()
        }
    }

    private fun updateLoginButtonState() {
        if (binding.emailInputET.text?.isNotEmpty() == true && binding.passwordInputET.text?.isNotEmpty() == true) {
            binding.launchHomeBtn.isEnabled = true
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_white, null))
        } else {
            binding.launchHomeBtn.isEnabled = false
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_pink, null))
        }
    }

    private fun initViews() {
        binding.forgotPasswordTV.text = getString(R.string.forgot_password)
        binding.goToRegisterTV.text =
            getString(R.string.go_to_register_text, getString(R.string.goToRegisterText), getString(R.string.goToRegisterLink))
    }

    private fun initObservers() {
        viewModel.onUserLoggedIn.observe(viewLifecycleOwner) { logValue ->
            val userResponse = viewModel.userData.value
            if (logValue == LOGGED && userResponse != null) {
                if (userResponse.user.driverLicenseKey == null) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLicenseInstructionsFragment(userData = userResponse))
                } else {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment())
                }
            } else if (logValue == ERROR) {
                Alerter.create(requireActivity())
                    .setTitle(getString(R.string.error_text_tapadoo_toast))
                    .setText(getString(R.string.login_error_string))
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
                launchHomeBtn.isActivated = !logValue
            }
        }
    }
}