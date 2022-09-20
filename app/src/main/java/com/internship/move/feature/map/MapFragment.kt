package com.internship.move.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val authViewModel: AuthenticationViewModel by viewModel()
    private val scooterViewModel: ScooterViewModel by viewModel()
    private lateinit var scooterMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionId = NUMBER_OF_PERMISSIONS
    private var doubleBackPressed = false

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
        scooterViewModel.scooterList.observe(viewLifecycleOwner) { scooterList ->
            if (scooterList.isNotEmpty())
                scooterList.forEach { scooterItem ->
                    scooterMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                scooterItem.location.coordinates[1],
                                scooterItem.location.coordinates[0]
                            )
                        ).title(scooterItem.id)
                            .icon(bitmapDescriptorFromVector(R.drawable.ic_map_pin))
                    )
                }
        }

        scooterViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            scooterMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            scooterViewModel.getScooters(location.latitude.toFloat(), location.longitude.toFloat())
        }
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable?.intrinsicWidth ?: 0, vectorDrawable?.intrinsicHeight ?: 0, ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable?.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun initButtons() {
        requireActivity().onBackPressedDispatcher.addCallback {
            if (doubleBackPressed) {
                authViewModel.logOut()
                requireActivity().finish()
            } else {
                doubleBackPressed = true
                Toast.makeText(requireContext(), getString(R.string.exit_app_button_text), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackPressed = false
                }, ON_BACK_RESET_DURATION)
            }
        }
    }

    private fun initScooterInfo(markerTitle: String) {
        scooterViewModel.scooterList.value?.forEach { scooter ->
            if (scooter.id == markerTitle) {
                binding.apply {
                    scooterInfo.scooterCV.isVisible = true
                    scooterInfo.unlockScooterBtn.text = getString(R.string.unlock_scooter_string)
                    scooterInfo.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
                    scooterInfo.batteryLevelTV.text = getString(R.string.scooter_battery_level_text, scooter.battery)
                    scooterInfo.scooterLocationTV.text = getScooterAddress(scooter.location)
                }
                setBatteryIcon(scooter.battery.toInt())
            }
        }
    }

    private fun setBatteryIcon(batteryLevel: Int) {
        val resource = when {
            batteryLevel >= 81 -> R.drawable.ic_battery_100
            batteryLevel >= 61 -> R.drawable.ic_battery_80
            batteryLevel >= 41 -> R.drawable.ic_battery_60
            batteryLevel >= 21 -> R.drawable.ic_battery_40
            batteryLevel >= 4 -> R.drawable.ic_battery_20
            batteryLevel >= 0 -> R.drawable.ic_battery_0
            else -> R.drawable.ic_battery_0
        }
        binding.scooterInfo.batteryIndicatorIV.setImageResource(resource)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        scooterMap = googleMap
        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(CLUJANGELES))
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CLUJANGELES, ZOOM_LEVEL))
        scooterMap.setOnCameraIdleListener {
            Handler(Looper.getMainLooper()).postDelayed({
                scooterViewModel.currentLocation.value = scooterMap.cameraPosition.target
                logTag("COORDS", scooterViewModel.currentLocation.value.toString())
            }, CHECK_LIST_DELAY)
        }
        scooterMap.setOnMarkerClickListener {
            initScooterInfo(it.title ?: "0000")
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
        )
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLocation()
            }
            else -> {
                Toast.makeText(requireContext(), "Permissions not given", Toast.LENGTH_SHORT).show()
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
                        scooterViewModel.currentLocation.value = LatLng(list[0].latitude, list[0].longitude)
                        val currentLocation = scooterViewModel.currentLocation.value ?: CLUJANGELES
                        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL))
                        scooterViewModel.getScooters(
                            currentLocation.latitude.toFloat(), currentLocation.longitude.toFloat()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.turn_location_message_string), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun getScooterAddress(scooterCoords: CoordinatesDTO): String {
        return getString(
            R.string.scooter_address_string, Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
                scooterCoords.coordinates[1],
                scooterCoords.coordinates[0],
                1
            ).get(0).thoroughfare
                .toString(), Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
                scooterCoords.coordinates[1],
                scooterCoords.coordinates[0],
                1
            ).get(0).subThoroughfare
                .toString()
        )
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
        private const val ZOOM_LEVEL = 18.0f
        private const val NUMBER_OF_PERMISSIONS = 1
        private const val CHECK_LIST_DELAY = 5000L
        private const val ON_BACK_RESET_DURATION = 3000L

    }
}