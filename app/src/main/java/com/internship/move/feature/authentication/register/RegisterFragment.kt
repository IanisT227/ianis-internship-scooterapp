package com.internship.move.feature.authentication.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.utils.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.launchHomeBtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMapFragment())
        }

        binding.goToLoginTV.addClickableText(getString(R.string.launch_login_link))
        {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        binding.termsAndConditionsTV.addClickableText(getString(R.string.t_and_c_link))
        {
            Toast.makeText(requireContext(), "Los termos and conditiones", Toast.LENGTH_SHORT).show()
        }

        binding.termsAndConditionsTV.addClickableText(getString(R.string.privacy_policy_link))
        {
            Toast.makeText(requireContext(), "Los privaciones policiones", Toast.LENGTH_SHORT).show()
        }


//        binding.loginBtn.setOnClickListener {
//            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
//        }
    }

    private fun initViews() {
        binding.goToLoginTV.text = getString(R.string.launch_login_Text) + " " + getString(R.string.launch_login_link)
        binding.termsAndConditionsTV.text =
            getString(R.string.t_and_c_link) + " " + getString(R.string.just_and) + " " + getString(R.string.privacy_policy_link)
    }
}