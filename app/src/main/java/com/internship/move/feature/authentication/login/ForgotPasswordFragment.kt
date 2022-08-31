package com.internship.move.feature.authentication.login

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentForgotPasswordBinding
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.utils.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.goBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.emailInputET.doOnTextChanged { _, _, _, _ ->
            if (binding.emailInputET.text?.isNotEmpty() == true) {
                binding.resetPasswordBtn.isEnabled = true
                binding.resetPasswordBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_white, null))
            } else {
                binding.resetPasswordBtn.isEnabled = false
                binding.resetPasswordBtn.setTextColor(ResourcesCompat.getColor(resources, R.color.neutral_pink, null))
            }
        }
    }
}