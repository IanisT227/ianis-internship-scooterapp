package com.internship.move.feature.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMainMenuBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {
    private val binding by viewBinding(FragmentMainMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initToolbar(){

    }
}