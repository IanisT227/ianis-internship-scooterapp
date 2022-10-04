package com.internship.move.feature.menu.resetPassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentAccountResetPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class AccountResetPasswordFragment : Fragment(R.layout.fragment_account_reset_password) {

    private val binding by viewBinding(FragmentAccountResetPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        binding.saveChangesBtn.setOnClickListener {
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}