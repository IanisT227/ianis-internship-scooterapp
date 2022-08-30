package com.internship.move.feature.map

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private lateinit var viewModel: OnboardingViewModel
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java).also {
            logTag("MVVM_FRAG", "My viewModel: ${it.hashCode()}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }

        binding.clearBtn.setOnClickListener {
            scope.launch {
                viewModel.logOut()
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
            }
        }
    }

    private fun initObservers() {
        viewModel.isLogged.observe(viewLifecycleOwner) { isLogged ->
            requireActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("IS_LOGGED", isLogged)
                .apply()
            logTag("OBS_VAL", isLogged.toString())
        }
    }
}