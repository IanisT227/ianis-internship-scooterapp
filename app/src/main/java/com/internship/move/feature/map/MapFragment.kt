package com.internship.move.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    private val binding by viewBinding(FragmentMapBinding::bind)
    private val authViewModel: AuthenticationViewModel by viewModel()
    private val mapViewModel: MapViewModel by viewModel()
    private lateinit var scooterMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionId = NUMBER_OF_PERMISSIONS

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getCurrentUser()
        logTag("MAP_USER", authViewModel.getCurrentUser().toString())

        binding.scooterMapView.onCreate(savedInstanceState)
        binding.scooterMapView.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocation()
        initViews()
        initButtons()
        initObservers()
    }

    private fun initViews() {
        binding.scooterMapView.getMapAsync(this)
    }

    private fun initObservers() {
        mapViewModel.scooterList.observe(viewLifecycleOwner) { scooterList ->
            if (scooterList.isNotEmpty())
                scooterList.forEach { scooterItem ->
                    scooterMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                scooterItem.location.coordinates[1],
                                scooterItem.location.coordinates[0]
                            )
                        ).title(scooterItem._id)
                    )
                }
        }

        mapViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            scooterMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            mapViewModel.getScooters(location.latitude.toFloat(), location.longitude.toFloat())
        }
    }

    private fun initButtons() {
        requireActivity().onBackPressedDispatcher.addCallback {
            authViewModel.logOut()
            requireActivity().finish()
        }
    }

    private fun initScooterInfo(markerTitle: String) {
        mapViewModel.scooterList.value?.forEach { scooter ->
            if (scooter._id == markerTitle) {
                binding.apply {
                    scooterInfo.scooterCV.isVisible = true
                    scooterInfo.unlockScooterBtn.text = getString(R.string.unlock_scooter_string)
                    scooterInfo.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
                    scooterInfo.batteryLevelTV.text = getString(R.string.scooter_battery_level_text, scooter.battery)
                    scooterInfo.scooterLocationTV.text = getScooterAddress(scooter.location)
                }
                setBatteryIcon(scooter.battery)
            }
        }
    }

    private fun setBatteryIcon(batteryLevel: String) {
        val resource = when (batteryLevel.toInt()) {
            in 81..100 -> R.drawable.ic_battery_100
            in 61..80 -> R.drawable.ic_battery_80
            in 41..60 -> R.drawable.ic_battery_60
            in 21..40 -> R.drawable.ic_battery_40
            in 3..20 -> R.drawable.ic_battery_20
            in 0..3 -> R.drawable.ic_battery_0
            else -> R.drawable.ic_battery_0
        }
        binding.scooterInfo.batteryIndicatorIV.setImageResource(resource)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        scooterMap = googleMap
        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(CLUJANGELES))
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CLUJANGELES, ZOOM_LEVEL))
        scooterMap.setOnCameraIdleListener {
            Handler(Looper.getMainLooper()).postDelayed({
                mapViewModel.currentLocation.value = scooterMap.cameraPosition.target
                logTag("COORDS", mapViewModel.currentLocation.value.toString())
            }, 5000L)
        }
        scooterMap.setOnMarkerClickListener {
            initScooterInfo(it.title)
            return@setOnMarkerClickListener true
        }

        scooterMap.setOnMapClickListener {
            binding.scooterInfo.scooterCV.visibility = View.GONE
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        mapViewModel.currentLocation.value = LatLng(list[0].latitude, list[0].longitude)
                        val currentLocation = mapViewModel.currentLocation.value ?: CLUJANGELES
                        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL))
                        mapViewModel.getScooters(
                            currentLocation.latitude.toFloat(), currentLocation.longitude.toFloat()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun getScooterAddress(scooterCoords: Coordinates): String {
        return Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
            scooterCoords.coordinates[1],
            scooterCoords.coordinates[0],
            1
        ).get(0).thoroughfare
            .toString() + ", Nr. " + Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
            scooterCoords.coordinates[1],
            scooterCoords.coordinates[0],
            1
        ).get(0).subThoroughfare
            .toString()
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
        private const val ZOOM_LEVEL = 18.0f
        private const val NUMBER_OF_PERMISSIONS = 2
    }
}