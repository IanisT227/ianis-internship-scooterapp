package com.internship.move.feature.authentication.login

import android.os.Bundle
import android.view.View
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
        binding.forgotPasswordTV.addClickableText(text = getString(R.string.forgot_password))
        {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.goToRegisterTV.addClickableText(getString(R.string.goToRegisterLink))
        {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun initViews() {
        binding.forgotPasswordTV.text = getString(R.string.forgot_password)
        binding.goToRegisterTV.text = getString(R.string.goToRegisterText) + " " + getString(R.string.goToRegisterLink)
    }
}