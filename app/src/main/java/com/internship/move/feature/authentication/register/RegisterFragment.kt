package com.internship.move.feature.authentication.register

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.ERROR
import com.internship.move.utils.LOGGED
import com.internship.move.utils.addClickableText
import com.internship.move.utils.checkMail
import com.internship.move.utils.checkUserOrPassword
import com.internship.move.utils.logTag
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

            if (!checkUserOrPassword(binding.passwordInputET)) {
                checkStatus = false
                binding.passwordInputTIL.error = "At least 5 characters"
            } else {
                binding.passwordInputTIL.error = null
            }

            if (!checkUserOrPassword(binding.usernameInputET)) {
                checkStatus = false
                binding.usernameInputTIL.error = "At least 5 characters"
            } else {
                binding.usernameInputTIL.error = null
            }

            if (checkStatus) {
                viewModel.register(
                    user = UserRegister(
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
            updateRegisterButtonState(binding.emailInputET)
        }

        binding.passwordInputET.doOnTextChanged { _, _, _, _ ->
            updateRegisterButtonState(binding.passwordInputET)
        }

        binding.usernameInputET.doOnTextChanged { _, _, _, _ ->
            updateRegisterButtonState(binding.usernameInputET)
        }
    }

    private fun updateRegisterButtonState(editText: EditText) {
        if (binding.emailInputET.text?.isNotEmpty() == true && binding.passwordInputET.text?.isNotEmpty() == true && binding.usernameInputET.text?.isNotEmpty() == true) {
            binding.launchHomeBtn.isEnabled = true
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_white, null))
        } else {
            binding.launchHomeBtn.isEnabled = false
            binding.launchHomeBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_pink, null))

        }
    }

    private fun initViews() {
        binding.goToLoginTV.text = "${getString(R.string.launch_login_Text)} ${getString(R.string.launch_login_link)}"
        binding.termsAndConditionsTV.text =
            "${getString(R.string.t_and_c_link)}  ${getString(R.string.just_and)} ${getString(R.string.privacy_policy_link)}"
    }

    private fun initObservers() {
        viewModel.onUserLoggedIn.observe(viewLifecycleOwner) { logValue ->
            logTag("ONLOGGED", viewModel.onUserLoggedIn.value.toString())
            if (logValue == LOGGED) {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLicenseInstructionsFragment(userData = viewModel.userData.value!!))
            } else if (logValue == ERROR) {
                Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }
}