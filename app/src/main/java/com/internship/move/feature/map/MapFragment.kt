package com.internship.move.feature.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val onboardingViewModel: OnboardingViewModel by viewModel()
    private val authViewModel: AuthenticationViewModel by viewModel()
    private val args: MapFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            authViewModel.deleteToken()
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }

        binding.clearBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onboardingViewModel.changeLogStatus(logValue = false)
                authViewModel.deleteToken()
            }
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }
    }

    private fun initViews() {
        binding.mapTV.text = args.receivedResponse.user.email
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(receivedUser: UserResponse) =
//            MapFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(RECEIVED_USER, receivedUser)
//                }
//            }
//        const val RECEIVED_USER = "RECEIVED_USER"
//    }
}