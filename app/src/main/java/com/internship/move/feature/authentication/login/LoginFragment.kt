package com.internship.move.feature.authentication.login

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.utils.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.launchHomeBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment())
        }
        binding.forgotPasswordTV.addClickableText(text = getString(R.string.forgot_password)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.goToRegisterTV.addClickableText(getString(R.string.goToRegisterLink)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        val editTexts = listOf(binding.emailInputET, binding.passwordInputET)
        for (editText in editTexts) {
            editText.doOnTextChanged { _, _, _, _ ->
                if (binding.emailInputET.text?.isNotEmpty() == true && binding.passwordInputET.text?.isNotEmpty() == true) {
                    binding.launchHomeBtn.isEnabled = true
                    binding.launchHomeBtn.setTextColor(resources.getColor(R.color.neutral_white))
                } else {
                    binding.launchHomeBtn.isEnabled = false
                    binding.launchHomeBtn.setTextColor(resources.getColor(R.color.neutral_pink))
                }
            }
        }
    }

    private fun initViews() {
        binding.forgotPasswordTV.text = getString(R.string.forgot_password)
        binding.goToRegisterTV.text = getString(R.string.goToRegisterText) + " " + getString(R.string.goToRegisterLink)

    }
}