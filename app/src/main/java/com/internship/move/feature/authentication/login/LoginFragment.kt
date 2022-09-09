package com.internship.move.feature.authentication.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.ERROR
import com.internship.move.utils.LOGGED
import com.internship.move.utils.addClickableText
import com.internship.move.utils.checkMail
import com.internship.move.utils.checkUserOrPassword
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
            updateLoginButtonState(binding.emailInputET)
        }

        binding.passwordInputET.doOnTextChanged { _, _, _, _ ->
            updateLoginButtonState(binding.passwordInputET)
        }
    }

    private fun updateLoginButtonState(editText: EditText) {
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
        binding.goToRegisterTV.text = "${getString(R.string.goToRegisterText)} ${getString(R.string.goToRegisterLink)}"
    }

    private fun initObservers() {
        viewModel.onUserLoggedIn.observe(viewLifecycleOwner) { logValue ->
            if (logValue == LOGGED) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment(receivedResponse = viewModel.userData.value!!))
            } else if (logValue == ERROR) {
                Toast.makeText(context, getString(R.string.login_error_string), Toast.LENGTH_SHORT).show()
            }
        }
    }
}