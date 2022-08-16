package com.internship.move

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.databinding.FragmentMapBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons() {
        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationIntro())
        }
    }
}