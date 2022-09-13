package com.internship.move.feature.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val authViewModel: AuthenticationViewModel by viewModel()
    private lateinit var scooterMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getCurrentUser()
        logTag("MAP_USER", authViewModel.getCurrentUser().toString())

        binding.scooterMapView.onCreate(savedInstanceState)
        binding.scooterMapView.onResume()
        initViews()
    }

    private fun initViews() {
        binding.scooterMapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        scooterMap = googleMap
        val Galati = LatLng(45.45, 28.05)
        scooterMap.addMarker(MarkerOptions().position(Galati).title("Galati"))
        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(Galati))
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Galati, 7.0F))
    }
}