package com.internship.move.feature.menu.rideHistory

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.internship.move.R
import com.internship.move.databinding.FragmentRideHistoryBinding
import com.internship.move.feature.menu.MenuViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RidesHistoryFragment : Fragment(R.layout.fragment_ride_history) {
    private val binding by viewBinding(FragmentRideHistoryBinding::bind)
    private val adapter by lazy { initRidesAdapter() }
    private val ridesViewModel: MenuViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initRidesAdapter()
        initRecyclerView(LinearLayoutManager(requireContext()))
        initObservers()
    }

    private fun initRidesAdapter(): RideHistoryAdapter =
        RideHistoryAdapter(requireContext())


    private fun initRecyclerView(layoutManager: LinearLayoutManager) {
        binding.rideHistoryRV.adapter = adapter
        binding.rideHistoryRV.layoutManager = layoutManager
        ridesViewModel.getUserRides()
    }

    private fun initObservers() {
        ridesViewModel.ridesList.observe(viewLifecycleOwner) { ridesList ->
            adapter.submitList(ridesList)
        }

        ridesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.rideHistoryRV.isVisible = !isLoading
            binding.getListProgressIndicator.isVisible= isLoading
        }
    }

    private fun initButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}