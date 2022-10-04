package com.internship.move.feature.menu.accountDetails

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentAccountDetailsBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.menu.MenuViewModel
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountDetailsFragment : Fragment(R.layout.fragment_account_details) {
    private val binding by viewBinding(FragmentAccountDetailsBinding::bind)
    private val accountDetailsViewModel: MenuViewModel by viewModel()
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initObservers()
        accountDetailsViewModel.getUser()
    }

    private fun initObservers() {
        accountDetailsViewModel.userData.observe(viewLifecycleOwner) { userData ->
            logTag("USERDATA_MAIN_MENU", userData.toString())
            binding.accountUsernameTV.setText(userData.username, TextView.BufferType.EDITABLE)
            binding.accountEmailTV.setText(userData.email, TextView.BufferType.EDITABLE)
        }

        accountDetailsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.getUserDataProgressIndicator.isVisible = isLoading
        }
    }

    private fun initButtons() {
        binding.logoutBtn.setOnClickListener {
            authenticationViewModel.logOut()
            findNavController().navigate(AccountDetailsFragmentDirections.actionAccountDetailsFragmentToRegisterFragment())
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}