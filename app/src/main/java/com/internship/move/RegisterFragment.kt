package com.internship.move

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.databinding.FragmentRegisterBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons() {
        binding.goHomeBtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMapFragment())
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }
}